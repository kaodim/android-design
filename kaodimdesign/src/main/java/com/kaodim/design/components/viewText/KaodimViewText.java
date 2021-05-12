package com.kaodim.design.components.viewText;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaodim.design.R;
import com.kaodim.design.components.utilities.ViewUtils;

public class KaodimViewText extends ConstraintLayout {

    private final static long HINT_ANIMATION_DURATION = 200;
    private final static float HINT_SHRINK_SCALE = 0.75f;
    private final static float HALF = 0.5f;

    private CustomTextListener listener;

    private TextView tvKdlTextSelectionSelect;
    private TextView tvCustomHint;
    private ConstraintLayout clKdlTextSelectionRoot;
    private ImageView ivLeftIcon;
    private ImageView ivRightIcon;
//    private ArrayAdapter adapter;

    private boolean initialUpdateFinished = false;

    public KaodimViewText(Context context) {
        super(context);
        this.initComponents();
        this.setEvents();
    }

    public KaodimViewText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public KaodimViewText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        //Retrieve the custom attributes from XML
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.KaodimViewTextLayout);
        String hintText = typedArray.getString(R.styleable.KaodimViewTextLayout_android_hint);
        boolean enabled = typedArray.getBoolean(R.styleable.KaodimViewTextLayout_android_enabled, true);
        int leftIconVisibility = typedArray.getInt(R.styleable.KaodimViewTextLayout_leftIconVisibility, GONE);
        int rightIconVisibility = typedArray.getInt(R.styleable.KaodimViewTextLayout_rightIconVisibility, GONE);
        int leftIconDrawable = typedArray.getResourceId(R.styleable.KaodimViewTextLayout_leftIconDrawable, R.drawable.ic_minical);
        int rightIconDrawable = typedArray.getResourceId(R.styleable.KaodimViewTextLayout_rightIconDrawable, R.drawable.icon_chevron_down);

        //Recycle the TypedArray (saves memory)
        typedArray.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.kdl_view_text_layout, this);
        initComponents();
        this.setEvents();
        this.setupView(hintText, enabled, leftIconVisibility, rightIconVisibility, leftIconDrawable, rightIconDrawable);
    }

    private void setEvents() {
//        clKdlTextSelectionRoot.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openPopupWindow().show();
//            }
//        });

        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (getHeight() > 0) {
                    setPadding(!TextUtils.isEmpty(tvKdlTextSelectionSelect.getText()));
                    tvCustomHint.setScaleX(!TextUtils.isEmpty(tvKdlTextSelectionSelect.getText()) ? HINT_SHRINK_SCALE:1);
                    tvCustomHint.setScaleY(!TextUtils.isEmpty(tvKdlTextSelectionSelect.getText()) ? HINT_SHRINK_SCALE:1);
                    tvCustomHint.setTranslationX(!TextUtils.isEmpty(tvKdlTextSelectionSelect.getText())?  getHintLateralTranslation(): 0f);
                    tvCustomHint.setTranslationY(!TextUtils.isEmpty(tvKdlTextSelectionSelect.getText())? getHintLongitudinalTranslation(): 0f);
                    if (!isEnabled()) {
                        tvKdlTextSelectionSelect.setTextColor(ContextCompat.getColor(getContext(), R.color.text_lightgrey));
                        tvCustomHint.setTextColor(ContextCompat.getColor(getContext(), !TextUtils.isEmpty(tvKdlTextSelectionSelect.getText())? R.color.text_midgrey: R.color.text_lightgrey));
                    } else {
                        tvKdlTextSelectionSelect.setTextColor(ContextCompat.getColor(getContext(), R.color.text_black));
                        tvCustomHint.setTextColor(ContextCompat.getColor(getContext(), !TextUtils.isEmpty(tvKdlTextSelectionSelect.getText()) ? R.color.text_lightgrey: R.color.text_midgrey));

                    }
                    initialUpdateFinished = true;
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        clKdlTextSelectionRoot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null )
                    listener.onClickListener();
            }
        });

        tvKdlTextSelectionSelect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (initialUpdateFinished) {
                    setPadding(!TextUtils.isEmpty(text));
                    animateColorHint(TextUtils.isEmpty(text));
                    animateScaleHint(TextUtils.isEmpty(text));
                }
            }
        });

    }

