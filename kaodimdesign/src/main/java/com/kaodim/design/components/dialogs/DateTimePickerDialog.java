package com.kaodim.design.components.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.kaodim.design.R;
import com.kaodim.design.components.DateTimePicker;

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
import java.util.Iterator;
import java.util.List;

public class DateTimePickerDialog extends Dialog {

    /*
     * DateTime Picker Component is based on: https://github.com/AigeStudio/WheelPicker
     * */

    public interface DateTimePickerListener {
        void onDateTimeSelected(String formatedDate, String formatedTime, Date selectedDate, String selectedTimeStamp);
    }

    private DateTimePickerListener listener;

    private WheelPicker wpDatePicker;
    private WheelPicker wpTimePicker;
    private TextView tvCancel;
    private TextView tvApply;
    private TextView tvDescription;
    private LinearLayout llDateDecriptionText;

    private List<String> timeSlots = new ArrayList<>();
    private List<String> dateSlots = new ArrayList<>();
    List<DateTime> datesBetween = new ArrayList<>();

    private DateTime rangeStartDate = new DateTime();
    private DateTime rangeEndDate= new DateTime();

    private boolean displayDate = true;
    private boolean displayTime = true;
    private boolean cyclicEffect = false;
    private boolean curvedEffect = false;
    private boolean atmosphericEffect = true;
    private boolean showPastDates = false;
    private boolean shouldShowPastTime = false;
    private int delayTime;
    private int defaultStartHour = 7;
    private int defaultEndHour = 23;
    private boolean hasStartHalfHour;
    private boolean hasEndHalfHour;
    public String textDescription;
    private String defaultSelectedDate;

    public DateTimePickerDialog(Activity activity, DateTimePickerListener listener) {
        super(activity);
        this.listener = listener;
    }

    public DateTimePickerDialog(Activity activity, DateTimePickerListener listener, DateTimePickerOptions options) {
        super(activity);
        this.listener = listener;
        this.displayDate = options.displayDate;
        this.displayTime = options.displayTime;
        this.cyclicEffect = options.cyclicEffect;
        this.curvedEffect = options.curvedEffect;
        this.atmosphericEffect = options.atmosphericEffect;
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
        tvDescription = findViewById(R.id.tvDateDescriptionText);
        llDateDecriptionText = findViewById(R.id.llDateValidation);

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

        checkTextDescription();

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
    }

    public void setDesriptionText(String text){
        this.textDescription = text;

    }

    private void checkTextDescription(){
        if(textDescription!=null && !textDescription.isEmpty()){
            llDateDecriptionText.setVisibility(View.VISIBLE);
            tvDescription.setText(textDescription);
        }
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

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
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

        listener.onDateTimeSelected(formattedDate, formattedTime, selectedDate,format.format(selectedDate));
    }


    public void setRangeStartDate(DateTime rangeStartTime) {
        this.rangeStartDate = rangeStartTime;
    }

    public void setRangeEndDate(DateTime rangeEndTime) {
        this.rangeEndDate = rangeEndTime;
    }

    public void shouldShowPastDates(boolean showPastDates) {
        this.showPastDates = showPastDates;
    }

    public void shouldShowTimeToday(boolean shouldShowPastTime) {
        this.shouldShowPastTime = shouldShowPastTime;
    }

    public void setDefaultEndHour(int defaultEndHour) {
        this.defaultEndHour = defaultEndHour;
    }

    public void setDefaultStartHour(int defaultStartHour) {
        this.defaultStartHour = defaultStartHour;
    }

    public void setHasEndHalfHour(boolean hasEndHalfHour) {
        this.hasEndHalfHour = hasEndHalfHour;
    }

    public void setHasStartHalfHour(boolean hasStartHalfHour) {
        this.hasStartHalfHour = hasStartHalfHour;
    }

