package com.kaodim.design.components.editText

import android.databinding.BindingAdapter
import android.databinding.InverseBindingAdapter
import android.databinding.InverseBindingListener
import android.text.Editable
import android.text.TextWatcher

object KaodimPhoneNumberEditTextBinding {
    @BindingAdapter(value = ["android:textAttrChanged"])
    fun setListener(layout: KaodimPhoneNumberEditText, textAttrChanged: InverseBindingListener?) {
        if (textAttrChanged != null) {
            layout.inputEditText!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(editable: Editable) {
                    textAttrChanged.onChange()
                }
            })
        }
    }

    @BindingAdapter("android:text")
    fun setText(view: KaodimPhoneNumberEditText, value: String?) {
        if (value != null && value != view.text) view.text = value
    }

    @BindingAdapter(value = ["app:textButtonOnClick"])
    fun setTextButtonOnClick(view: KaodimPhoneNumberEditText, onClick: Unit) {
        view.textButtonOnClick = onClick
    }

    @InverseBindingAdapter(attribute = "android:text")
    fun getText(view: KaodimPhoneNumberEditText): String? {
        return view.text
    }
}