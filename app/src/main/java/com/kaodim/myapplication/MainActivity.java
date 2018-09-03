package com.kaodim.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.kaodim.design.components.DateTimePicker;
import com.kaodim.design.components.InteractivePanel;
import com.kaodim.design.components.NumericControl;
import com.kaodim.design.components.PricingBottomBar;
import com.kaodim.design.components.SearchBox;
import com.kaodim.design.components.callbacks.NumericControlListener;
import com.kaodim.design.components.pre_loader.PreLoaderAnimation;
import com.kaodim.design.components.toast.ToastBanner;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    NumericControl numericControl;
    RelativeLayout rlDateSelection;
    DateTimePicker dateTimePicker;
    InteractivePanel interactivePanel;
    SearchBox searchBox;
    PricingBottomBar pricingBottomBar;
    Button toastSuccess, toastError, preLoader;
    LinearLayout toastMessageBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numericControl = findViewById(R.id.numericControl);
        dateTimePicker = findViewById(R.id.dateTimePicker);
        interactivePanel = findViewById(R.id.interactivePanel);
        searchBox = findViewById(R.id.searchBox);
        pricingBottomBar = findViewById(R.id.kdl_pricingBar);
        toastSuccess = findViewById(R.id.BtnToastSuccess);
        toastError = findViewById(R.id.BtnToastError);
        toastMessageBar = findViewById(R.id.lvTopMessage);
        preLoader = findViewById(R.id.BtnPreLoader);

        setupNumericControl();

        setupDateTimePicker();

        setupInteractivePanel();

        setupPricingListener();

        setupToastButtonListener();

        setupPreLoaderListerner();
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

    public void setupToastButtonListener(){
        toastSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSuccessBanner();
            }
        });

        toastError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callErrorBanner();
            }
        });
    }

    private void callSuccessBanner(){
        ToastBanner.getInstance().showSuccessAlert(toastMessageBar,
                this,"This is a very long multi line auto dismiss success message banner",ToastBanner.AUTODISMISS_LENGTH);
    }

    private void callErrorBanner(){
        ToastBanner.getInstance().showErrorAlert(toastMessageBar,
                this,"This is a very long multi line non-autodismiss error message banner ",ToastBanner.NO_AUTODISMISS);
    }

    private void setupPreLoaderListerner(){
        preLoader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingAnim();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideLoadingAnim();
    }


    //put this in baseActivity
    public  void showLoadingAnim(){
//        isLoadingAnim = true;
        View view = findViewById(android.R.id.content);
        PreLoaderAnimation.showLoading(view,this);
    }

    public void hideLoadingAnim(){
//        isLoadingAnim = false;
        PreLoaderAnimation.hideLoading();
    }
}