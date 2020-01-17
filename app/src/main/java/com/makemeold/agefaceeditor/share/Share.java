package com.makemeold.agefaceeditor.share;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


import com.makemeold.agefaceeditor.R;
import com.makemeold.agefaceeditor.StickerView.DrawableSticker;

import java.io.File;
import java.util.ArrayList;

public class Share {

    public static String Fragment = "MyPhotosFragmen" + "t";
    public static Bitmap EDITED_IMAGE = null;

    public static int screenWidth = 0;
    public static int screenHeight = 0;

    //font style //
    public static boolean FONT_FLAG = false;
    public static String FONT_TEXT = "";
    public static String FONT_STYLE = "";
    public static String FONT_EFFECT = "6";
    public static Integer COLOR = Color.parseColor("#00BFFF");
    public static DrawableSticker TEXT_DRAWABLE;


    public static boolean isStickerTouch = false;
    public static boolean isStickerAvail = false;


    public static String from = "";


    public static Bitmap SELECTED_BITMAP;

    public static String image_path;

    public static ArrayList<File> al_my_photos_favourite = new ArrayList<>();
    public static int my_favourite_position = 0;

    public static ArrayList<File> al_my_photos_photo = new ArrayList<>();
    public static int my_photos_position = 0;


    public static Bitmap bitmap;

    public static final String IMAGE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "Make me OLD";

    public static Dialog showProgress(Context mContext, String text) {

        Dialog mDialog = new Dialog(mContext, R.style.MyTheme);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View layout = mInflater.inflate(R.layout.dialog_progress, null);
        mDialog.setContentView(layout);

        TextView mTextView = (TextView) layout.findViewById(R.id.text);
        if (text.equals(""))
            mTextView.setVisibility(View.GONE);
        else
            mTextView.setText(text);

        mDialog.setCancelable(false);
        return mDialog;
    }

    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext, R.style.MyTheme);
        try {
            if (dialog.isShowing()) {
                dialog.dismiss();
                createProgressDialog(mContext);
            } else {
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setProgressDrawable((new ColorDrawable(Color.parseColor("#E45E46"))));
        dialog.setContentView(R.layout.progress_dialog_layout);
        return dialog;
    }


}
