package com.kaodim.design.components.pre_loader;


import android.app.Activity;
import android.view.View;

import com.kaodim.design.components.banner.Banner;

import static com.androidquery.util.AQUtility.postDelayed;

public class PreLoaderAnimation {

    private static final int MIN_SHOW_TIME = 500; // ms
    private static final int MIN_DELAY = 500; // ms

    static long mStartTime = -1;

    static boolean mPostedHide = false;

    static boolean mPostedShow = false;

    static boolean mDismissed = false;

    static Banner banner;

    private static final Runnable mDelayedHide = new Runnable() {

        @Override
        public void run() {
            mPostedHide = false;
            mStartTime = -1;
            hide();
        }
    };

    private static final Runnable mDelayedShow = new Runnable() {

        @Override
        public void run() {
            mPostedShow = false;
            if (!mDismissed) {
                mStartTime = System.currentTimeMillis();
            }
        }
    };


    private static void show(View view, Activity activity) {
        if (activity != null) {
            if (banner != null) {
                banner.dismissBanner();
            }
            banner = new Banner(view,activity);
            banner.setAsDropDown(false);
            banner.setFillScreen(true);
            banner.setLayout(com.kaodim.design.R.layout.kdl_pre_loader);
            banner.show();
        }

    }

    private static void hide() {
        if(banner!=null){
            banner.dismissBanner();
        }
    }


    /**
     * Hide the progress view if it is visible. The progress view will not be
     * hidden until it has been shown for at least a minimum show time. If the
     * progress view was not yet visible, cancels showing the progress view.
     */
    public static synchronized void hideLoading() {
        mDismissed = true;
        mPostedShow = false;
        long diff = System.currentTimeMillis() - mStartTime;
        if (diff >= MIN_SHOW_TIME || mStartTime == -1) {
            // The progress spinner has been shown long enough
            // OR was not shown yet. If it wasn't shown yet,
            // it will just never be shown.
            if(banner!=null){
                banner.dismissBanner();
            }
        } else {
            // The progress spinner is shown, but not long enough,
            // so put a delayed message in to hide it when its been
            // shown long enough.
            if (!mPostedHide) {
                postDelayed(mDelayedHide, MIN_SHOW_TIME - diff);
                mPostedHide = true;
            }
        }
    }

    /**
     * Show the progress view after waiting for a minimum delay. If
     * during that time, hide() is called, the view is never made visible.
     */
    public static synchronized void showLoading(final View view,final Activity activity) {
        // Reset the start time.
        mStartTime = -1;
        mDismissed = false;
        mPostedHide = false;
        if (!mPostedShow) {
            postDelayed(new Runnable() {

                @Override
                public void run() {
                    mPostedShow = false;
                    if (!mDismissed) {
                        mStartTime = System.currentTimeMillis();
                        show(view,activity);
                    }
                }
            }, MIN_DELAY);
            mPostedShow = true;
        }
    }
}
