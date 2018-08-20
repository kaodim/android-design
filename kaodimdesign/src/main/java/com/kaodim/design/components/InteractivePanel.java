package com.kaodim.design.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaodim.design.R;
import com.kaodim.design.components.dialogs.DateTimePickerDialog;

public class InteractivePanel extends RelativeLayout {

    RelativeLayout rlParent;
    TextView tvHint;

    String hint = "";

    public interface InteractivePanelListener {
        void onPanelInteracted();
    }

    InteractivePanelListener listener;

    public InteractivePanel(Context context) {
        super(context);
        init(context);
    }

    public InteractivePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InteractivePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //Retrieve the custom attributes from XML
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.SearchBox);

        DateTimePickerDialog.DateTimePickerOptions options = new DateTimePickerDialog.DateTimePickerOptions();
        hint = typedArray.getString(R.styleable.InteractivePanel_panelHint);
        //Recycle the TypedArray (saves memory)
        typedArray.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.kdl_element_interactive_panel_layout, this);
        initComponents();
    }

    private void initComponents() {
        rlParent = findViewById(R.id.rlParent);
        tvHint = findViewById(R.id.tvHint);

        tvHint.setText(hint);

        setEvents();
    }

    public void setHint(String hint) {
        this.hint = hint;
        tvHint.setText(hint);
    }

    public void setPanelInteractedListener(InteractivePanelListener listener) {
        this.listener = listener;
    }

    private void setEvents() {
        rlParent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPanelInteracted();
                }
            }
        });
    }

}
