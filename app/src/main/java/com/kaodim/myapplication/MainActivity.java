package com.kaodim.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
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
import com.kaodim.design.components.dialogs.ModalDialog;
import com.kaodim.design.components.notes.NotesError;
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
    Button toastSuccess, toastError, preLoader, modalDialog;
    LinearLayout toastMessageBar;


    NotesStandard notesStandard;
    NotesError notesError;
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
        notesError = findViewById(R.id.notesErrorView);
        notesInfo = findViewById(R.id.notesInfoView);
        fullWidthBottomBar = findViewById(R.id.kdl_full_width_bottombar);
        modalDialog = findViewById(R.id.btnModalDialog);

        setupNumericControl();

//        setupDateTimePicker();

        setupDateTimePickerWithSessionText();

        setupInteractivePanel();

        setupPricingListener();

        setupToastButtonListener();

        setupPreLoaderListerner();

        setupNotesStandardListener();

        setupErrorNotesStandardListener();

        setupNotesInfo();

        setupFullWidthBottomBar();

        modalDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupModalDialog();
            }
        });
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

    private void setupDateTimePickerWithSessionText() {
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

        dateTimePicker.setSessionableTimePicker(true);
        dateTimePicker.setSessionFrequency(7);
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
        notesStandard.setTvDescriptionVisibility(View.GONE);
        notesStandard.setDescriptionText("This is very long multi line notes description for standard notes");
        notesStandard.setButtonSingleText("Click Me");
        notesStandard.setButtonDoublePrimaryText("Primary");
        notesStandard.setButtonDoubleSecondaryText("Secondary");
        notesStandard.setTvDescriptionVisibility(View.VISIBLE);
//        notesStandard.setEnableButtonSingle(true);

//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(21,17);
//        layoutParams.setMargins(0,8,15,0);
//        notesStandard.setIconSize(layoutParams);


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

    private void setupErrorNotesStandardListener(){
        notesError.setNotesType(NotesError.TYPE_DOUBLE_BUTTON);
        notesError.setDescriptionText("This is a very long multi line error notes description for error notes");
        notesError.setButtonSingleText("Button");
        notesError.setButtonDoublePrimaryText("Button Primary");
        notesError.setButtonDoubleSecondaryText("Button Secondary");

        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
        layoutParams.setMargins(0,8,15,0);
        notesError.setIconSize(layoutParams);
        notesError.setIcon(R.drawable.ic_notes_error);
        notesError.setIconSize(20,20);

        notesError.setButtonOnClickListener(new NotesStandard.NotesStandardListener() {
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

//       LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(13,18);
//       layoutParams.setMargins(0,10,26,0);
//       notesInfo.setIconSize(layoutParams);
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

   private void setupModalDialog(){
       View view = findViewById(android.R.id.content);
       ModalDialog modalDialog = new ModalDialog(this,view);
       modalDialog.setType(ModalDialog.TYPE_DOUBLE_BUTTON);
       modalDialog.setTextForDoubleButton("This is title text", "This is an example of a very long description",
               "Primary","Secondary");
//       modalDialog.setTextForSingleButton("This is title text", "This is an example of a very long description",
//               "Primary");
       modalDialog.setIcon(R.drawable.illus_settings);
       modalDialog.setButtonOnClickListener(new ModalDialog.ModalDialogListener() {
           @Override
           public void onButtonPrimaryClicked() {
               Toast.makeText(getBaseContext(),"You have clicked Modal Dialog Single Button",Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onButtonSecondaryClicked() {
               Toast.makeText(getBaseContext(),"You have clicked Modal Dialog Single Button",Toast.LENGTH_SHORT).show();
           }
       });
       modalDialog.show();
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