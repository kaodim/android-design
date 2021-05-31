package com.kaodim.design.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaodim.design.R;
import com.kaodim.design.components.callback.NumericControlListener;
import com.kaodim.design.components.handlers.CounterHandler;
import com.kaodim.design.components.utilities.ViewUtils;

import java.math.BigDecimal;

public class NumericControlWithEditText extends RelativeLayout implements CounterHandler.CounterListener {

    ImageView selectionMinusWithEditText, selectionAddWithEditText;
    EditText etNumericDisplayWithEditText;

    CounterHandler counterHandler;
    CounterHandler.Builder builder = new CounterHandler.Builder();

    NumericControlListener listener;

    int min = 0;
    int max = 10;
    int currentValue = 0;

    public NumericControlWithEditText(Context context) {
        super(context);
        init(context,null);
    }

    public NumericControlWithEditText(Context context, AttributeSet set) {
        super(context, set);
        init(context, set);
    }

    public NumericControlWithEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //Retrieve the custom attributes from XML
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.NumericControl);
        currentValue = typedArray.getInteger(R.styleable.NumericControl_value, 0);
        min = typedArray.getInteger(R.styleable.NumericControl_min, 0);
        max = typedArray.getInteger(R.styleable.NumericControl_max, 10);
        boolean enabled = typedArray.getBoolean(R.styleable.NumericControl_android_enabled, true);
        //Recycle the TypedArray (saves memory)
        typedArray.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.kdl_element_numeric_control_with_edit_text_layout, this);
        initComponents();
        this.setEnabled(enabled);
    }

    private void initComponents() {
        selectionMinusWithEditText = findViewById(R.id.selectionMinusWithEditText);
        selectionAddWithEditText = findViewById(R.id.selectionAddWithEditText);
        etNumericDisplayWithEditText = findViewById(R.id.etNumericDisplayWithEditText);

        updateCounter();
        counterHandler = builder
                .startNumber(currentValue)
                .incrementalView(selectionAddWithEditText)
                .decrementalView(selectionMinusWithEditText)
                .editTextView(etNumericDisplayWithEditText)
                .minRange(min) // cant go any less than -50
                .maxRange(max) // cant go any further than 50
                .isCycle(false) // 49,50,-50,-49 and so on
                .counterDelay(150) // speed of counter
                .counterStep(1)  // steps e.g. 0,2,4,6...
                .listener(this) // to listen counter results and show them in app
                .build();
    }

    public void setListener(NumericControlListener listener) {
        this.listener = listener;
    }

    private void updateCounter() {
        if (etNumericDisplayWithEditText != null) {
            etNumericDisplayWithEditText.setText(String.valueOf(currentValue));
        } else {
            Log.d("KAODESIGN", "NumericControl.updateCounter().tvNumericDisplay is null. Failed to set value.");
        }
    }

    public void setValue(int value) {
        this.currentValue = value;
        initComponents();
        toggleButtons();
    }

    public int getValue(){
        return this.currentValue;
    }

    public void setValue(String value) {
        if(checkIfDecimal(value)) {
            this.currentValue = Math.round(Float.parseFloat(value));
        }
        else {
            this.currentValue = Integer.parseInt(value);
        }
        initComponents();
        toggleButtons();
    }

    private boolean checkIfDecimal(String value) {
        Float floatVal = null;
        try {
            floatVal = Float.parseFloat(value);
        } catch (NumberFormatException ex) {
            // Not a float
        }
        return floatVal != null;
    }

    public void setMin(int minValue) {
        this.min = minValue;
        //update Counter builder's minimum & set start number to minimum value
        builder.minRange(min).startNumber(min).build();

        toggleButtons();

        //check if the current value is less than the latest minimum. If so, increment value to latest minimum
        if (currentValue < min) {
            currentValue = min;
            updateCounter();
        }
    }

    public void setMax(int maxValue) {
        this.max = maxValue;
        //update Counter builder's maximum
        builder.maxRange(max).build();

        toggleButtons();

        //check if the current value is greater than the latest maximum. If so, decrement value to latest maximum
        if (currentValue > max) {
            currentValue = max;
            updateCounter();
        }
    }

    @Override
    public void onIncrement(View view, long number) {
        currentValue = Integer.parseInt(etNumericDisplayWithEditText.getText().toString())+ 1;
        updateCounter();

        //if current value is at maximum already, disable further incrementing. vise versa
        toggleButtons();

        //send callback
        if (listener != null) {
            listener.onNumericAdded(currentValue);
            listener.onNumericValueChanged(currentValue);
        }
    }

    @Override
    public void onDecrement(View view, long number) {
        currentValue = Integer.parseInt(etNumericDisplayWithEditText.getText().toString())- 1;
        updateCounter();

        //if current value is at minimum already, disable further decrementing. vise versa
        toggleButtons();

        //send callback
        if (listener != null) {
            listener.onNumericRemoved(currentValue);
            listener.onNumericValueChanged(currentValue);
        }

    }

    @Override
    public void onEditText(View view, long number) {
        etNumericDisplayWithEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (listener != null){
                    try {
                        int editTextValue = Integer.parseInt(s.toString());
                        if (editTextValue != currentValue){
                            currentValue = editTextValue;
                            listener.onNumericValueChanged(currentValue);
                        }
                    }
                    catch(Exception ex) {
                        System.out.println("Error: Could not parse editable to currentvalue, exiting");
                    }
                }
                }
        });
    }

    private void toggleButtons() {
        if (currentValue < max) {
            enableIncrementButton();
            if (currentValue > min) {
                enableDecrementButton();
            } else {
                disableDecrementButton();
            }
        } else {
            disableIncrementButton();
            if (currentValue > min) {
                enableDecrementButton();
            } else {
                disableDecrementButton();
            }
        }
    }

    private void enableDecrementButton() {
        selectionMinusWithEditText.setClickable(true);
        selectionMinusWithEditText.setImageAlpha(255);
    }

    private void disableDecrementButton() {
        selectionMinusWithEditText.setClickable(false);
        selectionMinusWithEditText.setImageAlpha(102);
    }

    private void enableIncrementButton() {
        selectionAddWithEditText.setClickable(true);
        selectionAddWithEditText.setImageAlpha(255);
    }

    private void disableIncrementButton() {
        selectionAddWithEditText.setClickable(false);
        selectionAddWithEditText.setImageAlpha(102);
    }

    @Override
    public void setEnabled(boolean enabled) {
        ViewUtils.recursiveSetEnabled(this, enabled);

        if (enabled) {
            toggleButtons();
        } else {
            disableDecrementButton();
            disableIncrementButton();
        }

        //if you want to remove below line to remove the function of super class.
        super.setEnabled(enabled);
    }

}
