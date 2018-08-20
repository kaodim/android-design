package com.kaodim.design.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaodim.design.R;

public class SearchBox extends RelativeLayout {

    private boolean displayNext = false;
    private String inputType = "text";
    private String hint = "Sample hint text";

    private SearchBoxClickListener listener;

    TextView tvSearchBoxHint;
    RelativeLayout rlNext, rlSearchBox;

    public interface SearchBoxClickListener {
        void onNextClicked();

        void onSearchBoxClicked();
    }

    public SearchBox(Context context) {
        super(context);
        init(context);
    }

    public SearchBox(Context context, AttributeSet set) {
        super(context, set);
        init(context);
    }

    public SearchBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //Retrieve the custom attributes from XML
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.SearchBox);
        displayNext = typedArray.getBoolean(R.styleable.SearchBox_nextButton, false);
        hint = typedArray.getString(R.styleable.SearchBox_hint);
        //Recycle the TypedArray (saves memory)
        typedArray.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.kdl_element_searchbox_layout, this);
        initComponents();
    }

    private void initComponents() {
        tvSearchBoxHint = findViewById(R.id.tvSearchBoxHint);
        rlNext = findViewById(R.id.rlNext);
        rlSearchBox = findViewById(R.id.rlSearchBox);

        rlNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onNextClicked();
                }
            }
        });

        rlSearchBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSearchBoxClicked();
                }
            }
        });
    }

    public void setListener(SearchBoxClickListener listener) {
        this.listener = listener;
    }

    public void setHint(String hint) {
        this.hint = hint;
        tvSearchBoxHint.setText(hint);
    }

    public void setDisplayNext(boolean displayNext) {
        this.displayNext = displayNext;
        rlNext.setVisibility((displayNext ? VISIBLE : GONE));
    }

}
