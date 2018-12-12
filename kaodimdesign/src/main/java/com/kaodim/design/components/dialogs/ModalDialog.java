package com.kaodim.design.components.dialogs;

import android.app.Activity;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaodim.design.R;
import com.shasin.notificationbanner.Banner;

public class ModalDialog {

    public static final int TYPE_SINGLE_BUTTON = 1;
    public static final int TYPE_DOUBLE_BUTTON = 2;
    public static final int TYPE_SINGLE_BUTTON_NO_ICON = 3;
    public static final int TYPE_DOUBLE_BUTTON_NO_ICON = 4;
    public static final int TYPE_NO_BUTTON_WITH_ICON = 5;

    public interface ModalDialogListener {
        void onButtonPrimaryClicked();
        void onButtonSecondaryClicked();
    }

    ModalDialogListener listener;

    private Banner banner;


    public ModalDialog(Activity activity, View view){
        banner = new Banner(view,activity);
        banner.setAsDropDown(false);
        banner.setFillScreen(true);
        banner.setLayout(R.layout.kdl_modal_dialog);
    }


    /**
     Set type for the model dialog
     **/
    public void setType(int type){
        if(banner!=null){
            if(type == TYPE_SINGLE_BUTTON){
                banner.getBannerView().findViewById(R.id.btnSecondary).setVisibility(View.GONE);
                banner.getBannerView().findViewById(R.id.btnPrimary).setVisibility(View.VISIBLE);
                banner.getBannerView().findViewById(R.id.ivImage).setVisibility(View.VISIBLE);
            }
            else if(type == TYPE_DOUBLE_BUTTON){
                banner.getBannerView().findViewById(R.id.btnSecondary).setVisibility(View.VISIBLE);
                banner.getBannerView().findViewById(R.id.btnPrimary).setVisibility(View.VISIBLE);
                banner.getBannerView().findViewById(R.id.ivImage).setVisibility(View.VISIBLE);
            }
            else if(type == TYPE_SINGLE_BUTTON_NO_ICON){
                banner.getBannerView().findViewById(R.id.btnSecondary).setVisibility(View.GONE);
                banner.getBannerView().findViewById(R.id.btnPrimary).setVisibility(View.VISIBLE);
                banner.getBannerView().findViewById(R.id.ivImage).setVisibility(View.GONE);
            }
            else if(type == TYPE_DOUBLE_BUTTON_NO_ICON){
                banner.getBannerView().findViewById(R.id.btnSecondary).setVisibility(View.VISIBLE);
                banner.getBannerView().findViewById(R.id.btnPrimary).setVisibility(View.VISIBLE);
                banner.getBannerView().findViewById(R.id.ivImage).setVisibility(View.GONE);
            }
            else if(type == TYPE_NO_BUTTON_WITH_ICON){
                banner.getBannerView().findViewById(R.id.btnSecondary).setVisibility(View.GONE);
                banner.getBannerView().findViewById(R.id.btnPrimary).setVisibility(View.GONE);
                banner.getBannerView().findViewById(R.id.ivImage).setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     Set label text for type single button
     **/
    public void setTextForSingleButton(String title,String description, String btnPrimaryText){
        if(banner!=null){
            banner.getBannerView().findViewById(R.id.tvTitle).setVisibility(View.VISIBLE);
            TextView tvtitle = (TextView) banner.getBannerView().findViewById(R.id.tvTitle);
            tvtitle.setText(title);

            banner.getBannerView().findViewById(R.id.tvDescription).setVisibility(View.VISIBLE);
            TextView tvDescription = (TextView) banner.getBannerView().findViewById(R.id.tvDescription);
            tvDescription.setText(description);

            banner.getBannerView().findViewById(R.id.btnPrimary).setVisibility(View.VISIBLE);
            Button btnPrimary = (Button) banner.getBannerView().findViewById(R.id.btnPrimary);
            btnPrimary.setText(btnPrimaryText);

            banner.getBannerView().findViewById(R.id.btnSecondary).setVisibility(View.GONE);
        }
    }

    /**
     Set label text for no button
     **/
    public void setTextForNoButton(String title,String description){
        if(banner!=null){
            banner.getBannerView().findViewById(R.id.tvTitle).setVisibility(View.VISIBLE);
            TextView tvtitle = (TextView) banner.getBannerView().findViewById(R.id.tvTitle);
            tvtitle.setText(title);

            banner.getBannerView().findViewById(R.id.tvDescription).setVisibility(View.VISIBLE);
            TextView tvDescription = (TextView) banner.getBannerView().findViewById(R.id.tvDescription);
            tvDescription.setText(description);

            banner.getBannerView().findViewById(R.id.btnPrimary).setVisibility(View.GONE);
            banner.getBannerView().findViewById(R.id.btnSecondary).setVisibility(View.GONE);
        }
    }


    /**
     Set label text for type double button
     **/
    public void setTextForDoubleButton(String title,String description, String btnPrimaryText, String btnSecondaryText ){
        if(banner!=null){
            banner.getBannerView().findViewById(R.id.tvTitle).setVisibility(View.VISIBLE);
            TextView tvtitle = (TextView) banner.getBannerView().findViewById(R.id.tvTitle);
            tvtitle.setText(title);

            banner.getBannerView().findViewById(R.id.tvDescription).setVisibility(View.VISIBLE);
            TextView tvDescription = (TextView) banner.getBannerView().findViewById(R.id.tvDescription);
            tvDescription.setText(description);

            banner.getBannerView().findViewById(R.id.btnPrimary).setVisibility(View.VISIBLE);
            Button btnPrimary = (Button) banner.getBannerView().findViewById(R.id.btnPrimary);
            btnPrimary.setText(btnPrimaryText);

            banner.getBannerView().findViewById(R.id.btnSecondary).setVisibility(View.VISIBLE);
            Button btnSecondary = (Button) banner.getBannerView().findViewById(R.id.btnSecondary);
            btnSecondary.setText(btnSecondaryText);
        }
    }


    /**
     Invoke this to show the pop up
     **/
    public void show(){
        setListeners();
        banner.show();
    }

    /**
     Invoke this to hide the pop up
     **/
    public void hide(){
        if(banner!=null){
            banner.dismissBanner();
        }
    }


    /**
     This will set the icon, mas dimension is 150x150
     **/
    public void setIcon(int resourceID){
        if(banner!=null){
            ImageView imageView =  banner.getBannerView().findViewById(R.id.ivImage);
            imageView.setImageResource(resourceID);

        }
    }

    /**
     This sets margin to top of illustration asset
     **/
    public void setTopMarginToIcon(int marginInPx) {
        RelativeLayout.LayoutParams parameter = (RelativeLayout.LayoutParams) banner.getBannerView().findViewById(R.id.ivImage).getLayoutParams();
        parameter.setMargins(marginInPx, parameter.topMargin, parameter.rightMargin, parameter.bottomMargin); // left, top, right, bottom
        banner.getBannerView().findViewById(R.id.ivImage).setLayoutParams(parameter);
    }



    private void setListeners(){
        if(banner!=null){
            banner.getBannerView().findViewById(R.id.btnPrimary).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onButtonPrimaryClicked();
                }
            });

            banner.getBannerView().findViewById(R.id.btnSecondary).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onButtonSecondaryClicked();
                }
            });

            banner.getBannerView().findViewById(R.id.rlCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   banner.dismissBanner();
                }
            });

