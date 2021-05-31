package com.kaodim.design.components.callback;

public interface NumericControlListener {
    void onNumericAdded(int value);
    void onNumericRemoved(int value);
    void onNumericValueChanged(int value);
}
