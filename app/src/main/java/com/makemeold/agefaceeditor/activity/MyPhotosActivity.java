package com.makemeold.agefaceeditor.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makemeold.agefaceeditor.Constant;
import com.makemeold.agefaceeditor.R;
import com.makemeold.agefaceeditor.fragment.FavouriteFragment;
import com.makemeold.agefaceeditor.fragment.MyPhotosFragment;
import com.makemeold.agefaceeditor.share.Share;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MyPhotosActivity extends AppCompatActivity implements View.OnClickListener {

    public static ArrayList<File> myphotoArraylist = new ArrayList<>();
    private File[] allFiles;

    private ImageView imgBack, imgDelete;

    TextView tvAll, tvFav;
    private List<String> listPermissionsNeeded = new ArrayList<>();
    android.support.v4.app.Fragment fragment = null;


    MyPhotosActivity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_photos);

        activity = MyPhotosActivity.this;


        addBanner();
        if (checkPermissions()) {

            findViews();
            initViews();

        } else {


        }

    }

    private void findViews() {

        imgBack = (ImageView) findViewById(R.id.iv_back_my_photos);
        imgDelete = (ImageView) findViewById(R.id.iv_all_delete);

        tvAll = (TextView) findViewById(R.id.tvAll);
        tvFav = (TextView) findViewById(R.id.tvFav);

    }

    private void initViews() {

        tvAll.setOnClickListener(this);
        tvFav.setOnClickListener(this);

        setListner();
        setData();

        updateFragment(MyPhotosFragment.newInstance());

    }

    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tvAll:
                System.gc();
                Runtime.getRuntime().gc();
                tvAll.setTextColor(getResources().getColor(R.color.white));
                tvAll.setBackgroundResource(R.drawable.back_txt_left_selected);

                tvFav.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvFav.setBackgroundResource(R.drawable.back_txt_right_unselected);
                fragment = MyPhotosFragment.newInstance();
                updateFragment(fragment);
                break;

            case R.id.tvFav:
                System.gc();
                Runtime.getRuntime().gc();
                tvFav.setTextColor(getResources().getColor(R.color.white));
                tvFav.setBackgroundResource(R.drawable.back_txt_right_selected);

                tvAll.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvAll.setBackgroundResource(R.drawable.back_txt_left_unselected);
                fragment = FavouriteFragment.newInstance();
                updateFragment(fragment);
                break;

            case R.id.iv_back_my_photos:
                onBackPressed();
                break;
        }
    }

    private void updateFragment(android.support.v4.app.Fragment fragment) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.simpleFrameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void setListner() {
        imgBack.setOnClickListener(this);
        imgDelete.setOnClickListener(this);
    }

    private void setData() {
        File path = new File(Share.IMAGE_PATH);
        myphotoArraylist.clear();

        if (path.exists()) {
            allFiles = path.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"));
                }
            });

            if (allFiles.length > 0) {
                imgDelete.setAlpha(1f);
                imgDelete.setEnabled(true);
                for (int i = 0; i < allFiles.length; i++) {
                    myphotoArraylist.add(allFiles[i]);

                }
                Collections.sort(myphotoArraylist, Collections.reverseOrder());

            } else {

                imgDelete.setAlpha(0.5f);
                imgDelete.setEnabled(false);

            }
        } else {

            imgDelete.setAlpha(0.5f);
            imgDelete.setEnabled(false);

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private boolean checkPermissions() {

        listPermissionsNeeded.clear();

        int writeStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writeStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (readStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        Log.e("TAG", "listPermissionsNeeded===>" + listPermissionsNeeded);
        if (!listPermissionsNeeded.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                        || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Log.e("TAG", "onRequestPermissionsResult: deny");

            } else {
                Log.e("TAG", "onRequestPermissionsResult: dont ask again");

                final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Permissions Required")
                        .setMessage("Please allow permission for Storage")
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }

        } else {

            //saveImage();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checkPermissions()) {

            setData();

        } else {


        }
    }

    public void addBanner() {

        final AdView mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);
        final View adContainer = findViewById(R.id.layoutViewAdd);


        mAdView.setAdUnitId(Constant.bannerId);

        ((LinearLayout) adContainer).addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("EA965DE183B804F71E5E6D353E6607DE")
                .addTestDevice("5CE992DB43E8F2B50F7D2201A724526D")
                .addTestDevice("6E5543AE954EAD6702405BFCCC34C9A2")
                .addTestDevice("28373E4CC308EDBD5C5D39795CD4956A")
                .addTestDevice("3C5740EB2F36FB5F0FEFA773607D27CE") // mi white
                .addTestDevice("79E8DED973BDF7477739501E228D88E1") //samsung max
                .build();

        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);


                adContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                adContainer.setVisibility(View.VISIBLE);

            }
        });
    }

}

