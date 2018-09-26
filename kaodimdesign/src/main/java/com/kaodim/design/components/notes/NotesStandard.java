package com.kaodim.design.components.notes;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
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
    private int notesType,iconWidth,iconHeight,iconResourceId;
    String description,secondaryDescription, btnSingleText, btnDoublePrimaryText,btnDoubleSecondaryText;
    boolean showSecondaryText = false;
    LinearLayout llSingleBtn, llDoubleBtn,llIcon;
    ImageView ivIcon;


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
        iconWidth = typedArray.getLayoutDimension(R.styleable.NotesStandard_notesIconWidth,21);
        iconHeight = typedArray.getLayoutDimension(R.styleable.NotesStandard_notesIconHeight,50);
        iconResourceId = typedArray.getResourceId(R.styleable.NotesStandard_notesIconDrawable,R.drawable.ic_cash);
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
        llIcon = findViewById(R.id.llIcon);

        tvDescription.setText(description);
        tvDescription.setVisibility(VISIBLE);
        tvSecondaryDescription.setText(secondaryDescription);
        btnSingle.setText(btnSingleText);
        btnDoublePrimary.setText(btnDoublePrimaryText);
        btnDoubleSecondary.setText(btnDoubleSecondaryText);

        setEvents();
        setNotesType(notesType);
        setSecondaryVisibility(secondaryDescription);
        setIcon(iconResourceId);
        setIconXMLSize(iconWidth,iconHeight);
    }

    public void setNotesType(int type){
        this.notesType = type;

        switch (notesType){
            case TYPE_NO_BUTTON:
                ivIcon.setVisibility(View.VISIBLE);
                llIcon.setVisibility(View.VISIBLE);
                llSingleBtn.setVisibility(View.GONE);
                llDoubleBtn.setVisibility(View.GONE);
                setSecondaryVisibility(secondaryDescription);
                tvDescription.setVisibility(VISIBLE);
                break;
            case TYPE_SINGLE_BUTTON:
                ivIcon.setVisibility(View.VISIBLE);
                llIcon.setVisibility(View.VISIBLE);
                llSingleBtn.setVisibility(View.VISIBLE);
                llDoubleBtn.setVisibility(View.GONE);
                setSecondaryVisibility(secondaryDescription);
                tvDescription.setVisibility(VISIBLE);
                break;
            case TYPE_DOUBLE_BUTTON:
                ivIcon.setVisibility(View.VISIBLE);
                llIcon.setVisibility(View.VISIBLE);
                llSingleBtn.setVisibility(View.GONE);
                llDoubleBtn.setVisibility(View.VISIBLE);
                setSecondaryVisibility(secondaryDescription);
                tvDescription.setVisibility(VISIBLE);
                break;
            case TYPE_NO_ICON:
                ivIcon.setVisibility(View.GONE);
                llIcon.setVisibility(View.GONE);
                llSingleBtn.setVisibility(View.VISIBLE);
                llDoubleBtn.setVisibility(View.VISIBLE);
                setSecondaryVisibility(secondaryDescription);
                tvDescription.setVisibility(VISIBLE);
                break;
            case TYPE_ONLY_PRIMARY_TEXT:
                ivIcon.setVisibility(View.GONE);
                llIcon.setVisibility(View.GONE);
                llSingleBtn.setVisibility(View.GONE);
                llDoubleBtn.setVisibility(View.GONE);
                setSecondaryVisibility(secondaryDescription);
                tvDescription.setVisibility(VISIBLE);
                break;

            default:
                ivIcon.setVisibility(View.VISIBLE);
                llIcon.setVisibility(View.VISIBLE);
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

    public void setSecondaryVisibility(CharSequence secondaryDescription){
        if(secondaryDescription == null || secondaryDescription.length() == 0){
            tvSecondaryDescription.setVisibility(View.GONE);
        }else{
            tvSecondaryDescription.setVisibility(View.VISIBLE);
        }
    }

    public void setIcon(int resourceID){
        ivIcon.setVisibility(View.VISIBLE);
        ivIcon.setImageResource(resourceID);
    }

    public void setIconSize(int width, int height){
        int width_dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
        int height_dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
        LayoutParams layoutParams = new LayoutParams(width_dp,height_dp);
        layoutParams.setMargins(0,8,15,0);
        ivIcon.setLayoutParams(layoutParams);
    }

    private void setIconXMLSize(int width, int height){
        LayoutParams layoutParams = new LayoutParams(width,height);
        layoutParams.setMargins(0,8,15,0);
        ivIcon.setLayoutParams(layoutParams);
    }


    public void setIconSize( LayoutParams layoutParams){
        ivIcon.setLayoutParams(layoutParams);
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

    public void setDescriptionText(CharSequence description) {
        tvDescription.setText(description);
    }

    public void setSecondaryDescription(String description){
        tvSecondaryDescription.setText(description);
        setSecondaryVisibility(description);
    }

    public void setSecondaryDescription(CharSequence description){
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
