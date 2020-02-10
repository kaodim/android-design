package com.kaodim.design.components.editText

import android.databinding.*
import android.text.Editable
import android.text.TextWatcher

@InverseBindingMethods(InverseBindingMethod(type = KaodimPhoneNumberEditText::class, attribute = "android:text"))
class KaodimPhoneNumberEditTextBinding {

    object KaodimPhoneNumberEditTextBinding {
        @BindingAdapter(value = ["android:textAttrChanged"])
        fun setListener(layout: KaodimEditText, textAttrChanged: InverseBindingListener?) {
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
        fun setText(view: KaodimEditText, value: String?) {
            if (value != null && value != view.text) view.text = value
        }

        @InverseBindingAdapter(attribute = "android:text")
        fun getText(view: KaodimEditText): String? {
            return view.text
        }
    }

}