package com.kaodim.design.components.informationBars

import android.content.Context
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewTreeObserver
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
        val barColor = typedArray.getColor(R.styleable.InfoBar_barColor, ContextCompat.getColor(context, R.color.kaodim_blue))
        val backgroundColor = typedArray.getColor(R.styleable.InfoBar_backgroundColor, ContextCompat.getColor(context, R.color.kaodim_blue_10))
        val textColor = typedArray.getColor(R.styleable.InfoBar_textColor, ContextCompat.getColor(context, R.color.kaodim_blue_text))
        typedArray.recycle()

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.kdl_info_bar, this)
        initComponent(primaryString, barColor, backgroundColor, textColor)
        this.setEvents()
    }

    private fun setEvents() {
        this.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (height > 0) {

                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        })
    }

    private fun initComponent(primaryString: String?,
                              @ColorInt barColor: Int,
                              @ColorInt backgroundColor: Int,
                              @ColorInt textColor: Int  ) {

        primaryString?.let { setPrimaryText(it) }

        this.setBarColor(barColor)
        this.setPrimaryTextViewBackgroundColor(backgroundColor)
        this.setPrimaryTextColor(textColor)

    }

    fun setPrimaryText(primaryText: String) {
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
        tvPrimaryText.setBackgroundColor(color)
    }
}