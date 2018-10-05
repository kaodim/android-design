package com.kaodim.design.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaodim.design.R;

public class PasswordField extends RelativeLayout {

    private String hintTitle = "Password";
    private String showText = "Show";
    private String hideText = "Hide";
    private boolean isVisible = false;
    private String inputValue = "";
    private PasswordChangedListener listener;
    private String inputError = "";

    EditText etPassword;
    TextView tvHintTitle;
    View viewMargin;
    TextView tvVisibilityToggle;
    TextView tvError;

    public interface PasswordChangedListener {
        void onPasswordChanged(String password);
    }

    public PasswordField(Context context) {
        super(context);
        initComponents();
    }

    public PasswordField(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PasswordField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public PasswordField(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //Retrieve the custom attributes from XML
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PasswordField);
        hintTitle = typedArray.getString(R.styleable.PasswordField_passwordHintTitle);
        //Recycle the TypedArray (saves memory)
        typedArray.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.kdl_element_password_field_layout, this);
        initComponents();
    }

    private void initComponents() {
        etPassword = findViewById(R.id.etPasswordInput);
        tvHintTitle = findViewById(R.id.tvHintTitle);
        viewMargin = findViewById(R.id.viewMargin);
        tvVisibilityToggle = findViewById(R.id.tvVisibilityToggle);
        tvError = findViewById(R.id.tvError);

        setHintTitle(hintTitle);

        setEvents();
    }

    public void initialize(String hintTitle, String showText, String hideText) {
        this.hintTitle = hintTitle;
        this.showText = showText;
        this.hideText = hideText;
        isVisible = false;
        tvVisibilityToggle.setText(showText);
        setHintTitle(hintTitle);
    }

    private void setEvents() {
        etPassword.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    viewMargin.setBackgroundResource(R.color.blackpearl);
                } else {
                    viewMargin.setBackgroundResource(R.color.kdl_grey);
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                inputValue = etPassword.getText().toString();

                if (listener != null)
                    listener.onPasswordChanged(inputValue);
            }
        });

        tvVisibilityToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible) {
                    isVisible = false;
                    tvVisibilityToggle.setText(showText);
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etPassword.setSelection(etPassword.getText().length());
                } else {
                    isVisible = true;
                    tvVisibilityToggle.setText(hideText);
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    etPassword.setSelection(etPassword.getText().length());
                }
            }
        });
    }

    public void setPasswordChangedListener(PasswordChangedListener listener) {
        this.listener = listener;
    }

    public void setHintTitle(String hintTitle) {
        this.hintTitle = hintTitle;
        tvHintTitle.setText(hintTitle);
    }

    public void setText(String inputValue) {
        this.inputValue = inputValue;
        etPassword.setText(inputValue);
    }

    public String getText() {
        return inputValue;
    }

    public void setInputError(String inputError) {
        this.inputError = inputError;
        toggleError();
    }

    public void clearInputError() {
        setInputError("");
    }

    private void toggleError() {
        if (inputError.length() > 0) {
            tvError.setVisibility(VISIBLE);
            tvError.setText(inputError);
            tvHintTitle.setTextColor(getResources().getColor(R.color.jasper));
        } else {
            tvError.setVisibility(INVISIBLE);
            tvError.setText("");
            tvHintTitle.setTextColor(getResources().getColor(R.color.kdl_grey_medium));
        }
    }
}