//            banner.getBannerView().findViewById(R.id.rlOverlay).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    banner.dismissBanner();
//                }
//            });
        }
    }

    public void setButtonOnClickListener(ModalDialogListener modalDialogListener) {
        this.listener = modalDialogListener;
    }

    /**
        Call this method for non dismissable pop up
    **/
    public void setDissmissable(boolean isDismissable){
        if(banner !=null){
            if(isDismissable){
                setTypeDismissable();
            }else
                setTypeNonDismissable();
        }
    }

    private void setTypeNonDismissable(){
        banner.getBannerView().findViewById(R.id.rlCancel).setVisibility(View.GONE);
        banner.getBannerView().findViewById(R.id.rlOverlay).setClickable(false);
        banner.getBannerView().findViewById(R.id.rlOverlay).setFocusable(false);
        banner.getBannerView().findViewById(R.id.rlOverlay).setEnabled(false);
    }

    private void setTypeDismissable(){
        banner.getBannerView().findViewById(R.id.rlCancel).setVisibility(View.VISIBLE);
        banner.getBannerView().findViewById(R.id.rlOverlay).setClickable(true);
        banner.getBannerView().findViewById(R.id.rlOverlay).setFocusable(true);
        banner.getBannerView().findViewById(R.id.rlOverlay).setEnabled(true);
        banner.getBannerView().findViewById(R.id.rlOverlay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    banner.dismissBanner();
                }
            });
    }
}
