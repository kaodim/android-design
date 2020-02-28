package com.kaodim.design.components.editText

import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.kaodim.design.R

class KaodimPhoneNumberEditText : LinearLayout {
    var inputEditText: EditText? = null
    private var inputType: Int = 0
    private var iconDrawableId: Int = 0
    private var isSecured: Boolean = false
    private var isFirstLetterCapitalize: Boolean = false
    private var tvCustomError: TextView? = null
    private var ivIcon: ImageView? = null
    private var tvCountryPhoneCode: TextView? = null
    var tvTextButton: TextView? = null
    private var userCountry: String? = null
    private var textButtonText: String? = null
    private var mContext: Context? = null

    val MALAYSIA = "Malaysia"
    val SINGAPORE = "Singapore"
    val INDONESIA = "Indonesia"
    val PHILIPPINES = "Philippines"

    var errorText: String?
        get() = this.tvCustomError!!.text.toString()
        set(errorText) {
            tvCustomError!!.text = errorText
        }

    var text: String?
        get() = this.inputEditText!!.text.toString()
        set(text) = inputEditText!!.setText(text)

    var country: String?
        get()  =  userCountry
        set(text) = setFlag(text)

    var textButtonOnClick: Unit
        get() = Unit
        set(listener) = tvTextButton!!.setOnClickListener { listener }

    private fun setFlag(text: String?) {
        userCountry = text
        mContext?.let { setIconAndCountryCode(it) }
    }

    constructor(context: Context) : super(context) {
        this.initComponents()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        mContext = context
        //Retrieve the custom attributes from XML
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.KaodimPhoneNumberEditTextLayout)
        val errorText = typedArray.getString(R.styleable.KaodimPhoneNumberEditTextLayout_errorText)
        val inputText = typedArray.getString(R.styleable.KaodimPhoneNumberEditTextLayout_android_text)
        val enabled = typedArray.getBoolean(R.styleable.KaodimPhoneNumberEditTextLayout_android_enabled, true)
        userCountry = typedArray.getString(R.styleable.KaodimPhoneNumberEditTextLayout_country)
        textButtonText = typedArray.getString(R.styleable.KaodimPhoneNumberEditTextLayout_textButtonText)
        //Recycle the TypedArray (saves memory)
        typedArray.recycle()

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.kdl_phone_number_edit_text_layout, this)
        initComponents()
        this.setupView(errorText, inputText, enabled)
        setIconAndCountryCode(context)
    }

    private fun initComponents() {
        inputEditText = findViewById(R.id.etKdlTextInputInput)
        tvCustomError = findViewById(R.id.tvKdlTextInputError)
        ivIcon = findViewById(R.id.ivIcon)
        tvCountryPhoneCode = findViewById(R.id.tvCountryPhoneCode)
        tvTextButton = findViewById(R.id.tvTextButton)
    }

    private fun setupView(errorText: String?, inputText: String?, enabled: Boolean) {
        this.errorText = errorText
        this.text = inputText
        this.isEnabled = enabled
        inputEditText?.showSoftInputOnFocus = true

        if (textButtonText.isNullOrEmpty().not()) {
            tvTextButton?.visibility = View.VISIBLE
            tvTextButton?.text = textButtonText
        }

    }

    private fun setIconAndCountryCode(context: Context) {
        when (userCountry) {
            MALAYSIA -> {
                ivIcon?.background = context.getDrawable(R.drawable.img_flag_malaysia)
                tvCountryPhoneCode!!.text = context.getString(R.string.malaysia_country_code)
            }
            SINGAPORE -> {
                ivIcon?.background = context.getDrawable(R.drawable.img_flag_singapore)
                tvCountryPhoneCode!!.text = context.getString(R.string.singapore_country_code)
            }
            PHILIPPINES -> {
                ivIcon?.background = context.getDrawable(R.drawable.img_flag_philippines)
                tvCountryPhoneCode!!.text = context.getString(R.string.philippines_country_code)
            }
            INDONESIA -> {
                ivIcon?.background = context.getDrawable(R.drawable.img_flag_indonesia)
                tvCountryPhoneCode!!.text = context.getString(R.string.indonesia_country_code)
            }
        }


    }


}