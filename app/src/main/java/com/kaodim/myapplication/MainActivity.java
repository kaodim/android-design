package com.kaodim.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;

import com.kaodim.design.components.DateTimePicker;
import com.kaodim.design.components.InteractivePanel;
import com.kaodim.design.components.NumericControl;
import com.kaodim.design.components.NumericControlWithEditText;
import com.kaodim.design.components.SearchBox;
import com.kaodim.design.components.bottomBars.FullWidthBottomBar;
import com.kaodim.design.components.bottomBars.PricingBottomBar;
import com.kaodim.design.components.dialogs.DateTimePickerDialog;
import com.kaodim.design.components.dialogs.ModalDialog;
import com.kaodim.design.components.editText.KaodimEditText;
import com.kaodim.design.components.editText.KaodimPhoneNumberEditText;
import com.kaodim.design.components.informationBars.InfoBar;
import com.kaodim.design.components.notes.NotesError;
import com.kaodim.design.components.notes.NotesInfo;
import com.kaodim.design.components.notes.NotesStandard;
import com.kaodim.design.components.pre_loader.PreLoaderAnimation;
import com.kaodim.design.components.searchEditText.SearchEditText;
import com.kaodim.design.components.toast.ToastBanner;
import com.kaodim.design.components.tooltip.ViewTooltip;
import com.kaodim.design.components.viewText.KaodimViewText;
import com.kaodim.myapplication.databinding.ActivityMainBinding;
import com.kaodim.myapplication.fragement.CalendarViewDialogFragment;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    NumericControl numericControl;
    NumericControlWithEditText numericControlWithEditText;
    RelativeLayout rlDateSelection;
    DateTimePicker dateTimePicker;
    InteractivePanel interactivePanel;
    SearchBox searchBox;
    PricingBottomBar pricingBottomBar;
    FullWidthBottomBar fullWidthBottomBar;
    Button toastSuccess, toastError, preLoader, modalDialog, newModalDialog;
    LinearLayout toastMessageBar;
    Button btnShowToolTip, btnHideToolTip, btnShowViewText, btnHideViewText,btnShowCalendarDialog;
    ImageView icBulb;
    LinearLayout toolTipParentView;
    KaodimEditText ketEditText, gender, password, etNotes, etGender;
    KaodimViewText kvtViewText;
    SearchEditText setSearchText;
    TextView tvSearchTextDelay, tvSearchTextInstantenous;
    KaodimPhoneNumberEditText phoneEditText;

    InfoBar blueInfoBar, greenInfoBar, orangeInfoBar;

    NotesStandard notesStandard;
    NotesError notesError;
    NotesInfo notesInfo;

    ViewTooltip.TooltipView tooltip;

    MutableLiveData<String> test = new MutableLiveData();

    AlertDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

//        gender = findViewById(R.id.gender);
//        etGender = findViewById(R.id.etGender);
        numericControl = findViewById(R.id.numericControl);
        numericControlWithEditText = findViewById(R.id.numericControlWithEditText);
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
        newModalDialog = findViewById(R.id.btnNewModalDialog );
        btnShowToolTip = findViewById(R.id.BtnShowToolTip);
        btnHideToolTip = findViewById(R.id.BtnHideToolTip);
        icBulb = findViewById(R.id.ivImage);
        toolTipParentView = findViewById(R.id.llToolTipParentView);
//        ketEditText = findViewById(R.id.ketEditText);
        kvtViewText = findViewById(R.id.kvtViewText);
        btnShowViewText = findViewById(R.id.BtnShowViewText);
        btnHideViewText = findViewById(R.id.BtnHideViewText);
        setSearchText = findViewById(R.id.setSearchTextSearch);
        tvSearchTextDelay = findViewById(R.id.tvSearchTextDelay);
        tvSearchTextInstantenous = findViewById(R.id.tvSearchTextInstantenous);
        btnShowCalendarDialog = findViewById(R.id.BtnShowCalendarDialog);
        blueInfoBar = findViewById(R.id.blueInfoBar);
        greenInfoBar = findViewById(R.id.greenInfoBar);
        orangeInfoBar = findViewById(R.id.orangeInfoBar);
        orangeInfoBar.setLayoutPadding(0,0,0,0);
        phoneEditText = findViewById(R.id.phoneEditText);
        password = findViewById(R.id.password);
        etNotes = findViewById(R.id.etNotes);

        etNotes.getInputEditText().setMinLines(1);
//
//        etNotes.setText("BIG NOTES");
//        etNotes.setEnabled(false);