    /**
     * Delay Time WheelPicker in hours, this is to show next available hours
     */
    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    /**
     * Seed Date and time data to pickers
     */
    private void seedData() {

        //TODO optional: for future functionality, check if shouldShowPastDates is toggled and display or hide past dates

        datesBetween = getDateRange(rangeStartDate, rangeEndDate);

        if(datesBetween.size() == 0){
            datesBetween = getDateRange(new DateTime().minusYears(1), new DateTime().plusYears(1));
        }

        for (DateTime date : datesBetween) {
            dateSlots.add(DateTimeFormat.forPattern("EEE").print(date) + ", " + date.dayOfMonth().getAsString() + " " + date.toString("MMMM") + " " + date.year().getAsString());
        }

        List<String> timesBetween = getTimeRange(defaultStartHour,defaultEndHour);


        timeSlots.addAll(timesBetween);

        Log.d("TIMEPICKERTEST", "Seeded: " + timeSlots.size());

        setDateWheelPickerListener();
        wpDatePicker.setData(dateSlots);
        wpTimePicker.setData(timeSlots);
        compareCurrentDateWithList(0);
        setDefaulSelectedDate();
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

    private void setDefaulSelectedDate(){
        if(defaultSelectedDate!=null && !defaultSelectedDate.isEmpty()){
            if(displayDate){
                wpDatePicker.setSelectedItemPosition(getPositionForSelectedDate());
            }

           if(displayTime){
               wpTimePicker.setSelectedItemPosition(getPositionForSelectedDateTime());
           }
        }
    }


    /**
     * This method used calculated the position of the default selected date in date wheel picker
    **/
    private int getPositionForSelectedDate() {
       int position = 0;
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        DateTime selectedDate = formatter.parseDateTime(defaultSelectedDate)
                .withHourOfDay(7).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);

        Log.d("TIMEPICKERTEST", "SelectedDate: " + selectedDate);

        for (int i = 0; i < datesBetween.size(); i++) {
            Log.d("TIMEPICKERTEST", "DatesBetween: " + datesBetween.get(i));
            if(selectedDate.toString().equals(datesBetween.get(i).toString())){
                Log.d("TIMEPICKERTEST", "DatesSame");
                return i;
            }
        }
        return position;
    }


    /**
     * This method used calculated the position of the default selected time
     **/
    private int getPositionForSelectedDateTime() {
        int position = 0;
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        DateTime selectedDate = formatter.parseDateTime(defaultSelectedDate);
        String timeSlot;
        int time =  selectedDate.getHourOfDay();

        if(selectedDate.getMinuteOfHour()==30){
            timeSlot = convert24Clockto12Clock(time) + ":30" + (time < 12 ? "AM" : "PM").trim();
        }else{
            timeSlot = convert24Clockto12Clock(time) + ":00" + (time < 12 ? "AM" : "PM").trim();
        }

        Log.d("TIMEPICKERTEST", "SelectedTime: " + timeSlots);

        for (int i = 0; i < timeSlots.size(); i++) {
            Log.d("TIMEPICKERTEST", "SelectedTime: " + timeSlots.get(i));
            if(timeSlot.equals(timeSlots.get(i).trim())){
                Log.d("TIMEPICKERTEST", "SelectedTime: " + "Same");
                return i;
            }
        }
        return position;
    }

