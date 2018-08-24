package com.kaodim.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
//import android.widget.RelativeLayout;
//
//import com.kaodim.design.components.DateTimePicker;
//import com.kaodim.design.components.InteractivePanel;
//import com.kaodim.design.components.NumericControl;
//import com.kaodim.design.components.callbacks.NumericControlListener;

public class MainActivity extends AppCompatActivity {

//    NumericControl numericControl;
//    RelativeLayout rlDateSelection;
//    DateTimePicker dateTimePicker;
//    InteractivePanel interactivePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        numericControl = findViewById(R.id.numericControl);
//        dateTimePicker = findViewById(R.id.dateTimePicker);
//        interactivePanel = findViewById(R.id.interactivePanel);

        setupNumericControl();

        setupDateTimePicker();

        setupInteractivePanel();
    }

    private void setupInteractivePanel() {
//        interactivePanel.setPanelInteractedListener(new InteractivePanel.InteractivePanelListener() {
//            @Override
//            public void onPanelInteracted() {
//
//            }
//        });
//
//        interactivePanel.setBorderType(InteractivePanel.BORDER_TYPES.LINE);
    }

    private void setupNumericControl() {
//        numericControl.setListener(new NumericControlListener() {
//            @Override
//            public void onNumericAdded(int value) {
//
//            }
//
//            @Override
//            public void onNumericRemoved(int value) {
//
//            }
//
//            @Override
//            public void onNumericValueChanged(int value) {
//
//            }
//        });
    }

    private void setupDateTimePicker() {
//        dateTimePicker.initialize(this);
//        dateTimePicker.setRangeStartTime("2018-01-1T7:30:30.042+08:00");
//        dateTimePicker.setRangeEndTime("2018-10-20T22:00:30.042+08:00");
//        dateTimePicker.setDateTimeChangedListener((formattedDate, formattedTime, selectedDate, selectedTime) -> {
//            Toast.makeText(MainActivity.this, " Date: " + formattedDate + " Time: " + formattedTime, Toast.LENGTH_LONG).show();
//        });
    }
}