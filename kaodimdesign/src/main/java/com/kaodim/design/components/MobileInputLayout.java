package com.kaodim.design.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaodim.design.R;
import com.kaodim.design.components.adapters.CountryCodeRVAdapter;
import com.kaodim.design.components.models.CountryCodeRowItem;
import com.lamudi.phonefield.PhoneEditText;

import java.util.ArrayList;

public class MobileInputLayout extends RelativeLayout {

    TextView tvHintTitle, tvCode;
    RelativeLayout rlParent;
    LinearLayout llCountryCodeSelector;
    RecyclerView rvCountryCodeSelector;
    EditText etInput;
    ImageView ivFlag;
    PhoneEditText etValidator;

    CountryCodeRVAdapter adapter;
    ArrayList<CountryCodeRowItem> countryCodes = new ArrayList<>();
    String hint = "";
    String hintTitle = "";
    String countryCode = "";
    Context context;
    boolean countrySelectedDisplayed = false;
    String validationCountry = "";

    String mobileNumber = "";

    private MobileInputEventListener listener;

    public interface MobileInputEventListener {
        void onCountrySelected(CountryCodeRowItem item);

        void onMobileValueChanged(String countryCode, String value, boolean isValidNumber);
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
        rlParent = findViewById(R.id.rlParent);
        llCountryCodeSelector = findViewById(R.id.llCountryCodeSelector);
        rvCountryCodeSelector = findViewById(R.id.rvCountryCodeSelector);
        etInput = findViewById(R.id.etInput);
        etValidator = findViewById(R.id.etValidator);
        tvCode = findViewById(R.id.tvCode);
        ivFlag = findViewById(R.id.ivFlag);

        setHintText(hint);
        setHintTitle(hintTitle);

        setClickEvents();
    }

    /**
     * Initialize the component. Without initializing, the country will not work.
     *
     * @param context
     * @param mobileNumber
     */
    public void initialize(Context context, ArrayList<CountryCodeRowItem> countryCodes, String validationCountry, String mobileNumber) {
        this.context = context;
        this.countryCodes = countryCodes;
        this.mobileNumber = mobileNumber;

        setValidationCountry(validationCountry);
        setupRecyclerView(context);
        setText(mobileNumber);
    }

    public void setMobileInputEventListener(MobileInputEventListener listener) {
        this.listener = listener;
    }

    private void setupRecyclerView(Context context) {
        adapter = new CountryCodeRVAdapter(context, countryCodes, new CountryCodeRVAdapter.CountryCodeSelectionListener() {
            @Override
            public void onSelected(CountryCodeRowItem countryItem) {
                if (countrySelectedDisplayed) {
                    countrySelectedDisplayed = false;
                    hideCountrySelector();
                } else {
                    countrySelectedDisplayed = true;
                    displayCountrySelector();
                }

                tvCode.setText(countryItem.code);
                ivFlag.setImageResource(countryItem.flagIconResource);
                countryCode = countryItem.code.replace("(", "").replace(")", "");

                if(listener != null) {
                    listener.onMobileValueChanged(countryCode, etInput.getText().toString(), checkValidity());
                    listener.onCountrySelected(countryItem);
                }
            }
        });
        rvCountryCodeSelector.setLayoutManager(new LinearLayoutManager(context));
        rvCountryCodeSelector.setAdapter(adapter);
        rvCountryCodeSelector.setVisibility(View.GONE);
    }

    private void displayCountrySelector() {
        rvCountryCodeSelector.setVisibility(View.VISIBLE);
    }

    private void hideCountrySelector() {
        rvCountryCodeSelector.setVisibility(View.GONE);
    }

    private void setClickEvents() {

        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etValidator.setPhoneNumber(etInput.getText().toString());
                if(listener != null) {
                    listener.onMobileValueChanged(countryCode, etInput.getText().toString(), checkValidity());
                }
            }
        });

        llCountryCodeSelector.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countrySelectedDisplayed) {
                    countrySelectedDisplayed = false;
                    rvCountryCodeSelector.setVisibility(View.GONE);
                } else {
                    countrySelectedDisplayed = true;
                    rvCountryCodeSelector.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void setHintText(String hint) {
        this.hint = hint;
        etInput.setHint(hint);
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
        etValidator.setPhoneNumber(text);
        if(listener != null) {
            listener.onMobileValueChanged(countryCode, text, checkValidity());
        }
    }

    public void setValidationCountry(String country) {
        this.validationCountry = country;
        etValidator.setDefaultCountry(validationCountry);
    }

    public boolean checkValidity() {
        return etValidator.isValid();
    }

}
