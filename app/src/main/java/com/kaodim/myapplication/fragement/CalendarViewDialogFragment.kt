package com.kaodim.myapplication.fragement


import android.databinding.adapters.TextViewBindingAdapter.setTextSize
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.*
import android.widget.TextView
import com.kaodim.design.components.calendarView.model.CalendarDay
import com.kaodim.design.components.calendarView.model.CalendarMonth
import com.kaodim.design.components.calendarView.model.DayOwner
import com.kaodim.design.components.calendarView.ui.DayBinder
import com.kaodim.design.components.calendarView.ui.MonthHeaderFooterBinder
import com.kaodim.design.components.calendarView.ui.ViewContainer
import com.kaodim.myapplication.R
import kotlinx.android.synthetic.main.calendar_day_legend.*
import kotlinx.android.synthetic.main.example_4_calendar_day.view.*
import kotlinx.android.synthetic.main.example_4_calendar_header.view.*
import kotlinx.android.synthetic.main.exmaple_4_fragment.*
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter

class CalendarViewDialogFragment : BottomSheetDialogFragment() {

//    override val toolbar: Toolbar?
//        get() = exFourToolbar
//
//    override val titleRes: Int? = null

    private val today = LocalDate.now()

    private var startDate: LocalDate? = null
    private var endDate: LocalDate? = null

    private val headerDateFormatter = DateTimeFormatter.ofPattern("EEE'\n'd MMM")

    private val startBackground: GradientDrawable by lazy {
        return@lazy requireContext().getDrawableCompat(R.drawable.example_4_continuous_selected_bg_start)!! as GradientDrawable
    }

    private val endBackground: GradientDrawable by lazy {
        return@lazy requireContext().getDrawableCompat(R.drawable.example_4_continuous_selected_bg_end)!! as GradientDrawable
    }

    private var radiusUpdated = false

    /**
     * We set the radius of the continuous selection background drawable dynamically
     * since the view size is `match parent` hence we cannot determine the appropriate
     * radius value which would equal half of the view's size beforehand.
     */
    private fun updateDrawableRadius(textView: TextView) {
        if (radiusUpdated) return
        radiusUpdated = true
        val radius = (textView.height / 2).toFloat()
        startBackground.setCornerRadius(topLeft = radius, bottomLeft = radius)
        endBackground.setCornerRadius(topRight = radius, bottomRight = radius)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.exmaple_4_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the First day of week depending on Locale
        val daysOfWeek = daysOfWeekFromLocale()
//        legendLayout.children.forEachIndexed { index, view ->
//            (view as TextView).apply {
//                text = daysOfWeek[index].name.take(3).toLowerCase().capitalize()
//                setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
//                setTextColorRes(R.color.kaodim_blue)
//            }
//        }

        val currentMonth = YearMonth.now()
        exFourCalendar.setup(currentMonth, currentMonth.plusMonths(12), daysOfWeek.first())
        exFourCalendar.scrollToMonth(currentMonth)


        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val textView = view.exFourDayText
            val roundBgView = view.exFourRoundBgView

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH && (day.date == today || day.date.isAfter(today))) {
                        val date = day.date
                        if (startDate != null) {
                            if (date < startDate || endDate != null) {
                                startDate = date
                                endDate = null
                            } else if (date != startDate) {
                                endDate = date
                            }
                        } else {
                            startDate = date
                        }
                        exFourCalendar.notifyCalendarChanged()
                        bindSummaryViews()
                    }
                }
            }
        }
        exFourCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                val roundBgView = container.roundBgView

                textView.text = null
                textView.background = null
                roundBgView.makeInVisible()

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.text = day.day.toString()

                    if (day.date.isBefore(today)) {
                        textView.setTextColorRes(R.color.kaodim_blue)
                    } else {
                        when {
                            startDate == day.date && endDate == null -> {
                                textView.setTextColorRes(R.color.white)
                                roundBgView.makeVisible()
                                roundBgView.setBackgroundResource(R.drawable.example_4_single_selected_bg)
                            }
                            day.date == startDate -> {
                                textView.setTextColorRes(R.color.white)
                                updateDrawableRadius(textView)
                                textView.background = startBackground
                            }
                            startDate != null && endDate != null && (day.date > startDate && day.date < endDate) -> {
                                textView.setTextColorRes(R.color.white)
                                textView.setBackgroundResource(R.drawable.example_4_continuous_selected_bg_middle)
                            }
                            day.date == endDate -> {
                                textView.setTextColorRes(R.color.white)
                                updateDrawableRadius(textView)
                                textView.background = endBackground
                            }
                            day.date == today -> {
                                textView.setTextColorRes(R.color.kaodim_blue)
                                roundBgView.makeVisible()
                                roundBgView.setBackgroundResource(R.drawable.example_4_today_bg)
                            }
                            else -> textView.setTextColorRes(R.color.kaodim_blue)
                        }
                    }
                } else {

                    // This part is to make the coloured selection background continuous
                    // on the blank in and out dates across various months and also on dates(months)
                    // between the start and end dates if the selection spans across multiple months.

                    val startDate = startDate
                    val endDate = endDate
                    if (startDate != null && endDate != null) {
                        // Mimic selection of inDates that are less than the startDate.
                        // Example: When 26 Feb 2019 is startDate and 5 Mar 2019 is endDate,
                        // this makes the inDates in Mar 2019 for 24 & 25 Feb 2019 look selected.
                        if ((day.owner == DayOwner.PREVIOUS_MONTH
                                    && startDate.monthValue == day.date.monthValue
                                    && endDate.monthValue != day.date.monthValue) ||
                            // Mimic selection of outDates that are greater than the endDate.
                            // Example: When 25 Apr 2019 is startDate and 2 May 2019 is endDate,
                            // this makes the outDates in Apr 2019 for 3 & 4 May 2019 look selected.
                            (day.owner == DayOwner.NEXT_MONTH
                                    && startDate.monthValue != day.date.monthValue
                                    && endDate.monthValue == day.date.monthValue) ||

                            // Mimic selection of in and out dates of intermediate
                            // months if the selection spans across multiple months.
                            (startDate < day.date && endDate > day.date
                                    && startDate.monthValue != day.date.monthValue
                                    && endDate.monthValue != day.date.monthValue)
                        ) {
                            textView.setBackgroundResource(R.drawable.example_4_continuous_selected_bg_middle)
                        }
                    }
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = view.exFourHeaderText
        }
        exFourCalendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                val monthTitle = "${month.yearMonth.month.name.toLowerCase().capitalize()} ${month.year}"
                container.textView.text = monthTitle
            }
        }

