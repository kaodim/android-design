package com.kaodim.design.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaodim.design.R;

public class PricingBottomBar extends LinearLayout {

    String title,price,btnText;
    TextView tvTitle,tvPrice;
    Button btnNext;

    public interface PricingBottomBarButtonListener {
        void onButtonClicked();
    }

    PricingBottomBarButtonListener listener;


    public PricingBottomBar(Context context) {
        super(context);
        init(context,null);
    }

    public PricingBottomBar(Context context, AttributeSet set) {
        super(context, set);
        init(context,set);
    }

    public PricingBottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet atts) {
        //Retrieve the custom attributes from XML
        TypedArray typedArray = context.obtainStyledAttributes(atts,R.styleable.PricingBottomBar);
        title = typedArray.getString(R.styleable.PricingBottomBar_title);
        price = typedArray.getString(R.styleable.PricingBottomBar_pricing);
        btnText = typedArray.getString(R.styleable.PricingBottomBar_btnText);
        //Recycle the TypedArray (saves memory)
        typedArray.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.kdl_pricing_bar, this);
        initComponents();
    }

    private void initComponents() {
        tvPrice = findViewById(R.id.tvTotalAmount);
        tvTitle = findViewById(R.id.tvTotalTitle);
        btnNext = findViewById(R.id.btnProceed);

        tvPrice.setText(price);
        tvTitle.setText(title);
        btnNext.setText(btnText);

        setEvents();
    }

    public void setPriceTitle(String title) {
        this.title = title;
        tvTitle.setText(title);
    }

    public void setPriceString(String priceString) {
        this.price = priceString;
        tvPrice.setText(priceString);
    }

    public void setButtonText(String btnText) {
        this.btnText = btnText;
        btnNext.setText(btnText);
    }

    public void setButtonEnabled(boolean enabled) {
        btnNext.setEnabled(enabled);
    }

    public void setButtonOnClickListener(PricingBottomBarButtonListener pricingBottomBarButtonListener) {
        this.listener = pricingBottomBarButtonListener;
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
