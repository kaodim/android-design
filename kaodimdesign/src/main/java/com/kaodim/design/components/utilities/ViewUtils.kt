package com.kaodim.design.components.utilities

import android.view.ViewGroup

object ViewUtils {

    @JvmStatic fun recursiveSetEnabled(vg: ViewGroup, enabled: Boolean) {
        var i = 0

        val count = vg.childCount
        while (i < count) {
            val child = vg.getChildAt(i)
            child.isEnabled = enabled
            if (child is ViewGroup) {
                recursiveSetEnabled(child, enabled)
            }
            ++i
        }

    }
}