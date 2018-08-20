package com.kaodim.design.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaodim.design.R;
import com.kaodim.design.components.callbacks.NumericControlListener;
import com.kaodim.design.components.helpers.CounterHandler;

import java.math.BigDecimal;

public class NumericControl extends RelativeLayout implements CounterHandler.CounterListener {

    ImageView selectionMinus, selectionAdd;
    TextView tvNumericDisplay;

    CounterHandler counterHandler;
    CounterHandler.Builder builder = new CounterHandler.Builder();

    NumericControlListener listener;

    int min = 0;
    int max = 10;
    int currentValue = 0;

    public NumericControl(Context context) {
        super(context);
        init(context);
    }

    public NumericControl(Context context, AttributeSet set) {
        super(context, set);
        init(context);
    }

    public NumericControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //Retrieve the custom attributes from XML
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.SearchBox);
        currentValue = typedArray.getInteger(R.styleable.NumericControl_value, 0);
        min = typedArray.getInteger(R.styleable.NumericControl_min, 0);
        max = typedArray.getInteger(R.styleable.NumericControl_max, 10);
        //Recycle the TypedArray (saves memory)
        typedArray.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.kdl_element_numeric_control_layout, this);
        initComponents();
    }

    private void initComponents() {
        selectionMinus = findViewById(R.id.selectionMinus);
        selectionAdd = findViewById(R.id.selectionAdd);
        tvNumericDisplay = findViewById(R.id.tvNumericDisplay);

        updateCounter();

        counterHandler = builder
                .startNumber(currentValue)
                .incrementalView(selectionAdd)
                .decrementalView(selectionMinus)
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
        if (tvNumericDisplay != null) {
            tvNumericDisplay.setText(String.valueOf(currentValue));
        } else {
            Log.d("KAODESIGN", "NumericControl.updateCounter().tvNumericDisplay is null. Failed to set value.");
        }
    }

    public void setValue(int value) {
        this.currentValue = value;
        initComponents();
        toggleButtons();
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
        currentValue = new BigDecimal(number).intValueExact();
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
        currentValue = new BigDecimal(number).intValueExact();
        updateCounter();

        //if current value is at minimum already, disable further decrementing. vise versa
        toggleButtons();

        //send callback
        if (listener != null) {
            listener.onNumericRemoved(currentValue);
            listener.onNumericValueChanged(currentValue);
        }
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
        selectionMinus.setClickable(true);
        selectionMinus.setImageAlpha(255);
    }

    private void disableDecrementButton() {
        selectionMinus.setClickable(false);
        selectionMinus.setImageAlpha(102);
    }

    private void enableIncrementButton() {
        selectionAdd.setClickable(true);
        selectionAdd.setImageAlpha(255);
    }

    private void disableIncrementButton() {
        selectionAdd.setClickable(false);
        selectionAdd.setImageAlpha(102);
    }

}
