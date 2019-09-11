package com.kaodim.design.components.informationBars

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.kaodim.design.R
import kotlinx.android.synthetic.main.kdl_blue_info_bar.view.*

class BlueInfoBar: LinearLayout {
    constructor(ctx: Context) : super(ctx) {
        init(ctx, null)
    }

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {
        init(ctx, null)
    }

    constructor(ctx: Context, attrs: AttributeSet, defStyleAttr: Int) : super(ctx, attrs, defStyleAttr) {
        init(ctx, null)
    }

    constructor(ctx: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(ctx, attrs, defStyleAttr, defStyleRes) {
        init(ctx, null)
    }

    private lateinit var primaryText: TextView

    private fun init(context: Context, atts: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.kdl_blue_info_bar, this)
        primaryText = findViewById(R.id.tvPrimaryText)
    }

    fun setPrimaryText(primaryText: String) {
        this.primaryText.text = primaryText
    }

    fun getPrimaryText(primaryText: String): TextView {
        return tvPrimaryText
    }
}