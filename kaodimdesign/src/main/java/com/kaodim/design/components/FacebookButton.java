package com.kaodim.design.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaodim.design.R;

public class FacebookButton extends RelativeLayout {

    TextView tvHint;
    RelativeLayout rlParent;

    String hint = "Continue with Facebook";

    private FacebookButtonClickListener listener;

    public interface FacebookButtonClickListener {
        void onFacebookButtonClicked();
    }

    public FacebookButton(Context context) {
        super(context);
        initComponents();
    }

    public FacebookButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FacebookButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public FacebookButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //Retrieve the custom attributes from XML
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FacebookButton);
        hint = typedArray.getString(R.styleable.FacebookButton_fbHint);
        //Recycle the TypedArray (saves memory)
        typedArray.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.kdl_element_facebook_btn_layout, this);
        initComponents();
    }

    private void initComponents() {
        tvHint = findViewById(R.id.tvHint);
        rlParent = findViewById(R.id.rlParent);

        setHintText(hint);

        setClickEvents();
    }

    public void setOnClickListener(FacebookButtonClickListener listener) {
        this.listener = listener;
    }

    private void setClickEvents() {
        rlParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onFacebookButtonClicked();
            }
        });
    }

    public void setHintText(String hint) {
        this.hint = hint;
        tvHint.setText(hint);
    }

}
