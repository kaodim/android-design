package com.kaodim.design.components.editText;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.databinding.InverseBindingMethod;
import android.databinding.InverseBindingMethods;
import android.text.Editable;
import android.text.TextWatcher;

@InverseBindingMethods({
        @InverseBindingMethod(type = KaodimPhoneNumberEditTextBinding.class, attribute = "android:text"),
})
public class KaodimPhoneNumberEditTextBinding {
    @BindingAdapter(value = "android:textAttrChanged")
    public static void setListener(KaodimPhoneNumberEditText layout, final InverseBindingListener textAttrChanged) {
        if (textAttrChanged != null) {
            layout.getInputEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    textAttrChanged.onChange();
                }
            });
        }
    }

    @BindingAdapter("android:text")
    public static void setText(KaodimPhoneNumberEditText view, String value) {
        if (value != null && !value.equals(view.getText()))
            view.setText(value);
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static String getText(KaodimPhoneNumberEditText view) {
        return view.getText();
    }
}