//        etGender.setEnabled(false);

        LinearLayout layout = findViewById(R.id.llRoot);
        mainBinding.setTest(test);
        mainBinding.setLifecycleOwner(this);

        setupNumericControl();

        setupDateTimePicker();

//        setupDateTimePickerWithSessionText();

        setupInteractivePanel();

        setupPricingListener();

        setupToastButtonListener();

        setupPreLoaderListerner();

        setupNotesStandardListener();

        setupErrorNotesStandardListener();

        setupNotesInfo();

        setupFullWidthBottomBar();

        setToolTipContainerListener();

        setViewTextListener();

        setSearchTextListener();

        setBtnShowCalendarDialogListener();

        dateTimePicker.initialize(this);

        modalDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupModalDialog();
            }
        });

        newModalDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupNewModalDialog();
            }
        });

        setupInfoBars();

        setupGenderEditText();

        setupPhoneNumberEditText();

        setupNotesEditText();
    }

    private void setupNotesEditText() {
//        etNotes.setText("Here's a note");
    }

    private void setupPhoneNumberEditText() {
        password.setText("password");
//        password.setEnabled(false);
//        password.setTextButtonListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getBaseContext(), "Text button on click", Toast.LENGTH_SHORT).show();
//                password.setEnabled(true);
//            }
//        });

        phoneEditText.setTextButtonOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Text button on click", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupGenderEditText() {
//        gender.getInputEditText().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getBaseContext(),"Gender on Click",Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void setupInfoBars() {
        blueInfoBar.setPrimaryText("Your bank details are approved. If you need to change them please reach out to customer support.");
    }

    private void setupInteractivePanel() {
        interactivePanel.setPanelInteractedListener(new InteractivePanel.InteractivePanelListener() {
            @Override
            public void onPanelInteracted() {
                //do your click events here
            }
        });

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
        DateTimePickerDialog.DateTimePickerOptions options = new DateTimePickerDialog.DateTimePickerOptions();
        options.displayTime = true;
        dateTimePicker.initialize(this,options);
        dateTimePicker.setDateRange("2018-12-03T14:44:00.000+08:00","2018-12-30T23:00:00.000+08:00");
        dateTimePicker.setRangeStartTime((float) 7);
        dateTimePicker.setRangeEndTime((float) 23);
        dateTimePicker.shouldShowPastTimesForToday(false);
        dateTimePicker.setDateTimeChangedListener(new DateTimePicker.DateTimeChangedListener() {
            @Override
            public void onDateTimeSelected(String formatedDate, String formatedTime, Date selectedDate,String selectedTimeStamp) {
                    String date = formatedDate + " " + formatedTime;
                    dateTimePicker.setHint(date);
                    dateTimePicker.setDefaultSelectedDate(selectedTimeStamp);
                Log.d("DateTimeStamp", selectedTimeStamp);
                    Log.d("Date", selectedDate.toString());
            }

            @Override
            public void onDateRemoved() {
                dateTimePicker.setHint("");
                dateTimePicker.setDefaultSelectedDate(null);
            }
        });
    }

    private void setupDateTimePickerWithSessionText() {
        dateTimePicker.initialize(this);
//        dateTimePicker.setRangeStartTime("2018-01-1T7:30:30.042+08:00");
//        dateTimePicker.setRangeEndTime("2018-10-20T22:00:30.042+08:00");
        dateTimePicker.setDateTimeChangedListener(new DateTimePicker.DateTimeChangedListener() {
            @Override
            public void onDateTimeSelected(String formatedDate, String formatedTime, Date selectedDate,String selectedTimeStamp) {

            }

            @Override
            public void onDateRemoved() {

            }
        });
    }


    private void setupPricingListener(){
        pricingBottomBar.setPriceString("RM 100");
        pricingBottomBar.setButtonText("Pay Now");
        pricingBottomBar.setPriceTitle("Amount to pay");
        pricingBottomBar.setButtonEnabled(true);
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
       final ModalDialog modalDialog = new ModalDialog(this,view);
       modalDialog.setType(ModalDialog.TYPE_SINGLE_BUTTON);
       modalDialog.setDissmissable(true);
//       modalDialog.setTextForNoButton("This is title text", "This is an example of a very long description");
//       modalDialog.setTextForDoubleButton("This is title text", "This is an example of a very long description",
//               "Primary","Secondary");
       modalDialog.setTextForSingleButton("This is title text", "This is an example of a very long description",
               "Primary");

//       modalDialog.setIcon(this,"https://www.highburyconsulting.com/wp-content/uploads/2017/08/water-1330252_1920.jpg",5f);
//       modalDialog.setIconHeight(206,this);
//       modalDialog.setIconMargin(60,60,60,0);
//       modalDialog.setIconScaleType(ImageView.ScaleType.CENTER);
       modalDialog.setIcon(R.drawable.illus_settings);
//       modalDialog.setTitleVisibility(View.GONE);
//       modalDialog.setCancelIconVisibility(View.GONE);
       modalDialog.setButtonOnClickListener(new ModalDialog.ModalDialogListener() {
           @Override
           public void onButtonPrimaryClicked() {
               Toast.makeText(getBaseContext(),"You have clicked Modal Dialog Single Button",Toast.LENGTH_SHORT).show();
               modalDialog.hide();
           }

           @Override
           public void onButtonSecondaryClicked() {
               Toast.makeText(getBaseContext(),"You have clicked Modal Dialog Single Button",Toast.LENGTH_SHORT).show();
           }
       });
       modalDialog.show();

   }

    private void setupNewModalDialog(){
        View view = findViewById(android.R.id.content);
        final ModalDialog modalDialog = new ModalDialog(this,view);
        modalDialog.setType(ModalDialog.TYPE_DOUBLE_BUTTON_NO_ICON_NON_DISMISS);

//        modalDialog.setTextForSingleButtonRow("", "A brand new update for the app is available in the app store. Update now to get all the shiny new stuff!",
//                "Primary");
        modalDialog.setTextForDoubleButtonRow("Test", "A brand new update for the app is available in the app store. Update now to get all the shiny new stuff!",
                "Primary","Cancel");
        modalDialog.setIcon(R.drawable.illustration_update_customer);
       modalDialog.setIconHeight(217,this);
       modalDialog.setIconMarginWithDP(24,24,24,0,this);
       modalDialog.setIconScaleType(ImageView.ScaleType.FIT_CENTER);
       modalDialog.setButtonOnClickListener(new ModalDialog.NewModalDialogListener() {
           @Override
           public void onButtonPrimaryClicked() {
               Toast.makeText(getBaseContext(),"You have clicked Modal Dialog Single Button",Toast.LENGTH_SHORT).show();
               modalDialog.hide();
           }

           @Override
           public void onButtonSecondaryClicked() {
               Toast.makeText(getBaseContext(),"You have clicked Modal Dialog Single Button",Toast.LENGTH_SHORT).show();
               modalDialog.hide();
           }

           @Override
           public void onButtonDismissedClicked() {

           }
       },true);
        modalDialog.show();

    }

   private void setToolTipContainerListener(){
        btnShowToolTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showtooltip(icBulb);
            }
        });

        btnHideToolTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tooltip.close();
            }
        });
   }

    private void setViewTextListener(){
        btnShowViewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kvtViewText.setText("Show Text");
            }
        });

        btnHideViewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kvtViewText.setText("");
            }
        });
    }

    private void setSearchTextListener(){
        setSearchText.addTextChangedListener(new SearchEditText.SearchEditTextChangeListener() {
            @Override
            public void onTextChanged(String res) {
                tvSearchTextInstantenous.setText(res);
            }
        });

        setSearchText.addTextChangedListenerWithDelay(this, new SearchEditText.SearchEditTextChangeListener() {
            @Override
            public void onTextChanged(String res) {
                tvSearchTextDelay.setText(res);
            }
        });
    }

    private void setBtnShowCalendarDialogListener(){
        btnShowCalendarDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarViewDialogFragment calendarViewDialogFragment = new CalendarViewDialogFragment();
                calendarViewDialogFragment.show(getSupportFragmentManager(),"Calendar");
            }
        });
    }

   private void showtooltip(View view){
       View tooltipView = LayoutInflater.from(this).inflate(R.layout.example_tool_tip_view, null);

       DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
       int px = Math.round(16 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));

       tooltip = ViewTooltip.on(view)
               .clickToHide(true)
               .autoHide(false, 0)
               .align(ViewTooltip.ALIGN.CENTER)
               .margin(px, px, px, px)
               .position(ViewTooltip.Position.TOP)
               .customView(tooltipView)
               .arrowHeight(30)
               .arrowWidth(40)
               .color(Color.WHITE)
               .withShadow(true)
               .distanceWithView(20)
               .show();
   }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideLoadingAnim();
    }

    //put this in baseActivity
    public void showLoadingAnim() {
        View view = findViewById(android.R.id.content);
        PreLoaderAnimation.showLoading(view, this);
    }

    public void hideLoadingAnim(){
        PreLoaderAnimation.hideLoading();
    }
}