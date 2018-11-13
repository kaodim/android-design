package com.kaodim.design.components.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.kaodim.design.R;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateTimePickerDialog extends Dialog {

    /*
     * DateTime Picker Component is based on: https://github.com/AigeStudio/WheelPicker
     * */

    public interface DateTimePickerListener {
        void onDateTimeSelected(String formatedDate, String formatedTime, Date selectedDate);
    }

    private DateTimePickerListener listener;

    private WheelPicker wpDatePicker;
    private WheelPicker wpTimePicker;
    private TextView tvCancel;
    private TextView tvApply;

    private List<String> timeSlots = new ArrayList<>();
    private List<String> dateSlots = new ArrayList<>();
    List<DateTime> datesBetween = new ArrayList<>();

    private DateTime rangeStartTime = new DateTime();
    private DateTime rangeEndTime = new DateTime();

    private boolean displayDate = true;
    private boolean displayTime = true;
    private boolean cyclicEffect = false;
    private boolean curvedEffect = false;
    private boolean atmosphericEffect = true;
    private boolean showPastDates = false;
    private boolean ifSessionable = false;

    private int frequency;

    public DateTimePickerDialog(Activity activity, DateTimePickerListener listener) {
        super(activity);
        this.listener = listener;
    }

    public DateTimePickerDialog(Activity activity, DateTimePickerListener listener, DateTimePickerOptions options, boolean ifSessionable) {
        super(activity);
        this.listener = listener;
        this.displayDate = options.displayDate;
        this.displayTime = options.displayTime;
        this.cyclicEffect = options.cyclicEffect;
        this.curvedEffect = options.curvedEffect;
        this.atmosphericEffect = options.atmosphericEffect;
        this.ifSessionable = ifSessionable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.kdl_element_date_time_picker_dialog);

        initComponents();
    }

    private void initComponents() {
        wpDatePicker = findViewById(R.id.wpDatePicker);
        wpTimePicker = findViewById(R.id.wpTimePicker);
        tvCancel = findViewById(R.id.tvCancel);
        tvApply = findViewById(R.id.tvApply);

        //region Respond to options

        if (!displayDate) {
            wpDatePicker.setVisibility(View.GONE);
        }

        if (!displayTime) {
            wpTimePicker.setVisibility(View.GONE);
        } else {
            findViewById(R.id.wpTimePicker).setVisibility(View.VISIBLE);
        }

        //toggle cyclic effect
        wpDatePicker.setCyclic(cyclicEffect);
        wpTimePicker.setCyclic(cyclicEffect);

        //toggle curved effect
        wpDatePicker.setCurved(curvedEffect);
        wpTimePicker.setCurved(curvedEffect);

        //toggle atmospheric effect
        wpDatePicker.setAtmospheric(atmosphericEffect);
        wpTimePicker.setAtmospheric(atmosphericEffect);

        //endregion

        seedData();

        setEvents();
    }

    /**
     * Set click events
     */
    private void setEvents() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectedDateTime();
                dismiss();
            }
        });

        if (ifSessionable) {
            RelativeLayout rlNextSession = (RelativeLayout)findViewById(R.id.rlNextSession);
            rlNextSession.setVisibility(View.VISIBLE);
            final TextView nextSessionTextView = (TextView)findViewById(R.id.tvNextSession);

            wpDatePicker.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
                @Override
                public void onWheelScrolled(int offset) {

                }

                @Override
                public void onWheelSelected(int position) {
                    nextSessionTextView.setText(getContext().getResources().getString(R.string.your_next_session, getNextSessionString(dateSlots.get(position))));
                }

                @Override
                public void onWheelScrollStateChanged(int state) {

                }
            });
        }

    }

    private String getNextSessionString(String dateString) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy");
        try {
            date = format.parse(dateString);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar myCal = new GregorianCalendar();
        myCal.setTime(date);
        myCal.add(Calendar.DATE, frequency);
        return format.format(myCal.getTime());

    }

    /**
     * Retrieve and callback for date and time selection
     */
    private void getSelectedDateTime() {
        String formattedDate = dateSlots.get(wpDatePicker.getCurrentItemPosition());
        if (wpTimePicker.getCurrentItemPosition() == -1) {
            wpTimePicker.setSelectedItemPosition(0);
        }
        String formattedTime = "";
        if (timeSlots.size() > wpTimePicker.getCurrentItemPosition())
            formattedTime = timeSlots.get(wpTimePicker.getCurrentItemPosition());

        String selectedDateString = datesBetween.get(wpDatePicker.getCurrentItemPosition()).toString();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date selectedDate = new Date();
        try {
            //parse date
            selectedDate = format.parse(selectedDateString);

            //parse time
            if (!formattedTime.equals("")) {
                selectedDate.setHours(parseTime(formattedTime).getHours());
                selectedDate.setMinutes(parseTime(formattedTime).getMinutes());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        listener.onDateTimeSelected(formattedDate, formattedTime, selectedDate);
    }

    public void setRangeStartTime(DateTime rangeStartTime) {
        this.rangeStartTime = rangeStartTime;
    }

    public void setRangeEndTime(DateTime rangeEndTime) {
        this.rangeEndTime = rangeEndTime;
    }

    public void setSessionFrequency(int daysToNextSession) {
        this.frequency = daysToNextSession;
    }

    public void shouldShowPastDates(boolean showPastDates) {
        this.showPastDates = showPastDates;
    }

    /**
     * Seed Date and time data to pickers
     */
    private void seedData() {

        //TODO optional: for future functionality, check if shouldShowPastDates is toggled and display or hide past dates
        datesBetween = getDateRange(new DateTime(), new DateTime().plusYears(1));
        for (DateTime date : datesBetween) {
            dateSlots.add(DateTimeFormat.forPattern("EEE").print(date) + ", " + date.dayOfMonth().getAsString() + " " + date.toString("MMMM") + " " + date.year().getAsString());
        }

        Log.d("TIMEPICKERTEST", "Seeding for rangeStartTime: " + rangeStartTime);
        Log.d("TIMEPICKERTEST", "Seeding for rangeEndTime: " + rangeEndTime);

        List<String> timesBetween = getTimeRange(rangeStartTime, rangeEndTime);

        if (timeSlots.size() == 0) {
            timesBetween = getTimeRange(setStartTime("2018-06-14T7:00:00.249+08:00"), setEndTime("2018-06-14T23:00:00.249+08:00"));
        }

        timeSlots.addAll(timesBetween);

        Log.d("TIMEPICKERTEST", "Seeded: " + timeSlots.size());

        wpDatePicker.setData(dateSlots);
        wpTimePicker.setData(timeSlots);
    }

    public DateTime setStartTime(String rangeStartDateString) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        DateTime dateTime = formatter.parseDateTime(rangeStartDateString);
        return dateTime;
    }

    public DateTime setEndTime(String rangeEndDateString) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        DateTime dateTime = formatter.parseDateTime(rangeEndDateString);
        return dateTime;
    }

    /**
     * Optional attributes to alter the picker behavior
     */
    public static class DateTimePickerOptions {
        public boolean cyclicEffect = false; //endless picking from first item to last item and repeat
        public boolean curvedEffect = false; //toggle curved effect
        public boolean atmosphericEffect = true; //toggle atmospheric fade out when item is not in focus
        public boolean displayDate = true; //toggle date picker visibility
        public boolean displayTime = true; //toggle time picker visibility
    }

    private List<DateTime> getDateRange(DateTime start, DateTime end) {
        List<DateTime> dates = new ArrayList<>();
        int days = Days.daysBetween(start, end).getDays() + 1;
        for (int i = 0; i < days; i++) {
            DateTime d = start.withFieldAdded(DurationFieldType.days(), i);
            dates.add(d);
        }
        return dates;
    }

    private List<String> getTimeRange(DateTime start, DateTime end) {
        List<String> times = new ArrayList<>();
        int startTime = Integer.valueOf(start.hourOfDay().getAsString());
        int endTime = Integer.valueOf(end.hourOfDay().getAsString());
        for (int i = startTime; i <= endTime; i++) {
            times.add(convert24Clockto12Clock(i) + ":00" + (i < 12 ? "AM" : "PM"));
            //add up to 11:30PM
            if (i != 23)
                times.add(convert24Clockto12Clock(i) + ":30" + (i < 12 ? "AM" : "PM"));
        }
        return times;
    }

    private int convert24Clockto12Clock(int hourNumber) {
        int hour = hourNumber;
        switch (hourNumber) {
            case 13:
                hour = 1;
                break;
            case 14:
                hour = 2;
                break;
            case 15:
                hour = 3;
                break;
            case 16:
                hour = 4;
                break;
            case 17:
                hour = 5;
                break;
            case 18:
                hour = 6;
                break;
            case 19:
                hour = 7;
                break;
            case 20:
                hour = 8;
                break;
            case 21:
                hour = 9;
                break;
            case 22:
                hour = 10;
                break;
            case 23:
                hour = 11;
                break;
            case 24:
                hour = 12;
                break;
        }

        return hour;
    }

    private int convert12Clockto24Clock(int hourNumber) {
        int hour = hourNumber;
        switch (hourNumber) {
            case 1:
                hour = 13;
                break;
            case 2:
                hour = 14;
                break;
            case 3:
                hour = 15;
                break;
            case 4:
                hour = 16;
                break;
            case 5:
                hour = 17;
                break;
            case 6:
                hour = 18;
                break;
            case 7:
                hour = 19;
                break;
            case 8:
                hour = 20;
                break;
            case 9:
                hour = 21;
                break;
            case 10:
                hour = 22;
                break;
            case 11:
                hour = 23;
                break;
            case 12:
                hour = 24;
                break;
        }

        return hour;
    }

    /**
     * Parses a time string to hours and minutes
     *
     * @param timeString example: 7:00AM, 11:30PM
     * @return
     */
    private Date parseTime(String timeString) {
        Date date = new Date();
        boolean needs24HourConversion = timeString.contains("PM");

        if (needs24HourConversion) {
            date.setHours(convert12Clockto24Clock(Integer.parseInt(timeString.split(":")[0])));
            date.setMinutes(Integer.parseInt(timeString.substring(timeString.indexOf(":") + 1, timeString.indexOf("PM"))));
        } else {
            date.setHours(Integer.parseInt(timeString.split(":")[0]));
            date.setMinutes(Integer.parseInt(timeString.substring(timeString.indexOf(":") + 1, timeString.indexOf("AM"))));
        }

        return date;
    }
}