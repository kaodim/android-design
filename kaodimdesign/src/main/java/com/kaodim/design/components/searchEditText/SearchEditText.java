package com.kaodim.design.components.searchEditText;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaodim.design.R;

import java.util.Timer;
import java.util.TimerTask;

public class SearchEditText extends LinearLayout {

    private final long delay = 800; // milliseconds
    private boolean showCancelText =  true;

    private AppCompatEditText etSearch;
    private ImageView ivTextClear;
    private TextView tvSearchCancel;
    private ImageView ivMiniSearch;
    private int iconDrawableId;


    private SearchEditTextDefaultListener listener;

    //define callback interface
    public interface SearchEditTextChangeListener {
        void onTextChanged(String res);
    }

    public interface SearchEditTextDefaultListener {
        void onClearButtonClicked();
        void onCancelButtonClicked();
        void onFocusChangeListener(boolean hasFocus);
    }


    public SearchEditText(Context context) {
        super(context);
        this.initComponents();
        this.setEvents();
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //Retrieve the custom attributes from XML
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchEditText);
        int inputType = typedArray.getInt(R.styleable.SearchEditText_android_inputType, EditorInfo.TYPE_NULL);
        String hintText = typedArray.getString(R.styleable.SearchEditText_android_hint);
        String cancelText = typedArray.getString(R.styleable.SearchEditText_cancelText);
        showCancelText = typedArray.getBoolean(R.styleable.SearchEditText_showCancelText, true);
        iconDrawableId = typedArray.getResourceId(R.styleable.SearchEditText_leftIconDrawable, R.drawable.icon_mini_search);
        //Recycle the TypedArray (saves memory)
        typedArray.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.kdl_item_search_box, this);
        initComponents();
        this.setupView(hintText, cancelText, inputType);
        this.setEvents();
    }

    private void initComponents() {
        etSearch = findViewById(R.id.etSearch);
        ivTextClear = findViewById(R.id.ivTextClear);
        tvSearchCancel = findViewById(R.id.tvSearchCancel);
        ivMiniSearch = findViewById(R.id.ivMiniSearch);
    }

    private void setupView(String hintText, String cancelText, int inputType) {
        this.setHint(hintText);
        if (!TextUtils.isEmpty(cancelText))
            this.setCancelText(cancelText);
        etSearch.setShowSoftInputOnFocus(true);
        this.setInputType(inputType);
        this.setIconDrawable(ContextCompat.getDrawable(getContext(), iconDrawableId));
    }

    public void setInputType(int inputType) {
        if (etSearch != null) {
            etSearch.setInputType(inputType);
        }
    }

    private void setEvents() {
        etSearch.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, final boolean hasFocus) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        try {
                            if (hasFocus) {
                                inputMethodManager.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
                            } else {
                                inputMethodManager.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                            }
                        } catch (NullPointerException ignored) { }
                    }
                });

                tvSearchCancel.setVisibility(hasFocus && showCancelText ? View.VISIBLE: View.GONE);
                if (listener != null){
                    listener.onFocusChangeListener(hasFocus);
                }
            }
        });

        etSearch.addTextChangedListener(initTextWatcher(new SearchEditTextChangeListener() {
            @Override
            public void onTextChanged(String res) {
                ivTextClear.setVisibility(!TextUtils.isEmpty(res) ? View.VISIBLE: View.GONE);
            }
        }));

        ivTextClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setText("");
                if (listener != null){
                    listener.onClearButtonClicked();
                }
            }
        });

        tvSearchCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setText("");
                if (listener != null){
                    listener.onCancelButtonClicked();
                }
                etSearch.clearFocus();
            }
        });
    }

    public void addTextChangedListenerWithDelay(final Activity activity, final SearchEditTextChangeListener listener) {
        etSearch.addTextChangedListener(initTextWatcher(new SearchEditTextChangeListener() {
            Timer timer = new Timer();
            boolean isTyping = false;

            @Override
            public void onTextChanged(final String res) {
                if (!isTyping) {
                    isTyping = true;
                }
                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        isTyping = false;
                                        if (listener != null) {
                                            listener.onTextChanged(res);
                                        }
                                    }
                                });
                            }
                        }, delay
                );
            }
        }));
    }

    public void addTextChangedListener(final SearchEditTextChangeListener listener) {
        etSearch.addTextChangedListener(initTextWatcher(new SearchEditTextChangeListener() {
            @Override
            public void onTextChanged(final String res) {
                if (listener != null) {
                    listener.onTextChanged(res);
                }
            }
        }));
    }

    private TextWatcher initTextWatcher(final SearchEditTextChangeListener listener) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if (listener != null) {
                    listener.onTextChanged(editable.toString());
                }
            }
        };
    }

    public void setHint(String hint) {
        etSearch.setHint(hint);
    }

    public String getHint() {
        return etSearch.getHint().toString();
    }

    public void setText(String text) {
        etSearch.setText(text);
    }

    public String getText() {
        return etSearch.getText().toString();
    }

    public void setCancelText(String cancelText) {
        tvSearchCancel.setText(cancelText);
    }

    public String getCancelText() {
        return tvSearchCancel.getText().toString();
    }

    public void setListener(SearchEditTextDefaultListener listener) {
        this.listener = listener;
    }

    public void setIconDrawable(Drawable drawable) {
        ivMiniSearch.setImageDrawable(drawable);
    }

    public Drawable getIconDrawable() {
        return ivMiniSearch.getDrawable();
    }

    public AppCompatEditText getEditText() { return etSearch; }
}
