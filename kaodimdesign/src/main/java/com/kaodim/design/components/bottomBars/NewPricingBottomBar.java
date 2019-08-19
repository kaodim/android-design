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

public class NewPricingBottomBar extends LinearLayout {

    String title,price,btnText,priceType, priceTypeSession, priceUpcomingSession, priceFirstSession;
    TextView tvTitle,tvPrice,tvPriceType, tvPriceTypeSession,
            tvFirstSessionPrice,tvUpcomingSessionPrice,
            tvLabelPriceTypeSession, tvLableFirstSessionPrice,tvLabelUpcomingSessionPrice
            ,tvTitleSession;
    Button btnNext;
    LinearLayout dynamicPrice;
    LinearLayout dynamicPriceSession;

    public interface PricingBottomBarButtonListener {
        void onButtonClicked();
    }

    PricingBottomBarButtonListener listener;


    public NewPricingBottomBar(Context context) {
        super(context);
        init(context,null);
    }

    public NewPricingBottomBar(Context context, AttributeSet set) {
        super(context, set);
        init(context,set);
    }

    public NewPricingBottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
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
        inflater.inflate(R.layout.kdl_new_pricing_bar, this);
        initComponents();
    }

    private void initComponents() {
        tvPrice = findViewById(R.id.tvAmount);
        tvPriceType = findViewById(R.id.tvPriceType);
        tvTitle = findViewById(R.id.tvTitle);
        btnNext = findViewById(R.id.btnProceed);
        dynamicPrice = findViewById(R.id.llDynamicPricing);
        dynamicPriceSession = findViewById(R.id.llDynamicSessionPricing);
        tvFirstSessionPrice = findViewById(R.id.tvFirstSession);
        tvUpcomingSessionPrice = findViewById(R.id.tvUpcomingSession);
        tvLableFirstSessionPrice = findViewById(R.id.tvFirstSessionLable);
        tvUpcomingSessionPrice = findViewById(R.id.tvUpcomingSession);
        tvLabelUpcomingSessionPrice = findViewById(R.id.tvUpcomingLable);
        tvPriceTypeSession = findViewById(R.id.tvPriceTypeSession);
        tvTitleSession = findViewById(R.id.tvTitleSession);

        tvPrice.setText(price);
        tvTitle.setText(title);
        btnNext.setText(btnText);

        setEvents();
    }

//    public void setPriceTitle(String title) {
//        this.title = title;
//        tvTitle.setText(title);
//    }
//
//    public void setPriceString(String priceString) {
//        this.price = priceString;
//        tvPrice.setText(priceString);
//    }
//
//    public void setPriceTypeString(String priceType) {
//        this.priceType = priceType;
//        tvPrice.setText(priceType);
//    }
//
    public void setButtonText(String btnText) {
        this.btnText = btnText;
        btnNext.setText(btnText);
    }

    public void setNonSessionLabels(String title, String priceType){
        tvTitle.setText(title);
        tvPriceType.setText(priceType);
    }

    public void setNonSessionPrice(String price){
        tvPrice.setText(price);
    }


    public void setPriceContainer(boolean priceAvailable,boolean hasSession) {
        if(priceAvailable && hasSession){
            dynamicPriceSession.setVisibility(View.VISIBLE);
            dynamicPrice.setVisibility(View.GONE);
        }else if (priceAvailable && !hasSession) {
            dynamicPrice.setVisibility(View.VISIBLE);
            dynamicPriceSession.setVisibility(View.GONE);
        }else{
            dynamicPrice.setVisibility(View.GONE);
            dynamicPriceSession.setVisibility(View.GONE);
        }
    }

    public void setSessionPricingDetails(String priceUpcomingSession,String priceFirstSession){
        tvFirstSessionPrice.setText(priceFirstSession);
        tvUpcomingSessionPrice.setText(priceUpcomingSession);
    }

    public void setSessionLabels(String title, String priceType, String upComingLable, String firstSessionLable){
        tvTitleSession.setText(title);
        tvPriceTypeSession.setText(priceType);
        tvLabelUpcomingSessionPrice.setText(upComingLable);
        tvLableFirstSessionPrice.setText(firstSessionLable);
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
