package com.kaodim.design.components.bottomBars;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaodim.design.R;

public class FullWidthBottomBar extends LinearLayout{

    String title,price,btnText;
    TextView tvTitle,tvPrice;
    Button btnNext;
    boolean isButtonDisabled = false;

    public interface FullWidthBottomBarButtonListener {
        void onButtonClicked();
    }

    FullWidthBottomBarButtonListener listener;


    public FullWidthBottomBar(Context context) {
        super(context);
        init(context,null);
    }

    public FullWidthBottomBar(Context context, AttributeSet set) {
        super(context, set);
        init(context,set);
    }

    public FullWidthBottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet atts) {
        //Retrieve the custom attributes from XML
        TypedArray typedArray = context.obtainStyledAttributes(atts, R.styleable.FullWidthBottomBar);
        btnText = typedArray.getString(R.styleable.FullWidthBottomBar_btnFullWidthText);
        isButtonDisabled = typedArray.getBoolean(R.styleable.FullWidthBottomBar_btnEnabled, true);
        //Recycle the TypedArray (saves memory)
        typedArray.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.full_width_bottom_par, this);
        initComponents();
    }

    private void initComponents() {
        btnNext = findViewById(R.id.btnFullBottomBar);
        btnNext.setText(btnText);
        btnNext.setEnabled(isButtonDisabled);

        setEvents();
    }

    public void setButtonText(String btnText) {
        this.btnText = btnText;
        btnNext.setText(btnText);
    }

    public void setEnabled(boolean enabled) {
        this.isButtonDisabled = enabled;
        btnNext.setEnabled(isButtonDisabled);
    }

    public void setButtonEnabled(boolean enabled) {
        btnNext.setEnabled(enabled);
    }

    public void setButtonOnClickListener(FullWidthBottomBarButtonListener fullWidthBottomBarButtonListener) {
        this.listener = fullWidthBottomBarButtonListener;
    }

    private void setEvents() {
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onButtonClicked();
                }
            }
        });
    }

}
