package com.makemeold.agefaceeditor.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.R;
import com.makemeold.agefaceeditor.share.DisplayMetricsHandler;
import com.makemeold.agefaceeditor.share.MainApplication;
import com.google.android.gms.ads.AdListener;
import com.makemeold.agefaceeditor.share.Share;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class HomeScreen extends Wrapper implements OnClickListener {

    ImageView lnCamera;
    public final int STORAGE_PERMISSION_CODE = 23;
    LinearLayout lnGallery, lnMyphotos;

    private String image_name = "";
    ImageView imgPrivacy, imgRateus;


    private List<String> listPermissionsNeeded = new ArrayList<>();

    Uri imgUri;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        getDisplaySize();
        findViews();
        initViews();

    }

    private void findViews() {

        lnCamera = findViewById(R.id.ic_camera);
        lnGallery = findViewById(R.id.ic_gallery);
        lnMyphotos = findViewById(R.id.ic_myphotos);

        imgPrivacy = findViewById(R.id.privacy);
        imgRateus = findViewById(R.id.rateus);
    }

    private void initViews() {

        lnCamera.setOnClickListener(this);
        lnGallery.setOnClickListener(this);
        lnMyphotos.setOnClickListener(this);

        imgPrivacy.setOnClickListener(this);
        imgRateus.setOnClickListener(this);
    }


    void pickImageFromCamera() {

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        try {
            File photo = createTemporaryFile("picture", ".jpg");
            imgUri = Uri.fromFile(photo);
            photo.delete();
            intent.putExtra("output", imgUri);
            startActivityForResult(intent, 2);
        } catch (Exception e) {
            Log.e("test", "Can't create file to take picture!");
        }
    }

    private File createTemporaryFile(String part, String ext) throws Exception {

        File tempDir = new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append("/.temp/").toString());
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        return File.createTempFile(part, ext, tempDir);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent intent1;
        switch (requestCode) {
            case 1:
                if (resultCode == -1 && data != null) {
                    Log.d("test", "image from gallery");
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        Log.d("test", "image from gallery url " + selectedImage);
                        intent1 = new Intent(this, MainScreen.class);
                        intent1.putExtra("URI", selectedImage.toString());
                        startActivity(intent1);
                        return;
                    }
                    return;
                }
                return;
            case 2:
                if (resultCode == -1) {
                    if (imgUri == null) {
                        Log.d("faceswap", "imagechooser camera image url is null");
                        if (data != null) {
                            imgUri = data.getData();
                        }
                        if (imgUri == null) {
                            return;
                        }
                    }
                    intent1 = new Intent(this, MainScreen.class);
                    intent1.putExtra("URI", imgUri.toString());
                    startActivity(intent1);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private boolean checkPermissions() {

        listPermissionsNeeded.clear();

        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readStorage = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        int camera = ContextCompat.checkSelfPermission(this, CAMERA);

        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (readStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(READ_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(CAMERA);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case STORAGE_PERMISSION_CODE:

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

                        || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        Log.e("TAG", "onRequestPermissionsResult: deny");

                    } else {
                        Log.e("TAG", "onRequestPermissionsResult: dont ask again");

                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                        alertDialogBuilder.setTitle("Permissions Required")
                                .setMessage("Please allow permission for storage")
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

                    if (image_name == "camera") {

                        pickImageFromCamera();

                    } else if (image_name == "gallery") {

                        startActivityForResult(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), 1);

                    } else if (image_name == "my_photos") {

                        Intent i = new Intent(HomeScreen.this, MyPhotosActivity.class);
                        startActivity(i);
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {

        if (v == lnCamera) {

            if (checkPermissions()) {

                image_name = "camera";
                pickImageFromCamera();

            } else {

                image_name = "camera";
                ActivityCompat.requestPermissions(HomeScreen.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE);

            }

        } else if (v == lnGallery) {

            if (checkPermissions()) {

                image_name = "gallery";
                startActivityForResult(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), 1);

            } else {

                image_name = "gallery";
                ActivityCompat.requestPermissions(HomeScreen.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE);

            }
        } else if (v == lnMyphotos) {

            Random rand = new Random();
            int randomNum = 0 + rand.nextInt(5);

            if (randomNum == 0) {

                if (checkPermissions()) {

                    if (!MainApplication.getInstance().requestNewInterstitial()) {

                        image_name = "my_photos";
                        Intent i = new Intent(HomeScreen.this, MyPhotosActivity.class);
                        startActivity(i);

                    } else {

                        MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();

                                MainApplication.getInstance().mInterstitialAd.setAdListener(null);
                                MainApplication.getInstance().mInterstitialAd = null;
                                MainApplication.getInstance().ins_adRequest = null;
                                MainApplication.getInstance().LoadAds();

                                image_name = "my_photos";
                                Intent i = new Intent(HomeScreen.this, MyPhotosActivity.class);
                                startActivity(i);

                            }

                            @Override
                            public void onAdFailedToLoad(int i) {
                                super.onAdFailedToLoad(i);
                            }

                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                            }
                        });
                    }


                } else {

                    image_name = "my_photos";
                    ActivityCompat.requestPermissions(HomeScreen.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE);
                }
            } else {

                if (checkPermissions()) {

                    image_name = "my_photos";
                    Intent i = new Intent(HomeScreen.this, MyPhotosActivity.class);
                    startActivity(i);

                } else {

                    image_name = "my_photos";
                    ActivityCompat.requestPermissions(HomeScreen.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE);

                }
            }

        } else if (v == imgPrivacy) {

            if (!MainApplication.getInstance().requestNewInterstitial()) {

                Intent i = new Intent(HomeScreen.this, PrivacyPolicyScreen.class);
                startActivity(i);

            } else {

                MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();

                        MainApplication.getInstance().mInterstitialAd.setAdListener(null);
                        MainApplication.getInstance().mInterstitialAd = null;
                        MainApplication.getInstance().ins_adRequest = null;
                        MainApplication.getInstance().LoadAds();

                        Intent i = new Intent(HomeScreen.this, PrivacyPolicyScreen.class);
                        startActivity(i);

                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                    }
                });
            }
        } else if (v == imgRateus) {

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
            builder.setMessage("Are you sure you want to rate this App?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    @Override
    public void onBackPressed() {

        try {

            Log.e("TAG", "onBackPressed: ");

            final Dialog dialog = new Dialog(HomeScreen.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dlg_exit1);
            dialog.getWindow().setLayout((int) (DisplayMetricsHandler.getScreenWidth() - 50), Toolbar.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            LinearLayout rateus, shareapp, nothnks;

            rateus = dialog.findViewById(R.id.rateus);
            shareapp = dialog.findViewById(R.id.shareapp);
            nothnks = dialog.findViewById(R.id.nothnks);

            rateus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));

                    } catch (android.content.ActivityNotFoundException anfe) {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                    }

                }
            });

            shareapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                        String shareMessage = "Make me OLD" + "" + "\n\nLet me recommend you this application\n\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                        dialog.dismiss();

                    } catch (Exception e) {

                    }

                }
            });

            nothnks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.cancel();
                    HomeScreen.super.onBackPressed();
                    finish();

                }
            });

        } catch (Exception e) {
            e.getMessage();
        }


    }

    private void getDisplaySize() {

        Display display = getWindow().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Share.screenWidth = size.x;
        Share.screenHeight = size.y;
    }

}

