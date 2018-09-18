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
import com.kaodim.design.components.SearchBox;
import com.kaodim.design.components.bottomBars.FullWidthBottomBar;
import com.kaodim.design.components.bottomBars.PricingBottomBar;
import com.kaodim.design.components.callbacks.NumericControlListener;
import com.kaodim.design.components.notes.NotesInfo;
import com.kaodim.design.components.notes.NotesStandard;
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
    FullWidthBottomBar fullWidthBottomBar;
    Button toastSuccess, toastError, preLoader;
    LinearLayout toastMessageBar;


    NotesStandard notesStandard;
    NotesInfo notesInfo;

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
        notesStandard = findViewById(R.id.notesStandardView);
        notesInfo = findViewById(R.id.notesInfoView);
        fullWidthBottomBar = findViewById(R.id.kdl_full_width_bottombar);

        setupNumericControl();

        setupDateTimePicker();

        setupInteractivePanel();

        setupPricingListener();

        setupToastButtonListener();

        setupPreLoaderListerner();

        setupNotesStandardListener();

        setupNotesInfo();

        setupFullWidthBottomBar();
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

   private void setupNotesStandardListener(){
        notesStandard.setNotesType(NotesStandard.TYPE_NO_BUTTON);
        notesStandard.setDescriptionText("This is very long multi line notes description for standard notes");
        notesStandard.setButtonSingleText("Click Me");
        notesStandard.setButtonDoublePrimaryText("Primary");
        notesStandard.setButtonDoubleSecondaryText("Secondary");
        notesStandard.setTvDescriptionVisibility(View.GONE);
        notesStandard.setSecondaryDescription("This is secondary description");
        notesStandard.setEnableButtonSingle(true);


        notesStandard.setButtonOnClickListener(new NotesStandard.NotesStandardListener() {
            @Override
            public void onButtonSingleClicked() {
                Toast.makeText(getBaseContext(),"You have clicked Notes' Single Button",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onButtonDoublePrimaryClicked() {
                Toast.makeText(getBaseContext(),"You have clicked Notes' Primary Double Button",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onButtonDoubleSecondaryClicked() {
                Toast.makeText(getBaseContext(),"You have clicked Notes' Secondary Double Button",Toast.LENGTH_SHORT).show();
            }
        });
   }

   private void setupNotesInfo(){
        notesInfo.setNotesType(NotesInfo.TYPE_WITH_ICON_TITLE);
        notesInfo.setNotesInfoTitleText("This is an example title");
        notesInfo.setNotesInfoDescriptionText("This is very long multi line notes description for notes info component");
//        notesInfo.setTitleVisibility(View.VISIBLE);
//        notesInfo.setDescriptionVisibility(View.VISIBLE);
//        notesInfo.setIconVisibility(View.VISIBLE);
   }

   private void setupFullWidthBottomBar(){
        fullWidthBottomBar.setButtonText("Proceed");
        fullWidthBottomBar.setButtonEnabled(true);


        fullWidthBottomBar.setButtonOnClickListener(new FullWidthBottomBar.FullWidthBottomBarButtonListener() {
            @Override
            public void onButtonClicked() {
                Toast.makeText(getBaseContext(),"You have clicked Full Width Bottom Bar Button",Toast.LENGTH_SHORT).show();
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