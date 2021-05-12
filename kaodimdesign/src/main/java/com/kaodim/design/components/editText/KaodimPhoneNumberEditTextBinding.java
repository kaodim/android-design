package com.kaodim.design.components.editText;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

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

    @BindingAdapter(value = "app:textButtonOnClick")
    public static void setTextButtonOnClick(KaodimPhoneNumberEditText view, View.OnClickListener onClick) {
            view.setTextButtonOnClick(onClick);
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static String getText(KaodimPhoneNumberEditText view) {
        return view.getText();
    }
}
