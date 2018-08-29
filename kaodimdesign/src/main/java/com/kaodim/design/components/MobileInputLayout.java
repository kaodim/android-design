package com.kaodim.design.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaodim.design.R;
import com.kaodim.design.components.adapters.CountryCodeRVAdapter;
import com.kaodim.design.components.models.CountryCodeRowItem;

import java.util.ArrayList;

public class MobileInputLayout extends RelativeLayout {

    TextView tvHintTitle, tvHint;
    RelativeLayout rlParent;
    LinearLayout llCountryCodeSelector;
    RecyclerView rvCountryCodeSelector;
    EditText etInput;

    CountryCodeRVAdapter adapter;
    ArrayList<CountryCodeRowItem> countryCodes = new ArrayList<>();
    boolean isViewingCountries = false;
    String hint = "";
    String hintTitle = "";
    Context context;

    private MobileInputEventListener listener;

    public interface MobileInputEventListener {
        void onCountrySelected(CountryCodeRowItem item);

        void onMobileLayoutClicked();
    }

    public MobileInputLayout(Context context) {
        super(context);
        initComponents();
    }

    public MobileInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MobileInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public MobileInputLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //Retrieve the custom attributes from XML
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MobileInputMockLayout);
        hint = typedArray.getString(R.styleable.MobileInputLayout_inputHint);
        hintTitle = typedArray.getString(R.styleable.MobileInputLayout_inputHintTitle);
        //Recycle the TypedArray (saves memory)
        typedArray.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.kdl_element_mobile_input_layout, this);
        initComponents();
    }

    private void initComponents() {
        tvHintTitle = findViewById(R.id.tvHintTitle);
        tvHint = findViewById(R.id.tvHint);
        rlParent = findViewById(R.id.rlParent);
        llCountryCodeSelector = findViewById(R.id.llCountryCodeSelector);
        rvCountryCodeSelector = findViewById(R.id.rvCountryCodeSelector);
        etInput = findViewById(R.id.etInput);

        setHintText(hint);
        setHintTitle(hintTitle);

        setClickEvents();
    }

    /**
     * Initialize the component. Without initializing, the country will not work.
     *
     * @param context
     */
    public void initialize(Context context) {
        this.context = context;

        setupRecyclerView(context);
    }

    public void setMobileInputEventListener(MobileInputEventListener listener) {
        this.listener = listener;
    }

    private void setupRecyclerView(Context context) {
        adapter = new CountryCodeRVAdapter(context, countryCodes, new CountryCodeRVAdapter.CountryCodeSelectionListener() {
            @Override
            public void onSelected(CountryCodeRowItem countryItem) {
                listener.onCountrySelected(countryItem);
            }
        });
        rvCountryCodeSelector.setLayoutManager(new LinearLayoutManager(context));
        rvCountryCodeSelector.setAdapter(adapter);
        rvCountryCodeSelector.setVisibility(View.GONE);
    }

    private void setClickEvents() {
        rlParent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onMobileLayoutClicked();
            }
        });

        llCountryCodeSelector.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isViewingCountries) {
                    rvCountryCodeSelector.setVisibility(View.GONE);
                    isViewingCountries = false;
                } else {
                    rvCountryCodeSelector.setVisibility(View.VISIBLE);
                    isViewingCountries = true;
                }
            }
        });
    }

    public void setHintText(String hint) {
        this.hint = hint;
        tvHint.setText(hint);
    }

    public void setHintTitle(String hintTitle) {
        this.hintTitle = hintTitle;
        tvHintTitle.setText(hintTitle);
    }

    public String getText() {
        return etInput.getText().toString();
    }

    public void setText(String text) {
        etInput.setText(text);
    }

}