    public void setDefaultSelectedDate(String defaultSelectedDate) {
        this.defaultSelectedDate = defaultSelectedDate;
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

    /**
     * Get time range between start and endtime as long as hour between 7-23
     */
    private List<String> getTimeRange(int start, int end) {
        List<String> times = new ArrayList<>();
        boolean isStartHalfHour = hasStartHalfHour;
        Log.d("TIMEPICKERTEST", "HalfStartHalf: " + hasStartHalfHour);
        Log.d("TIMEPICKERTEST", "DefaultEndHout: " + defaultEndHour);
        Log.d("TIMEPICKERTEST", "HalfEndHout: " + hasEndHalfHour);

        for (int i = start; i <= end; i++) {
            if(i >= defaultStartHour && i <= defaultEndHour) {
                if(!isStartHalfHour)
                    times.add(convert24Clockto12Clock(i) + ":00" + (i < 12 ? "AM" : "PM"));

                if (!(i == end)){
                    times.add(convert24Clockto12Clock(i) + ":30" + (i < 12 ? "AM" : "PM"));
                }

                if (i == defaultEndHour && hasEndHalfHour){
                    times.add(convert24Clockto12Clock(i) + ":30" + (i < 12 ? "AM" : "PM"));
                }

                isStartHalfHour = false;
            }
        }
        return times;
    }


    /**
     * Get time range for currentDate
     * If the current Date min <= 30, skip to next hour.
     * E.g Current Time 10:21 AM, will set the first hour step as 11:00 AM
     * If the current Date min > 30, skip to next 30
     * E.g Current Time 10:41 AM, will set the first hour step as 11:30 AM
     */
    private List<String> getTimeRangeForCurrentDate(DateTime start, DateTime end) {
        List<String> times = new ArrayList<>();
        int startTime = Integer.valueOf(start.hourOfDay().getAsString());
        int startTimeMin = Integer.valueOf(start.minuteOfHour().getAsString());
        int endTime = Integer.valueOf(end.hourOfDay().getAsString());
        boolean shouldSkipNext00 = false;

        int j = startTime +delayTime;
        boolean isStartHalfHour = hasStartHalfHour;

        for (int i = startTime; i <= endTime; i++) {
            if(i >= j && (i <= defaultEndHour && i >= defaultStartHour)) {

                    if(i==j && startTimeMin <=30){
                        isStartHalfHour = false;
                       continue;
                    }else if (i==j && startTimeMin > 30){
                        isStartHalfHour = false;
                        shouldSkipNext00 = true;
                        continue;
                    }

                        if(!shouldSkipNext00){
                            if(!isStartHalfHour)
                                times.add(convert24Clockto12Clock(i) + ":00" + (i < 12 ? "AM" : "PM"));
                        }

                        shouldSkipNext00 = false;


                        if (!(i == endTime)){
                            times.add(convert24Clockto12Clock(i) + ":30" + (i < 12 ? "AM" : "PM"));
                        }

                        if (i == endTime && hasEndHalfHour){
                            times.add(convert24Clockto12Clock(i) + ":30" + (i < 12 ? "AM" : "PM"));
                        }

                        isStartHalfHour = false;
                }
        }
        return times;
    }

    /**
     * Set listener when a date is scrolled
     */
    private void setDateWheelPickerListener(){
        wpDatePicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
               compareCurrentDateWithList(position);
            }
        });
    }


    /**
     * Compare the selected date and current date to limit the hour
     */
    private void compareCurrentDateWithList(int position){
        DateTime currentDateTime = new DateTime();
        checkSelectedDate(datesBetween.get(position),currentDateTime);
    }


    /**
     * Method to limit if the selected date is current date
     */
    private void checkSelectedDate(DateTime selectedDate, DateTime currentDate){
        if((selectedDate.getYear() == currentDate.getYear())
                && (selectedDate.getMonthOfYear() == currentDate.getMonthOfYear())
                && (selectedDate.getDayOfMonth() == currentDate.getDayOfMonth())){
            setTimeWheelPickerForToday(currentDate,new DateTime().withHourOfDay(defaultEndHour));
        }else{
            setTimeWheelPicker();
        }
    }

    private void setTimeWheelPickerForToday(DateTime start, DateTime end){
        List<String> timesBetween = getTimeRangeForCurrentDate(start, end);

        if (timesBetween.size() == 0) {
            timesBetween = getTimeRange(defaultStartHour,defaultEndHour);
        }

        timeSlots.clear();
        timeSlots.addAll(timesBetween);
        wpTimePicker.setData(timeSlots);
    }

    private void setTimeWheelPicker(){
        List<String> timesBetween = getTimeRange(defaultStartHour,defaultEndHour);

        timeSlots.clear();
        timeSlots.addAll(timesBetween);
        wpTimePicker.setData(timeSlots);
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