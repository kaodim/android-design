package com.kaodim.design.components.notes;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaodim.design.R;

public class NotesInfo extends LinearLayout {

    TextView tvDescription, tvTitle;
    String description, title;
    int notesType,iconWidth,iconHeight,iconResourceId;
    ImageView ivIcon;

    public static final int TYPE_WITH_ICON_TITLE = 1;
    public static final int TYPE_WITH_ICON = 2;
    public static final int TYPE_TITLE_AND_TEXT = 3;
    public static final int TYPE_TEXT_ONLY = 4;

    public NotesInfo(Context context) {
        super(context);
        init(context,null);
    }

    public NotesInfo(Context context, AttributeSet set) {
        super(context, set);
        init(context,set);
    }

    public NotesInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet atts) {
        //Retrieve the custom attributes from XML
        TypedArray typedArray = context.obtainStyledAttributes(atts, R.styleable.NotesInfo);
        description = typedArray.getString(R.styleable.NotesInfo_notesInfoText);
        title = typedArray.getString(R.styleable.NotesInfo_notesInfoTitle);
        notesType = typedArray.getInt(R.styleable.NotesInfo_notesInfoType,TYPE_WITH_ICON_TITLE);
        iconWidth = typedArray.getLayoutDimension(R.styleable.NotesInfo_notesInfoIconWidth,13);
        iconHeight = typedArray.getLayoutDimension(R.styleable.NotesInfo_notesInfoIconHeight,18);
        iconResourceId = typedArray.getResourceId(R.styleable.NotesInfo_notesInfoIconDrawable,R.drawable.ic_bulb);
        //Recycle the TypedArray (saves memory)
        typedArray.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.kdl_notes_full_width_info, this);
        initComponents();
    }

    private void initComponents() {
        tvDescription = findViewById(R.id.tvDescription);
        tvTitle = findViewById(R.id.tvTitle);
        ivIcon = findViewById(R.id.ivIcon);


        tvDescription.setText(description);
        tvTitle.setText(title);
        setNotesType(notesType);
        setIcon(iconResourceId);
        setIconSize(iconWidth,iconHeight);
    }

    public void setNotesType(int type){
        this.notesType = type;

        switch (notesType){
            case TYPE_WITH_ICON_TITLE:
                ivIcon.setVisibility(View.VISIBLE);
                tvDescription.setVisibility(View.VISIBLE);
                tvTitle.setVisibility(View.VISIBLE);
                break;
            case TYPE_WITH_ICON:
                ivIcon.setVisibility(View.VISIBLE);
                tvDescription.setVisibility(View.VISIBLE);
                tvTitle.setVisibility(View.GONE);
                break;
            case TYPE_TITLE_AND_TEXT:
                ivIcon.setVisibility(View.GONE);
                tvDescription.setVisibility(View.VISIBLE);
                tvTitle.setVisibility(View.VISIBLE);
                break;
            case TYPE_TEXT_ONLY:
                ivIcon.setVisibility(View.GONE);
                tvDescription.setVisibility(View.VISIBLE);
                tvTitle.setVisibility(View.GONE);
                break;
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
        layoutParams.setMargins(0,10,26,0);
        ivIcon.setLayoutParams(layoutParams);
    }

    public void setIconSize( LayoutParams layoutParams){
        ivIcon.setLayoutParams(layoutParams);
    }

    public void setNotesInfoTitleText(String title){
        tvTitle.setText(title);
    }

    public void setNotesInfoTitleText(CharSequence title){
        tvTitle.setText(title);
    }


    public void setNotesInfoDescriptionText(String description){
        tvDescription.setText(description);
    }

    public void setNotesInfoDescriptionText(CharSequence description){
        tvDescription.setText(description);
    }

    public void setTitleVisibility(int visibility){
        tvTitle.setVisibility(visibility);
    }

    public void setDescriptionVisibility(int  visibility){
        tvDescription.setVisibility(visibility);
    }

    public void setIconVisibility(int  visibility){
        ivIcon.setVisibility(visibility);
    }
}
