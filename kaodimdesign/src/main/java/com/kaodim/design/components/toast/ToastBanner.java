package com.kaodim.design.components.toast;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaodim.design.R;
import com.shasin.notificationbanner.Banner;

public class ToastBanner {

    public static int AUTODISMISS_LENGTH = 5000;
    public static int NO_AUTODISMISS = 0;


    private static ToastBanner instance = null;

    private ToastBanner() {
    }

    public static ToastBanner getInstance() {
        if (instance == null) {
            instance = new ToastBanner();
        }
        return instance;
    }

    public void showErrorAlert(View view, Activity activity, String error , int duration){
        if(activity != null){
            Banner.make(view,activity,Banner.TOP, R.layout.kdl_notification_error,true);
            TextView label = Banner.getInstance().getBannerView().findViewById(R.id.errorLabel);
            label.setText(error);


            RelativeLayout cancel = Banner.getInstance().getBannerView().findViewById(R.id.rlErrorCancel);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Banner.getInstance().dismissBanner();
                }
            });

            Banner.getInstance().setDuration(duration);

            if(activity.hasWindowFocus())
                Banner.getInstance().show();

        }
    }

    public void showSuccessAlert(View view, Activity activity, String error, int duration){
        if(activity != null){
            Banner.make(view,activity,Banner.TOP, R.layout.kdl_notification_success,true);
            TextView label = Banner.getInstance().getBannerView().findViewById(R.id.successLabel);
            label.setText(error);
            RelativeLayout cancel = Banner.getInstance().getBannerView().findViewById(R.id.rlSuccessCancel);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Banner.getInstance().dismissBanner();
                }
            });

            Banner.getInstance().setDuration(duration);

            if(activity.hasWindowFocus()) //prevent activity not running crash for async call
                Banner.getInstance().show();
        }
    }
}
