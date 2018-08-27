package com.kaodim.design.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaodim.design.R;

public class TermsBar extends RelativeLayout {

    TextView tvTitle, tvTerms, tvPrivacy;
    String titleText, termsText, privacyText;

    private TermsClickEventsListener listener;

    interface TermsClickEventsListener {
        void onTermsClicked();

        void onPrivacyClicked();
    }

    public TermsBar(Context context) {
        super(context);
        initComponents();
    }

    public TermsBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TermsBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public TermsBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //Retrieve the custom attributes from XML
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.TermsBar);
        titleText = typedArray.getString(R.styleable.TermsBar_titleText);
        termsText = typedArray.getString(R.styleable.TermsBar_termsText);
        privacyText = typedArray.getString(R.styleable.TermsBar_privacyText);
        //Recycle the TypedArray (saves memory)
        typedArray.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.kdl_element_termsbar_layout, this);
        initComponents();
    }

    private void initComponents() {
        tvTitle = findViewById(R.id.tvTitle);
        tvTerms = findViewById(R.id.tvTerms);
        tvPrivacy = findViewById(R.id.tvPrivacy);

        setTitleText(titleText);
        setTermsText(termsText);
        setPrivacyText(privacyText);

        setClickEvents();
    }

    public void setTermsClickEventsListener(TermsClickEventsListener listener) {
        this.listener = listener;
    }

    private void setClickEvents() {
        tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onPrivacyClicked();
            }
        });

        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onTermsClicked();
            }
        });
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
        tvTitle.setText(titleText);
    }

    public void setTermsText(String termsText) {
        this.termsText = termsText;
        tvTerms.setText(termsText);
    }

    public void setPrivacyText(String privacyText) {
        this.privacyText = privacyText;
        tvPrivacy.setText(privacyText);
    }

}
