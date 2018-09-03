package com.kaodim.design.components.notes;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaodim.design.R;

public class NotesStandard extends LinearLayout {

    TextView tvDescription, tvSecondaryDescription;
    Button btnSingle, btnDoublePrimary, btnDoubleSecondary;
    private int notesType;
    String description,secondaryDescription, btnSingleText, btnDoublePrimaryText,btnDoubleSecondaryText;
    boolean showSecondaryText = false;
    LinearLayout llSingleBtn, llDoubleBtn;
    ImageView ivIcon;

    public interface NOTES_TYPES {
        String NO_BUTTON = "no_button";
        String SINGLE_BUTTON = "single_button";
        String DOUBLE_BUTTON = "double_button";
        String NO_ICON = "no_icon";
        String ONLY_PRIMARY_TEXT = "only_primary_text";
    }

    public static final int TYPE_NO_BUTTON = 1;
    public static final int TYPE_SINGLE_BUTTON = 2;
    public static final int TYPE_DOUBLE_BUTTON = 3;
    public static final int TYPE_NO_ICON = 4;
    public static final int TYPE_ONLY_PRIMARY_TEXT  = 5;
    public static final int TYPE_ALL  = 6;

    public interface NotesStandardListener {
        void onButtonSingleClicked();
        void onButtonDoublePrimaryClicked();
        void onButtonDoubleSecondaryClicked();
    }

    NotesStandardListener listener;


    public NotesStandard(Context context) {
        super(context);
        init(context,null);
    }

    public NotesStandard(Context context, AttributeSet set) {
        super(context, set);
        init(context,set);
    }

    public NotesStandard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet atts) {
        //Retrieve the custom attributes from XML
        TypedArray typedArray = context.obtainStyledAttributes(atts, R.styleable.NotesStandard);
        description = typedArray.getString(R.styleable.NotesStandard_notesText);
        secondaryDescription = typedArray.getString(R.styleable.NotesStandard_notesSecondary);
        notesType = typedArray.getInt(R.styleable.NotesStandard_notesType,TYPE_ALL);
        btnSingleText = typedArray.getString(R.styleable.NotesStandard_btnSingleText);
        btnDoublePrimaryText = typedArray.getString(R.styleable.NotesStandard_btnDoublePrimaryText);
        btnDoubleSecondaryText = typedArray.getString(R.styleable.NotesStandard_btnDoubleSecondaryText);
        showSecondaryText = typedArray.getBoolean(R.styleable.NotesStandard_showNotesSecondary,false);
        //Recycle the TypedArray (saves memory)
        typedArray.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.kdl_notes_standard, this);
        initComponents();
    }

    private void initComponents() {
        tvDescription = findViewById(R.id.tvDescription);
        tvSecondaryDescription = findViewById(R.id.tvSecondaryText);
        btnSingle = findViewById(R.id.btnSingle);
        btnDoublePrimary = findViewById(R.id.btnDoublePrimary);
        btnDoubleSecondary = findViewById(R.id.btnDoubleSecondary);
        llSingleBtn = findViewById(R.id.llSinglebutton);
        llDoubleBtn = findViewById(R.id.llDoublebutton);
        ivIcon = findViewById(R.id.ivIcon);

        tvDescription.setText(description);
        tvDescription.setVisibility(VISIBLE);
        tvSecondaryDescription.setText(secondaryDescription);
        btnSingle.setText(btnSingleText);
        btnDoublePrimary.setText(btnDoublePrimaryText);
        btnDoubleSecondary.setText(btnDoubleSecondaryText);

        setEvents();
        setNotesType(notesType);
        setSecondaryVisibility(secondaryDescription);
    }

    public void setNotesType(int type){
        this.notesType = type;

        switch (notesType){
            case TYPE_NO_BUTTON:
                ivIcon.setVisibility(View.VISIBLE);
                llSingleBtn.setVisibility(View.GONE);
                llDoubleBtn.setVisibility(View.GONE);
                setSecondaryVisibility(secondaryDescription);
                tvDescription.setVisibility(VISIBLE);
                break;
            case TYPE_SINGLE_BUTTON:
                ivIcon.setVisibility(View.VISIBLE);
                llSingleBtn.setVisibility(View.VISIBLE);
                llDoubleBtn.setVisibility(View.GONE);
                setSecondaryVisibility(secondaryDescription);
                tvDescription.setVisibility(VISIBLE);
                break;
            case TYPE_DOUBLE_BUTTON:
                ivIcon.setVisibility(View.VISIBLE);
                llSingleBtn.setVisibility(View.GONE);
                llDoubleBtn.setVisibility(View.VISIBLE);
                setSecondaryVisibility(secondaryDescription);
                tvDescription.setVisibility(VISIBLE);
                break;
            case TYPE_NO_ICON:
                ivIcon.setVisibility(View.GONE);
                llSingleBtn.setVisibility(View.VISIBLE);
                llDoubleBtn.setVisibility(View.VISIBLE);
                setSecondaryVisibility(secondaryDescription);
                tvDescription.setVisibility(VISIBLE);
                break;
            case TYPE_ONLY_PRIMARY_TEXT:
                ivIcon.setVisibility(View.GONE);
                llSingleBtn.setVisibility(View.GONE);
                llDoubleBtn.setVisibility(View.GONE);
                setSecondaryVisibility(secondaryDescription);
                tvDescription.setVisibility(VISIBLE);
                break;

            default:
                ivIcon.setVisibility(View.VISIBLE);
                llSingleBtn.setVisibility(View.VISIBLE);
                llDoubleBtn.setVisibility(View.VISIBLE);
                setSecondaryVisibility(secondaryDescription);
                tvDescription.setVisibility(VISIBLE);
                break;



        }
    }

    public void setSecondaryVisibility(String secondaryDescription){
        if(secondaryDescription == null || secondaryDescription.isEmpty()){
            tvSecondaryDescription.setVisibility(View.GONE);
        }else{
            tvSecondaryDescription.setVisibility(View.VISIBLE);
        }
    }

    public void setTvDescriptionVisibility(int visibility){
        tvDescription.setVisibility(visibility);
    }

    public void setTvSecondaryDescriptionVisibility(int visibility){
        tvSecondaryDescription.setVisibility(visibility);
    }

    public void setDescriptionText(String description) {
        tvDescription.setText(description);
    }

    public void setSecondaryDescription(String description){
        tvSecondaryDescription.setText(description);
        setSecondaryVisibility(description);
    }

    public void setButtonOnClickListener(NotesStandardListener notesStandardListener) {
        this.listener = notesStandardListener;
    }

    public void setButtonSingleText(String text){
        btnSingle.setText(text);
    }

    public void setButtonDoublePrimaryText(String text){
        btnDoublePrimary.setText(text);
    }

    public void setButtonDoubleSecondaryText(String text){
        btnDoubleSecondary.setText(text);
    }

    public void setEnableButtonSingle(boolean enable){
        btnSingle.setEnabled(enable);
    }

    public void setEnableButtonDoublePrimary(boolean enable){
        btnDoublePrimary.setEnabled(enable);
    }

    public void setEnableButtonDoubleSecondary(boolean enable){
        btnDoubleSecondary.setEnabled(enable);
    }

    private void setEvents() {
        btnSingle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onButtonSingleClicked();
                }
            }
        });

        btnDoublePrimary.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onButtonDoublePrimaryClicked();
                }
            }
        });

        btnDoubleSecondary.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onButtonDoubleSecondaryClicked();
                }
            }
        });
    }

}
