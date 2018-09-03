package com.kaodim.design.components.pre_loader;


import android.app.Activity;
import android.view.View;

import com.kaodim.design.R;
import com.shasin.notificationbanner.Banner;

public class PreLoaderAnimation {

    private static PreLoaderAnimation instance = null;

    private PreLoaderAnimation() {
    }

    public static PreLoaderAnimation getInstance() {
        if (instance == null) {
            instance = new PreLoaderAnimation();
        }
        return instance;
    }

    public static void showLoading(View view, Activity activity){

        if(activity != null){
            Banner.make(view,activity, R.layout.kdl_pre_loader,true);
            Banner.getInstance().show();
        }
    }

    public static void hideLoading(){
        Banner.getInstance().dismissBanner();
    }
}
