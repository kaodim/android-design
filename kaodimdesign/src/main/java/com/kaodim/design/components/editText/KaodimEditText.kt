package com.kaodim.design.components.editText

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.*
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.kaodim.design.R
import com.kaodim.design.components.utilities.ViewUtils
import org.w3c.dom.Text

class KaodimEditText : LinearLayout {

    var inputEditText: EditText? = null
    private var inputType: Int = 0
    private var iconDrawableId: Int = 0
    private var isSecured: Boolean = false
    private var isFirstLetterCapitalize: Boolean = false
    private var isDropdown: Boolean = false
    private var tvCustomHint: TextView? = null
    private var tvCustomError: TextView? = null
    private var ivIcon: ImageView? = null
    private var ivKdlTextInputError: ImageView? = null
    private var ivKdlTextInputClear: ImageView? = null
    private var ivKdlTextInputShowPassword: ImageView? = null
    var tvTextButton: TextView? = null
    private var textButtonText: String? = null

    private var hasTextButton = false

    var clKdlTextInputInput: ConstraintLayout? = null

    private val hintLateralTranslation: Float
        get() {
            val width = tvCustomHint!!.width.toFloat()
            return -((width - HINT_SHRINK_SCALE * width) * HALF)
        }

    private val hintLongitudinalTranslation: Float
        get() {
            val paddingTop = resources.getDimensionPixelSize(R.dimen.size_8_dp)
            val editTextHeight = if(inputType == INPUT_TYPE_MULTI_LINE_TEXT) inputEditText!!.height.toFloat()*HINT_SHRINK_SCALE else inputEditText?.height?.toFloat()
            val hintHeight = tvCustomHint!!.height.toFloat()
            if (editTextHeight != null) {
                return -((editTextHeight - hintHeight) * HALF - paddingTop)
            }
            return 0f
        }

    var errorText: String?
        get() = this.tvCustomError!!.text.toString()
        set(errorText) {
            tvCustomError!!.text = errorText
        }

    var text: String?
        get() = this.inputEditText!!.text.toString()
        set(text) = setTextOnFocusAnimation(text)

    var secured: Boolean
        get() = this.isSecured
        set(secured) {
            this.isSecured = secured
            inputEditText!!.inputType = getInputType(inputType, isSecured, inputEditText!!.isFocused, isFirstLetterCapitalize)
        }

