package com.kaodim.design.components;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaodim.design.R;
import com.kaodim.design.components.dialogs.DateTimePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimePicker extends RelativeLayout {

    public interface DateTimeChangedListener {
        void onDateTimeSelected(String formatedDate, String formatedTime, Date selectedDate, String selectedTimeStamp);
        void onDateRemoved();
    }

    private DateTimeChangedListener listener;

    private RelativeLayout rlDateSelection, rlRemoveDate;
    private TextView tvDate;
    private TextView tvLabel;
    private boolean showPastDates = false;
    private boolean showPastTimesToday = false;
    private boolean allowRemoval = false;
    private int delayTime;
    public String identifier = "";
    private String datePickerDescriptionText;

    private DateTime rangeStartDate = new DateTime();
    private DateTime rangeEndDate = new DateTime();
    private int rangeStartTime = 7;
    private int rangeEndTime = 23;
    private boolean startHalfHour;
    private  boolean endHalfHour;
    private String defaultSelectedDate;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    private DateTimePickerDialog.DateTimePickerOptions options;

    private String hint = "Pick a date";
    private Activity activity;

    public DateTimePicker(Context context) {
        super(context);
        init(context);
    }

    public DateTimePicker(Context context, AttributeSet set) {
        super(context, set);
        init(context);
    }

    public DateTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //Retrieve the custom attributes from XML
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.SearchBox);

        DateTimePickerDialog.DateTimePickerOptions options = new DateTimePickerDialog.DateTimePickerOptions();
        options.curvedEffect = typedArray.getBoolean(R.styleable.DateTimePicker_curvedEffect, false);
        options.cyclicEffect = typedArray.getBoolean(R.styleable.DateTimePicker_cyclicEffect, false);
        options.atmosphericEffect = typedArray.getBoolean(R.styleable.DateTimePicker_atmosphericEffect, true);
        options.displayTime = typedArray.getBoolean(R.styleable.DateTimePicker_displayTime, true);
        options.displayDate = typedArray.getBoolean(R.styleable.DateTimePicker_displayDate, true);
        hint = typedArray.getString(R.styleable.DateTimePicker_pickerHint);
        setOptions(options);

        //Recycle the TypedArray (saves memory)
        typedArray.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.kdl_element_date_time_picker_layout, this);
        initComponents();
    }

    private void setOptions(DateTimePickerDialog.DateTimePickerOptions options) {
        this.options = options;
    }

    private void initComponents() {
        rlDateSelection = findViewById(R.id.rlDateSelection);
        rlRemoveDate = findViewById(R.id.rlRemoveDate);
        tvDate = findViewById(R.id.tvDate);
        tvLabel = findViewById(R.id.tvLabel);
        setEvents();
    }

    private void setEvents() {
        rlDateSelection.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dateSelectionClicked();
            }
        });

        rlRemoveDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDateRemoved();
                }
            }
        });
    }

    public void setDisable(boolean disable){
        if(disable){
            rlDateSelection.setFocusable(false);
            rlDateSelection.setBackground(getResources().getDrawable(R.drawable.background_disabled));
            rlDateSelection.setEnabled(false);
            tvDate.setTextColor(getResources().getColor(R.color.kdl_grey_dark));
        }else{
            rlDateSelection.setFocusable(true);
            rlDateSelection.setBackground(getResources().getDrawable(R.drawable.kdl_rect_grey_curved));
            rlDateSelection.setEnabled(true);
            tvDate.setTextColor(getResources().getColor(R.color.blackpearl));
        }
    }

    private void dateSelectionClicked() {
        if (activity != null) {

            DateTimePickerDialog pickerDialog = new DateTimePickerDialog(activity, new DateTimePickerDialog.DateTimePickerListener() {
                @Override
                public void onDateTimeSelected(String formatedDate, String formatedTime, Date selectedDate, String selectedTimeStamp) {
                    if (listener != null) {
                        listener.onDateTimeSelected(formatedDate, formatedTime, selectedDate,selectedTimeStamp);
                    }
                }
            }, options);


            pickerDialog.setRangeStartDate(rangeStartDate);
            pickerDialog.setRangeEndDate(rangeEndDate);
            pickerDialog.setDelayTime(delayTime);
            pickerDialog.setDefaultEndHour(rangeEndTime);
            pickerDialog.setDefaultStartHour(rangeStartTime);
            pickerDialog.setHasEndHalfHour(endHalfHour);
            pickerDialog.setHasStartHalfHour(startHalfHour);
            pickerDialog.setDefaultSelectedDate(defaultSelectedDate);
            pickerDialog.shouldPastTimeToday(showPastTimesToday);

            if(datePickerDescriptionText!=null && !datePickerDescriptionText.isEmpty()){
                pickerDialog.setDesriptionText(datePickerDescriptionText);
            }

            pickerDialog.show();
            Window window = pickerDialog.getWindow();
            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    public void initialize(Activity activity) {
        this.activity = activity;
    }

    public void initialize(Activity activity, DateTimePickerDialog.DateTimePickerOptions options) {
        this.activity = activity;
        setOptions(options);
    }

    public void setDateTimeChangedListener(DateTimeChangedListener listener) {
        this.listener = listener;
    }

    public void setHint(String hintText) {
        this.hint = hintText;
        tvDate.setText(hint);
    }

    public void setLabel(String labelText){
        tvLabel.setText(labelText);
        tvLabel.setVisibility(View.VISIBLE);
    }

    /**
     * Delay start time if current date
     **/
    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public void setDatePickerDescriptionText(String datePickerDescriptionText) {
        this.datePickerDescriptionText = datePickerDescriptionText;
    }

    public String getDatePickerDescriptionText() {
        return datePickerDescriptionText;
    }

    public void shouldShowPastDates(boolean showPastDates) {
        this.showPastDates = showPastDates;
    }

    public void shouldShowPastTimesForToday(boolean shouldShowPastTime) {
        this.showPastTimesToday = shouldShowPastTime;
    }

    public void setDefaultSelectedDate(String defaultSelectedDate) {
        this.defaultSelectedDate = defaultSelectedDate;
    }

    public void setAllowRemoval(boolean allowRemoval) {
        this.allowRemoval = allowRemoval;
        rlRemoveDate.setVisibility((allowRemoval) ? VISIBLE : GONE);
    }

    public void setRangeStartDate(String rangeStartDateString) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        DateTime dateTime = formatter.parseDateTime(rangeStartDateString);
        this.rangeStartDate = dateTime;
    }



    public void setRangeEndDate(String rangeEndDateString) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        DateTime dateTime = formatter.parseDateTime(rangeEndDateString);
        this.rangeEndDate = dateTime;
    }

    public void setDateRange(String rangeStartDateString, String rangeEndDateString){
       setRangeStartDate(rangeStartDateString);
       setRangeEndDate(rangeEndDateString);
    }


    public void setRangeStartTime(float startTime) {
        int mTime;

        if(startTime%1==0.5){
            startHalfHour = true;
            mTime = (int) (startTime - 0.5);
        }
        else{
            mTime = (int) startTime;
        }

        this.rangeStartTime = mTime;
    }

    public void setRangeEndTime(float endTime) {
        int mTime;

        if(endTime%1==0.5){
            endHalfHour = true;
            mTime = (int) (endTime - 0.5);
        }
        else{
            mTime = (int) endTime;
        }

        this.rangeEndTime = mTime;
    }


//    public void setRangeStartTimeWithTimeStamp(String timeStamp) {
//        int mTime;
//        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//        DateTime dateTime = formatter.parseDateTime(timeStamp);
//
//        if(dateTime.getMinuteOfHour() == 30){
//            startHalfHour = true;
//        }
//
//        mTime = (int) (dateTime.getHourOfDay());
//        this.rangeStartTime = mTime;
//    }
//
//    public void setRangeEndTimeWithTimeStamp(String timeStamp) {
//        int mTime;
//
//        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//        DateTime dateTime = formatter.parseDateTime(timeStamp);
//
//        if(dateTime.getMinuteOfHour() == 30){
//            endHalfHour = true;
//        }
//
//        mTime = (int) (dateTime.getHourOfDay());
//        this.rangeEndTime = mTime;
//    }

    public boolean hasHint() {
        return hint.length() > 0;
    }

    public void triggerPicker() {
        dateSelectionClicked();
    }
}
