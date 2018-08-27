package com.kaodim.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kaodim.design.components.DateTimePicker;
import com.kaodim.design.components.InteractivePanel;
import com.kaodim.design.components.NumericControl;
import com.kaodim.design.components.PricingBottomBar;
import com.kaodim.design.components.SearchBox;
import com.kaodim.design.components.callbacks.NumericControlListener;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    NumericControl numericControl;
    RelativeLayout rlDateSelection;
    DateTimePicker dateTimePicker;
    InteractivePanel interactivePanel;
    SearchBox searchBox;
    PricingBottomBar pricingBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numericControl = findViewById(R.id.numericControl);
        dateTimePicker = findViewById(R.id.dateTimePicker);
        interactivePanel = findViewById(R.id.interactivePanel);
        searchBox = findViewById(R.id.searchBox);
        pricingBottomBar = findViewById(R.id.kdl_pricingBar);

        setupNumericControl();

        setupDateTimePicker();

        setupInteractivePanel();

        setupPricingListener();
    }

    private void setupInteractivePanel() {
        interactivePanel.setPanelInteractedListener(new InteractivePanel.InteractivePanelListener() {
            @Override
            public void onPanelInteracted() {
                //do your click events here
            }
        });

        interactivePanel.setBorderType(InteractivePanel.BORDER_TYPES.LINE);
    }

    private void setupNumericControl() {
        numericControl.setListener(new NumericControlListener() {
            @Override
            public void onNumericAdded(int value) {

            }

            @Override
            public void onNumericRemoved(int value) {

            }

            @Override
            public void onNumericValueChanged(int value) {

            }
        });
    }

    private void setupSearchBox() {
        searchBox.setHint("My Custom Text");
        searchBox.setDisplayNext(true);
        searchBox.setListener(new SearchBox.SearchBoxClickListener() {
            @Override
            public void onNextClicked() {

            }

            @Override
            public void onSearchBoxClicked() {

            }
        });
    }

    private void setupDateTimePicker() {
        dateTimePicker.initialize(this);
        dateTimePicker.setRangeStartTime("2018-01-1T7:30:30.042+08:00");
        dateTimePicker.setRangeEndTime("2018-10-20T22:00:30.042+08:00");
        dateTimePicker.setDateTimeChangedListener(new DateTimePicker.DateTimeChangedListener() {
            @Override
            public void onDateTimeSelected(String formatedDate, String formatedTime, Date selectedDate) {

            }

            @Override
            public void onDateRemoved() {

            }
        });
    }

    private void setupPricingListener(){
        pricingBottomBar.setButtonEnabled(true);
        pricingBottomBar.setPriceString("RM 100");
        pricingBottomBar.setButtonText("Pay Now");
        pricingBottomBar.setPriceTitle("Amount to pay");
        pricingBottomBar.setButtonOnClickListener(new PricingBottomBar.PricingBottomBarButtonListener() {
            @Override
            public void onButtonClicked() {
                Toast.makeText(getBaseContext(),"You have clicked Pricing Bottom Bar Button",Toast.LENGTH_SHORT).show();
            }
        });
    }
}