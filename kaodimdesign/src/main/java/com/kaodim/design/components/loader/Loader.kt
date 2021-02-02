package com.kaodim.design.components.loader

import android.app.Activity
import android.support.v7.app.AlertDialog
import com.kaodim.design.R

class Loader {
    companion object {
        fun newInstance(activity: Activity): AlertDialog {
            val builder = AlertDialog.Builder(activity, R.style.WrapContentDialog)
            builder.setView(R.layout.kdl_pre_loader)

            val dialog = builder.create()
            dialog.setCanceledOnTouchOutside(false)
            dialog.setOnCancelListener {
                it.dismiss()
                activity.onBackPressed()
            }
            return dialog
        }
    }
}