package com.kaodim.design.components.informationBars

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.kaodim.design.R
import kotlinx.android.synthetic.main.kdl_info_bar.view.*

class InfoBar: RelativeLayout {
    constructor(ctx: Context) : super(ctx) {
        init(ctx, null)
    }

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {
        init(ctx, attrs)
    }

    constructor(ctx: Context, attrs: AttributeSet, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr) {
        init(ctx, attrs)
    }

    constructor(ctx: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(ctx, attrs, defStyleAttr, defStyleRes) {
        init(ctx, attrs)
    }

    private var primaryString: String? = null

    private fun init(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InfoBar)
        val primaryString = typedArray.getString(R.styleable.InfoBar_primaryText)
        val navigationString = typedArray.getString(R.styleable.InfoBar_navigationText)

        val barColor = typedArray.getColor(R.styleable.InfoBar_barColor, ContextCompat.getColor(context, R.color.kaodim_blue))
        val backgroundColor = typedArray.getColor(R.styleable.InfoBar_backgroundColor, ContextCompat.getColor(context, R.color.kaodim_blue_10))
        val textColor = typedArray.getColor(R.styleable.InfoBar_textColor, ContextCompat.getColor(context, R.color.kaodim_blue_text))
        val navigationTextColor = typedArray.getColor(R.styleable.InfoBar_navigationColor, ContextCompat.getColor(context, R.color.kaodim_blue))

        val src = typedArray.getDrawable(R.styleable.InfoBar_android_src)

        typedArray.recycle()

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.kdl_info_bar, this)
        initComponent(primaryString, navigationString, barColor, backgroundColor, textColor, navigationTextColor, src)
        this.setEvents()
    }

    private fun setEvents() {
//        this.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                if (height > 0) {
//
//                    viewTreeObserver.removeOnGlobalLayoutListener(this)
//                }
//            }
//        })
    }

    private fun initComponent(primaryString: String?,
                              navigationString: String?,
                              @ColorInt barColor: Int,
                              @ColorInt backgroundColor: Int,
                              @ColorInt textColor: Int,
                              @ColorInt navigationColor: Int,
                              src: Drawable?) {

        primaryString?.let { setPrimaryText(it) }
        this.setNavigationText(navigationString)
        this.setBarColor(barColor)
        this.setNavigationColor(navigationColor)
        this.setPrimaryTextViewBackgroundColor(backgroundColor)
        this.setPrimaryTextColor(textColor)
        this.setIcon(src)

    }

    fun setIcon(drawable: Drawable?) {
        if (drawable != null) {
            ivInfoBarIcon.visibility = View.VISIBLE
            ivInfoBarIcon.setImageDrawable(drawable)
        } else {
            ivInfoBarIcon.visibility = View.GONE
        }
    }

    fun setNavigationText(navigationText: String?) {
        tvInfoNavigation.text = navigationText
        llInfoNavigation.visibility = if (navigationText.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    fun setNavigationColor(@ColorInt color: Int) {
        tvInfoNavigation.setTextColor(color)
        ivInfoNavigation.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }


    fun setPrimaryText(primaryText: String) {
        tvPrimaryText.text = primaryText
    }

    fun setPrimaryText(primaryText: SpannableString) {
        tvPrimaryText.text = primaryText
    }

    fun getPrimaryTextView(): TextView {
        return tvPrimaryText
    }

    fun setBarColor(@ColorInt color: Int) {
        vInfoBarBar.setBackgroundColor(color)
    }

    fun setPrimaryTextColor(@ColorInt color: Int) {
        tvPrimaryText.setTextColor(color)
    }

    fun setPrimaryTextViewBackgroundColor(@ColorInt color: Int) {
        llInfoBarBase.setBackgroundColor(color)
    }

    fun setLayoutPadding(left: Int, top: Int, right: Int, bottom: Int) {
            val left_dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, left.toFloat(), llInfoBarBase.context.resources.displayMetrics).toInt()
            val right_dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, right.toFloat(), llInfoBarBase.context.resources.displayMetrics).toInt()
            val top_dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, top.toFloat(), llInfoBarBase.context.resources.displayMetrics).toInt()
            val bottom_dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, bottom.toFloat(), llInfoBarBase.context.resources.displayMetrics).toInt()
            val layoutParams = llInfoBarBase.layoutParams as ViewGroup.LayoutParams
            llInfoBarBase.setPadding(left_dp, top_dp, right_dp, bottom_dp)
            llInfoBarBase.layoutParams = layoutParams
    }

}