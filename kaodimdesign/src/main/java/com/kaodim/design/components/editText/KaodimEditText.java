package com.kaodim.design.components.editText;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaodim.design.R;

public class KaodimEditText extends LinearLayout {

    private final static long HINT_ANIMATION_DURATION = 200;
    private final static float HINT_SHRINK_SCALE = 0.75f;
    private final static float HALF = 0.5f;

    private final static int INPUT_TYPE_TEXT = 0;
    private final static int INPUT_TYPE_NUMBER = 1;

    private int inputType;
    private boolean isSecured;
    private boolean isFirstLetterCapitalize;
    private EditText etCustomInput;
    private TextView tvCustomHint;
    private TextView tvCustomError;
    private ImageView ivKdlTextInputError;
    private ImageView ivKdlTextInputClear;
    private ConstraintLayout clKdlTextInputInput;

    public KaodimEditText(Context context) {
        super(context);
        this.initComponents();
        this.setEvents();
    }

    public KaodimEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public KaodimEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public EditText getInputEditText() { return this.etCustomInput; }

    private void init(Context context, AttributeSet attrs) {
        //Retrieve the custom attributes from XML
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.KaodimEditTextLayout);
        String hintText = typedArray.getString(R.styleable.KaodimEditTextLayout_android_hint);
        String errorText = typedArray.getString(R.styleable.KaodimEditTextLayout_errorText);
        String inputText = typedArray.getString(R.styleable.KaodimEditTextLayout_android_text);
        boolean enabled = typedArray.getBoolean(R.styleable.KaodimEditTextLayout_android_enabled, true);
        inputType = typedArray.getInt(R.styleable.KaodimEditTextLayout_inputType, 0);
        isSecured = typedArray.getBoolean(R.styleable.KaodimEditTextLayout_secured, false);
        isFirstLetterCapitalize = typedArray.getBoolean(R.styleable.KaodimEditTextLayout_capitalize, false);
        //Recycle the TypedArray (saves memory)
        typedArray.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.kdl_edit_text_layout, this);
        initComponents();
        this.setEvents();
        this.setupView(hintText, errorText, inputText, enabled);
    }

    private void setEvents() {
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (getHeight() > 0) {
                    setPadding(!TextUtils.isEmpty(etCustomInput.getText()));
                    tvCustomHint.setScaleX(!TextUtils.isEmpty(etCustomInput.getText()) || etCustomInput.isFocused() ? HINT_SHRINK_SCALE:1);
                    tvCustomHint.setScaleY(!TextUtils.isEmpty(etCustomInput.getText()) || etCustomInput.isFocused() ? HINT_SHRINK_SCALE:1);
                    tvCustomHint.setTranslationX(!TextUtils.isEmpty(etCustomInput.getText()) || etCustomInput.isFocused()?  getHintLateralTranslation(): 0f);
                    tvCustomHint.setTranslationY(!TextUtils.isEmpty(etCustomInput.getText()) || etCustomInput.isFocused()? getHintLongitudinalTranslation(): 0f);
                    if (!isEnabled()) {
                        etCustomInput.setTextColor(ContextCompat.getColor(getContext(), R.color.text_lightgrey));
                        tvCustomHint.setTextColor(ContextCompat.getColor(getContext(), !TextUtils.isEmpty(etCustomInput.getText())? R.color.text_midgrey: R.color.text_lightgrey));
                    } else {
                        etCustomInput.setTextColor(ContextCompat.getColor(getContext(), R.color.text_midgrey));
                        tvCustomHint.setTextColor(ContextCompat.getColor(getContext(), !TextUtils.isEmpty(etCustomInput.getText()) && !etCustomInput.isFocused()? R.color.text_lightgrey: R.color.text_midgrey));

                    }
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        etCustomInput.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, final boolean hasFocus) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (hasFocus) {
                            inputMethodManager.showSoftInput(etCustomInput, InputMethodManager.SHOW_IMPLICIT);
                        } else {
                            inputMethodManager.hideSoftInputFromWindow(etCustomInput.getWindowToken(), 0);
                        }
                    }
                });
                etCustomInput.setInputType(getInputType(inputType, isSecured, hasFocus, isFirstLetterCapitalize ));
                etCustomInput.setSelection(etCustomInput.getText().length());
                // Update background
                Drawable drawable = getContext().getDrawable(getErrorText().isEmpty() ? (etCustomInput.isFocused() ? R.drawable.bg_edittext_focused: R.drawable.bg_edittext_default):R.drawable.bg_edittext_error);
                clKdlTextInputInput.setBackground(drawable);
                ivKdlTextInputError.setVisibility(!hasFocus && !getErrorText().isEmpty() ? View.VISIBLE: View.GONE);
                ivKdlTextInputClear.setVisibility(hasFocus && !getText().isEmpty() ? View.VISIBLE: View.GONE);
                setPadding(hasFocus || !TextUtils.isEmpty(etCustomInput.getText()));
                animateColorHint(hasFocus || TextUtils.isEmpty(etCustomInput.getText()));
                animateScaleHint(!hasFocus && TextUtils.isEmpty(etCustomInput.getText()));
            }
        });

        etCustomInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                ivKdlTextInputError.setVisibility(!etCustomInput.isFocused() && !getErrorText().isEmpty() ? View.VISIBLE: View.GONE);
                ivKdlTextInputClear.setVisibility(etCustomInput.isFocused() && !text.isEmpty() ? View.VISIBLE: View.GONE);
            }
        });


        tvCustomError.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();

                // Update background
                Drawable drawable = getContext().getDrawable(text.isEmpty() ? (etCustomInput.isFocused() ? R.drawable.bg_edittext_focused: R.drawable.bg_edittext_default):R.drawable.bg_edittext_error);
                clKdlTextInputInput.setBackground(drawable);

                // Update visibility
                tvCustomError.setVisibility(text.isEmpty() ? View.GONE: View.VISIBLE);

                ivKdlTextInputError.setVisibility(!etCustomInput.isFocused() && !text.isEmpty() ? View.VISIBLE: View.GONE);
                ivKdlTextInputClear.setVisibility(etCustomInput.isFocused() && !getText().isEmpty() ? View.VISIBLE: View.GONE);
            }
        });

        ivKdlTextInputClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setText("");
            }
        });
    }

    private void initComponents() {
        etCustomInput = findViewById(R.id.etKdlTextInputInput);
        tvCustomHint = findViewById(R.id.tvKdlTextInputHint);
        tvCustomError = findViewById(R.id.tvKdlTextInputError);
        ivKdlTextInputClear = findViewById(R.id.ivKdlTextInputClear);
        ivKdlTextInputError = findViewById(R.id.ivKdlTextInputError);
        clKdlTextInputInput = findViewById(R.id.clKdlTextInputInput);
    }

    private void setupView(String hintText, String errorText, String inputText, boolean enabled) {
        this.setHint(hintText);
        this.setErrorText(errorText);
        this.setText(inputText);
        this.setEnabled(enabled);
        etCustomInput.setShowSoftInputOnFocus(true);
        etCustomInput.setInputType(getInputType(inputType, isSecured, etCustomInput.isFocused(),isFirstLetterCapitalize));
    }

    private void animateColorHint(boolean hasFocus) {
        int start = tvCustomHint.getCurrentTextColor();
        int end = ContextCompat.getColor(getContext(), hasFocus ? R.color.text_midgrey: R.color.text_lightgrey);
        ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), start, end);
        animator.setDuration(HINT_ANIMATION_DURATION);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                tvCustomHint.setTextColor((int)valueAnimator.getAnimatedValue());
            }
        });
        animator.start();
    }

    private void animateScaleHint(boolean grow) {
        float scale = grow ? 1f: HINT_SHRINK_SCALE;
        float translationX = grow ? 0f: getHintLateralTranslation();
        float translationY = grow ? 0f:  getHintLongitudinalTranslation();

        tvCustomHint.animate()
                .scaleX(scale)
                .scaleY(scale)
                .translationX(translationX)
                .translationY(translationY)
                .setDuration(HINT_ANIMATION_DURATION)
                .start();
    }

    private void setPadding(boolean isFocused) {
        int paddingBottom = getResources().getDimensionPixelSize(isFocused ? R.dimen.size_10_dp: R.dimen.size_20_dp);
        int paddingTop =  getResources().getDimensionPixelSize(isFocused ? R.dimen.size_30_dp: R.dimen.size_20_dp);
        etCustomInput.setPadding(etCustomInput.getPaddingLeft(), paddingTop, etCustomInput.getPaddingRight(), paddingBottom);
    }

    private float getHintLateralTranslation() {
        float width = tvCustomHint.getWidth();
        return -((width - HINT_SHRINK_SCALE * width) * HALF);
    }

    private float getHintLongitudinalTranslation() {
        int paddingTop = getResources().getDimensionPixelSize(R.dimen.size_8_dp);
        float editTextHeight = etCustomInput.getHeight();
        float hintHeight = tvCustomHint.getHeight();
        return -((editTextHeight - hintHeight) * HALF - paddingTop);
    }


    public void setHint(String hint) {
        tvCustomHint.setText(hint);
    }

    public void setErrorText(String errorText) {
        tvCustomError.setText(errorText);
    }

    public String getErrorText() {
        return this.tvCustomError.getText().toString();
    }

    public void setText(String text) {
        etCustomInput.setText(text);
    }

    public String getText() {
        return this.etCustomInput.getText().toString();
    }

    public void setSecured(boolean secured) {
        this.isSecured = secured;
        etCustomInput.setInputType(getInputType(inputType, isSecured, etCustomInput.isFocused(), isFirstLetterCapitalize ));
    }

    public boolean getSecured() {
        return this.isSecured;
    }

    public void setEnabled(boolean enabled) {
        recursiveSetEnabled(this, enabled);
        super.setEnabled(enabled);
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        etCustomInput.addTextChangedListener(textWatcher);
    }

    private int getInputType(int customInputType, boolean isSecured, boolean isFocused, boolean isCapitalized) {
        int inputType = InputType.TYPE_CLASS_TEXT;
        if (customInputType == INPUT_TYPE_NUMBER) {
            inputType = InputType.TYPE_CLASS_NUMBER;
            if (isSecured && !isFocused) {
                inputType = inputType | InputType.TYPE_NUMBER_VARIATION_PASSWORD;
            }
        } else {
            if (isCapitalized) {
                inputType = inputType | InputType.TYPE_TEXT_FLAG_CAP_WORDS;
            }
            if (isSecured && !isFocused) {
                inputType = inputType | InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }
        }

        return inputType;
    }

    private static void recursiveSetEnabled(ViewGroup vg, boolean enabled) {
        int i = 0;

        for(int count = vg.getChildCount(); i < count; ++i) {
            View child = vg.getChildAt(i);
            child.setEnabled(enabled);
            if (child instanceof ViewGroup) {
                recursiveSetEnabled((ViewGroup)child, enabled);
            }
        }

    }
}