    constructor(context: Context) : super(context) {
        this.initComponents()
        this.setEvents(isDropdown)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun setTextOnFocusAnimation(text: String?) {
        inputEditText!!.setText(text)

        if(!text.isNullOrEmpty()){
            setHasTextConstraint(true)
        }

    }

    private fun init(context: Context, attrs: AttributeSet) {
        //Retrieve the custom attributes from XML
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.KaodimEditTextLayout)
        val hintText = typedArray.getString(R.styleable.KaodimEditTextLayout_android_hint)
        val errorText = typedArray.getString(R.styleable.KaodimEditTextLayout_errorText)
        val inputText = typedArray.getString(R.styleable.KaodimEditTextLayout_android_text)
        val enabled = typedArray.getBoolean(R.styleable.KaodimEditTextLayout_android_enabled, true)
        inputType = typedArray.getInt(R.styleable.KaodimEditTextLayout_inputType, 0)
        isSecured = typedArray.getBoolean(R.styleable.KaodimEditTextLayout_secured, false)
        iconDrawableId = typedArray.getResourceId(R.styleable.KaodimEditTextLayout_iconDrawable, 0)
        isFirstLetterCapitalize = typedArray.getBoolean(R.styleable.KaodimEditTextLayout_capitalize, false)
        isDropdown= typedArray.getBoolean(R.styleable.KaodimEditTextLayout_dropdown, false)
        textButtonText = typedArray.getString(R.styleable.KaodimEditTextLayout_etTextButtonText)
        setHasTextButtonText()
        //Recycle the TypedArray (saves memory)
        typedArray.recycle()

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.kdl_edit_text_layout, this)
        initComponents()
        this.setEvents(isDropdown)
        this.setupView(hintText, errorText, inputText, enabled, isDropdown)
    }

    private fun setHasTextButtonText() {
        if (textButtonText.isNullOrEmpty().not()) {
            hasTextButton = true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setEvents(isDropdown: Boolean) {
        this.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (height > 0) {
                    setPadding(!TextUtils.isEmpty(inputEditText!!.text))
                    tvCustomHint?.scaleX = if (!TextUtils.isEmpty(inputEditText!!.text) || inputEditText!!.isFocused) HINT_SHRINK_SCALE else 1f
                    tvCustomHint?.scaleY = if (!TextUtils.isEmpty(inputEditText!!.text) || inputEditText!!.isFocused) HINT_SHRINK_SCALE else 1f
                    tvCustomHint?.translationX = if (!TextUtils.isEmpty(inputEditText!!.text) || inputEditText!!.isFocused) hintLateralTranslation else 0f
                    tvCustomHint?.translationY = if (!TextUtils.isEmpty(inputEditText!!.text) || inputEditText!!.isFocused) hintLongitudinalTranslation else 0f
                    if (!isEnabled) {
                        inputEditText!!.setTextColor(ContextCompat.getColor(context, R.color.text_lightgrey))
                        tvCustomHint!!.setTextColor(ContextCompat.getColor(context, if (!TextUtils.isEmpty(inputEditText!!.text)) R.color.text_midgrey else R.color.text_lightgrey))
                    } else {
                        inputEditText!!.setTextColor(ContextCompat.getColor(context, R.color.text_black))
                        tvCustomHint!!.setTextColor(ContextCompat.getColor(context, if (!TextUtils.isEmpty(inputEditText!!.text) && !inputEditText!!.isFocused) R.color.text_lightgrey else R.color.text_midgrey))
                    }
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        })

        inputEditText!!.onFocusChangeListener = OnFocusChangeListener { view, hasFocus ->
            post {
                val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (hasFocus) {
                    inputMethodManager.showSoftInput(inputEditText, InputMethodManager.SHOW_IMPLICIT)
                } else {
                    inputMethodManager.hideSoftInputFromWindow(inputEditText!!.windowToken, 0)
                }
            }
            inputEditText?.inputType = getInputType(inputType, isSecured, hasFocus, isFirstLetterCapitalize)
            inputEditText?.setSelection(inputEditText!!.text.length)
            // Update background
            val drawable = context.getDrawable(if (errorText!!.isEmpty()) if (inputEditText!!.isFocused) R.drawable.bg_edittext_focused else R.drawable.bg_edittext_default else R.drawable.bg_edittext_error)
            clKdlTextInputInput?.background = drawable
            ivKdlTextInputError?.visibility = if (!hasFocus && errorText!!.isNotEmpty()) View.VISIBLE else View.GONE
            ivKdlTextInputClear?.visibility = if (hasFocus && text!!.isNotEmpty() && inputType != INPUT_TYPE_MULTI_LINE_TEXT && inputType != INPUT_TYPE_PASSWORD && !hasTextButton) View.VISIBLE else View.GONE
            setPadding(hasFocus || !TextUtils.isEmpty(inputEditText!!.text))
            setHasTextConstraint(hasFocus || !TextUtils.isEmpty(inputEditText!!.text))
            animateColorHint(hasFocus || TextUtils.isEmpty(inputEditText!!.text))
            animateScaleHint(!hasFocus && TextUtils.isEmpty(inputEditText!!.text))
        }

        inputEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (height > 0) {
                    if (inputType != INPUT_TYPE_MULTI_LINE_TEXT) {
                        val hasFocus = inputEditText!!.hasFocus()
                        val isEmpty = TextUtils.isEmpty(s.toString())
                        setPadding(hasFocus || !isEmpty)
                        setHasTextConstraint(hasFocus || !isEmpty)
                        animateColorHint(hasFocus || isEmpty)
                        animateScaleHint(!hasFocus && isEmpty)
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {
                val text = s.toString()
                ivKdlTextInputError?.visibility = if (!inputEditText!!.isFocused && errorText!!.isNotEmpty()) View.VISIBLE else View.GONE
                ivKdlTextInputClear?.visibility = if (inputEditText!!.isFocused && text.isNotEmpty() && inputType != INPUT_TYPE_MULTI_LINE_TEXT && inputType != INPUT_TYPE_PASSWORD && !hasTextButton) View.VISIBLE else View.GONE
                
                if (inputType == INPUT_TYPE_PASSWORD) {
                    if (s.toString().isNotEmpty()) {
                        ivKdlTextInputShowPassword?.visibility = View.VISIBLE
                    } else {
                        ivKdlTextInputShowPassword?.visibility = View.GONE
                    }
                }
            }
        })


        inputEditText?.setOnTouchListener(object : OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (inputEditText!!.hasFocus()){
                    v?.parent?.requestDisallowInterceptTouchEvent(true)
                    when(event!!.action and MotionEvent.ACTION_MASK){
                        MotionEvent.ACTION_SCROLL -> { v?.parent?.requestDisallowInterceptTouchEvent(false); return true }
                    }
                }
                return false
            }

        })

        tvCustomError?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                val text = s.toString()

                // Update background
                val drawable = context.getDrawable(if (text.isEmpty()) if (inputEditText!!.isFocused) R.drawable.bg_edittext_focused else R.drawable.bg_edittext_default else R.drawable.bg_edittext_error)
                clKdlTextInputInput!!.background = drawable

                // Update visibility
                tvCustomError!!.visibility = if (text.isEmpty()) View.GONE else View.VISIBLE

                ivKdlTextInputError!!.visibility = if (!inputEditText!!.isFocused && !text.isEmpty()) View.VISIBLE else View.GONE
                ivKdlTextInputClear!!.visibility = if (inputEditText!!.isFocused && !text.isEmpty() && inputType != INPUT_TYPE_MULTI_LINE_TEXT && inputType != INPUT_TYPE_PASSWORD && !hasTextButton) View.VISIBLE else View.GONE
            }
        })

        ivKdlTextInputClear?.setOnClickListener { text = "" }
    }

    private fun initComponents() {
        inputEditText = findViewById(R.id.etKdlTextInputInput)
        tvCustomHint = findViewById(R.id.tvKdlTextInputHint)
        tvCustomError = findViewById(R.id.tvKdlTextInputError)
        ivIcon = findViewById(R.id.ivIcon)
        ivKdlTextInputClear = findViewById(R.id.ivKdlTextInputClear)
        ivKdlTextInputError = findViewById(R.id.ivKdlTextInputError)
        clKdlTextInputInput = findViewById(R.id.clKdlTextInputInput)
        ivKdlTextInputShowPassword = findViewById(R.id.ivKdlTextInputShowPassword)
        tvTextButton = findViewById(R.id.tvTextButton)
    }

    private fun setupView(hintText: String?, errorText: String?, inputText: String?, enabled: Boolean, isDropdown: Boolean) {
        setIcon()
        this.setHint(hintText)
        this.errorText = errorText
        this.text = inputText
        this.isEnabled = enabled
        inputEditText?.showSoftInputOnFocus = true
        inputEditText?.inputType = getInputType(inputType, isSecured, inputEditText!!.isFocused, isFirstLetterCapitalize)


        if(!inputText.isNullOrEmpty()){
            setHasTextConstraint(true)
            setPadding(true)
        }

        if (isDropdown) {
            inputEditText?.isFocusable = false
            inputEditText?.isEnabled = true
            inputEditText?.isLongClickable = false
            setDropdownIcon()
        }

        if (textButtonText.isNullOrEmpty().not()) {
            tvTextButton?.visibility = View.VISIBLE
            tvTextButton?.text = textButtonText
            ivKdlTextInputShowPassword?.visibility = View.GONE
            ivKdlTextInputError?.visibility = View.GONE
            ivKdlTextInputClear?.visibility = View.GONE
        }
    }

    fun setTextButtonListener(listener: OnClickListener) {
        tvTextButton?.setOnClickListener(listener)
    }

    private fun setIcon() {
        if(iconDrawableId != 0) {
            val drawable = context.getDrawable(iconDrawableId)
            ivIcon?.background = drawable
            ivIcon?.visibility = View.VISIBLE
        }
    }

    private fun animateColorHint(hasFocus: Boolean) {
        val start = tvCustomHint!!.currentTextColor
        val end = ContextCompat.getColor(context, if (hasFocus) R.color.text_midgrey else R.color.text_lightgrey)
        val animator = ValueAnimator.ofObject(ArgbEvaluator(), start, end)
        animator.duration = HINT_ANIMATION_DURATION
        animator.addUpdateListener { valueAnimator -> tvCustomHint!!.setTextColor(valueAnimator.animatedValue as Int) }
        animator.start()
    }

    private fun animateScaleHint(grow: Boolean) {
        val scale = if (grow) 1f else HINT_SHRINK_SCALE
        val translationX = if (grow) 0f else hintLateralTranslation
        val translationY = if (grow) 0f else hintLongitudinalTranslation

        tvCustomHint!!.animate()
                .scaleX(scale)
                .scaleY(scale)
                .translationX(translationX)
                .translationY(translationY)
                .setDuration(HINT_ANIMATION_DURATION)
                .start()
    }

    private fun setPadding(isFocused: Boolean) {
        val paddingBottom = resources.getDimensionPixelSize(if (isFocused) R.dimen.size_10_dp else R.dimen.size_20_dp)
        val paddingTop = resources.getDimensionPixelSize(if (isFocused) R.dimen.size_30_dp else R.dimen.size_20_dp)
        val multiLinePaddingTop = resources.getDimensionPixelSize(if (isFocused) R.dimen.size_5_dp else R.dimen.size_20_dp)
        val multiLinePaddingBottom = resources.getDimensionPixelSize(if (isFocused) R.dimen.size_10_dp else R.dimen.size_20_dp)
        val hintPadding = resources.getDimensionPixelSize(if (isFocused) R.dimen.size_20_dp else R.dimen.size_0_dp)

        if (inputType != INPUT_TYPE_MULTI_LINE_TEXT) {
            inputEditText?.setPadding(resources.getDimensionPixelSize( R.dimen.size_10_dp) , paddingTop, resources.getDimensionPixelSize( R.dimen.size_10_dp), paddingBottom)
        } else {
            if (isFocused) {
                inputEditText?.setPadding(resources.getDimensionPixelSize( R.dimen.size_10_dp), -multiLinePaddingTop, resources.getDimensionPixelSize( R.dimen.size_10_dp), multiLinePaddingBottom)
            } else {
                inputEditText?.setPadding(resources.getDimensionPixelSize( R.dimen.size_10_dp), multiLinePaddingTop, resources.getDimensionPixelSize( R.dimen.size_10_dp), multiLinePaddingBottom)
            }
            tvCustomHint?.setPadding(resources.getDimensionPixelSize( R.dimen.size_10_dp), hintPadding, resources.getDimensionPixelSize( R.dimen.size_10_dp), 0)
        }
    }

    private fun setHasTextConstraint(hasText: Boolean?) {
        if (inputType == INPUT_TYPE_MULTI_LINE_TEXT) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(clKdlTextInputInput!!)

            if (hasText!!) {
                constraintSet.connect(R.id.ivIcon, ConstraintSet.END, R.id.tvKdlTextInputHint, ConstraintSet.START)
                constraintSet.connect(R.id.tvKdlTextInputHint, ConstraintSet.BOTTOM, R.id.etKdlTextInputInput, ConstraintSet.TOP, 0)
                constraintSet.connect(R.id.etKdlTextInputInput, ConstraintSet.TOP, R.id.tvKdlTextInputHint, ConstraintSet.BOTTOM, 0)
            } else {
                constraintSet.connect(R.id.tvKdlTextInputHint, ConstraintSet.TOP, R.id.clKdlTextInputInput, ConstraintSet.TOP, 0)
                constraintSet.connect(R.id.tvKdlTextInputHint, ConstraintSet.BOTTOM, R.id.etKdlTextInputInput, ConstraintSet.BOTTOM, 0)
                constraintSet.connect(R.id.etKdlTextInputInput, ConstraintSet.TOP, R.id.clKdlTextInputInput, ConstraintSet.TOP, 0)
            }
            constraintSet.applyTo(clKdlTextInputInput!!)
        }
    }


    fun setHint(hint: String?) {
        tvCustomHint?.text = hint
    }

    override fun setEnabled(enabled: Boolean) {

        if (!enabled) {
            inputEditText!!.setTextColor(ContextCompat.getColor(context, R.color.text_lightgrey))
            tvCustomHint!!.setTextColor(ContextCompat.getColor(context, if (!TextUtils.isEmpty(inputEditText!!.text)) R.color.text_midgrey else R.color.text_lightgrey))
        } else {
            inputEditText!!.setTextColor(ContextCompat.getColor(context, R.color.text_black))
            tvCustomHint!!.setTextColor(ContextCompat.getColor(context, if (!TextUtils.isEmpty(inputEditText!!.text) && !inputEditText!!.isFocused) R.color.text_lightgrey else R.color.text_midgrey))
        }

        ViewUtils.recursiveSetEnabled(this, enabled)
        super.setEnabled(enabled)
        tvTextButton?.isEnabled = true
    }

    fun addTextChangedListener(textWatcher: TextWatcher) {
        inputEditText!!.addTextChangedListener(textWatcher)
    }

    private fun getInputType(customInputType: Int, isSecured: Boolean, isFocused: Boolean, isCapitalized: Boolean): Int {
        var inputType = InputType.TYPE_CLASS_TEXT
        if (customInputType == INPUT_TYPE_NUMBER) {
            inputType = InputType.TYPE_CLASS_NUMBER
            if (isSecured && !isFocused) {
                inputType = inputType or InputType.TYPE_NUMBER_VARIATION_PASSWORD
            }
        } else if (customInputType == INPUT_TYPE_MULTI_LINE_TEXT) {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            inputEditText?.inputType = inputType
            inputEditText?.gravity = Gravity.TOP or Gravity.START
            inputEditText?.setSingleLine(false)
            inputEditText?.maxLines = 4
        } else if (isSecured && customInputType == INPUT_TYPE_PASSWORD) {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            if (textButtonText.isNullOrEmpty()) {
                ivKdlTextInputShowPassword?.visibility = View.VISIBLE
                ivKdlTextInputShowPassword?.setOnClickListener { setShowHidePassword() }
            }
        } else {
            if (isCapitalized) {
                inputType = inputType or InputType.TYPE_TEXT_FLAG_CAP_WORDS
            }
            if (isSecured && !isFocused) {
                inputType = inputType or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
        return inputType
    }

    private fun setShowHidePassword() {
        val selectionStart: Int
        val selectionEnd: Int

        if(inputEditText?.transformationMethod?.equals(PasswordTransformationMethod.getInstance())!!){
            selectionStart = inputEditText?.selectionStart!!
            selectionEnd = inputEditText?.selectionEnd!!

            ivKdlTextInputShowPassword?.setImageResource(R.drawable.icon_mini_hidepassword);

            //Show Password
            inputEditText?.transformationMethod = HideReturnsTransformationMethod.getInstance();
            inputEditText?.setSelection(selectionStart, selectionEnd)
        }
        else{
            selectionStart = inputEditText?.selectionStart!!
            selectionEnd = inputEditText?.selectionEnd!!


            ivKdlTextInputShowPassword?.setImageResource(R.drawable.icon_mini_showpassword);

            //Hide Password
            inputEditText?.transformationMethod = PasswordTransformationMethod.getInstance();
            inputEditText?.setSelection(selectionStart, selectionEnd)
        }
    }

    private fun setDropdownIcon() {
        ivKdlTextInputShowPassword?.visibility = View.VISIBLE
        ivKdlTextInputShowPassword?.setImageResource(R.drawable.icon_chevron_down);
    }

    companion object {

        private const val HINT_ANIMATION_DURATION: Long = 200
        private const val HINT_SHRINK_SCALE = 0.75f
        private const val HALF = 0.5f

        private const val INPUT_TYPE_TEXT = 0
        private const val INPUT_TYPE_NUMBER = 1
        private const val INPUT_TYPE_MULTI_LINE_TEXT = 2
        private const val INPUT_TYPE_PASSWORD = 3
    }
}