//    private ListPopupWindow openPopupWindow() {
//
//        // initialize a pop up window type
//        ListPopupWindow popupWindow = new ListPopupWindow(getContext());
//        popupWindow.setAdapter(this.adapter);
//
//        // set the item click listener
//        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                tvKdlTextSelectionSelect.setText(adapter.getItem(position).toString());
//                animateScaleHint(false);
//                setPadding(true);
//                popupWindow.dismiss();
//            }
//        });
//
//        // some other visual settings
//        popupWindow.setWidth(getResources().getDimensionPixelSize(R.dimen.size_230_dp));
//        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//
//        popupWindow.setAnchorView(this);
//        popupWindow.setModal(true);
//        popupWindow.setPromptPosition(POSITION_PROMPT_ABOVE);
//        popupWindow.setDropDownGravity(Gravity.END);
//        popupWindow.setVerticalOffset(-getHeight());
//
//
//        // set the list view as pop up window content
//
//        return popupWindow;
//    }


    private void initComponents() {
        tvKdlTextSelectionSelect = findViewById(R.id.tvKdlTextSelectionSelect);
        tvCustomHint = findViewById(R.id.tvKdlTextSelectionHint);
        clKdlTextSelectionRoot= findViewById(R.id.clKdlTextSelectionRoot);
        ivLeftIcon = findViewById(R.id.ivIconLeft);
        ivRightIcon = findViewById(R.id.ivIconRight);
    }

    private void setupView(String hintText, boolean enabled, int leftIconVisibility, int rightIconVisibility, int leftIconDrawable, int rightIconDrawable) {
        this.setHint(hintText);
        this.setEnabled(enabled);
        this.setLeftIconDrawable(leftIconDrawable);
        this.setLeftIconVisibility(leftIconVisibility);
        this.setRightIconDrawable(rightIconDrawable);
        this.setRightIconVisibility(rightIconVisibility);
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

    private void setPadding(boolean isFocused) {
        int paddingBottom = getResources().getDimensionPixelSize(isFocused ? R.dimen.size_10_dp: R.dimen.size_20_dp);
        int paddingTop =  getResources().getDimensionPixelSize(isFocused ? R.dimen.size_30_dp: R.dimen.size_20_dp);
        tvKdlTextSelectionSelect.setPadding(tvKdlTextSelectionSelect.getPaddingLeft(), paddingTop, tvKdlTextSelectionSelect.getPaddingRight(), paddingBottom);
    }

    private float getHintLateralTranslation() {
        float width = tvCustomHint.getWidth();
        return -((width - HINT_SHRINK_SCALE * width) * HALF);
    }

    private float getHintLongitudinalTranslation() {
        int paddingTop = getResources().getDimensionPixelSize(R.dimen.size_8_dp);
        float editTextHeight = tvKdlTextSelectionSelect.getHeight();
        float hintHeight = tvCustomHint.getHeight();
        return -((editTextHeight - hintHeight) * HALF - paddingTop);
    }


    public void setHint(String hint) {
        tvCustomHint.setText(hint);
    }

//    public void setAdapter(ArrayAdapter adapter) {
//        this.adapter = adapter;
//    }

    public void setOnClickListener(CustomTextListener listener) {
        this.listener = listener;
    }

    public void setText(String text) {
        tvKdlTextSelectionSelect.setText(text);
    }

    public void setEnabled(boolean enabled) {
        ViewUtils.recursiveSetEnabled(this, enabled);
        super.setEnabled(enabled);
    }

    public void setLeftIconVisibility(int visibility) {
        ivLeftIcon.setVisibility(visibility);
    }

    public void setRightIconVisibility(int visibility) {
        ivRightIcon.setVisibility(visibility);
    }

    public void setLeftIconDrawable(int drawable) {
        ivLeftIcon.setImageDrawable(ContextCompat.getDrawable(this.getContext(), drawable));
    }

    public void setRightIconDrawable(int drawable) {
        ivRightIcon.setImageDrawable(ContextCompat.getDrawable(this.getContext(), drawable));
    }


    public interface CustomTextListener {
        void onClickListener();
    }
}