//        exFourSaveButton.setOnClickListener click@{
//            val startDate = startDate
//            val endDate = endDate
//            if (startDate != null && endDate != null) {
//                val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
//                val text = "Selected: ${formatter.format(startDate)} - ${formatter.format(endDate)}"
//                Snackbar.make(this.view!!, text, Snackbar.LENGTH_LONG).show()
//            } else {
//                Snackbar.make(this.view!!, "No selection. Searching all Airbnb listings.", Snackbar.LENGTH_LONG)
//                    .show()
//            }
//            fragmentManager?.popBackStack()
//        }

        bindSummaryViews()
    }

    private fun bindSummaryViews() {
        if (startDate != null) {
            exFourStartDateText.text = headerDateFormatter.format(startDate)
            exFourStartDateText.setTextColorRes(R.color.kdl_grey)
        } else {
            exFourStartDateText.text = "Start Date"
            exFourStartDateText.setTextColor(Color.GRAY)
        }
        if (endDate != null) {
            exFourEndDateText.text = headerDateFormatter.format(endDate)
            exFourEndDateText.setTextColorRes(R.color.kdl_grey)
        } else {
            exFourEndDateText.text = "End Date"
            exFourEndDateText.setTextColor(Color.GRAY)
        }

        // Enable save button if a range is selected or no date is selected at all, Airbnb style.
//        exFourSaveButton.isEnabled = endDate != null || (startDate == null && endDate == null)
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.example_4_menu, menu)
//        exFourToolbar.post {
//            // Configure menu text to match what is in the Airbnb app.
//            exFourToolbar.findViewById<TextView>(R.id.menuItemClear).apply {
//                setTextColor(requireContext().getColorCompat(R.color.example_4_grey))
//                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
//                isAllCaps = false
//            }
//        }
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.menuItemClear) {
//            startDate = null
//            endDate = null
//            exFourCalendar.notifyCalendarChanged()
//            bindSummaryViews()
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }

    override fun onStart() {
        super.onStart()
        val closeIndicator = requireContext().getDrawableCompat(R.drawable.kdl_ic_close)?.apply {
            setColorFilter(requireContext().getColorCompat(R.color.kdl_grey), PorterDuff.Mode.SRC_ATOP)
        }
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(closeIndicator)
        requireActivity().window.apply {
            // Update statusbar color to match toolbar color.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                statusBarColor = requireContext().getColorCompat(R.color.white)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                statusBarColor = Color.GRAY
            }
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.apply {
            // Reset statusbar color.
            statusBarColor = requireContext().getColorCompat(R.color.colorPrimaryDark)
            decorView.systemUiVisibility = 0
        }
    }
}
