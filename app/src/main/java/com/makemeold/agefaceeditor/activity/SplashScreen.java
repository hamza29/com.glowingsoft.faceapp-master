package com.makemeold.agefaceeditor.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.makemeold.agefaceeditor.R;
import com.makemeold.agefaceeditor.database.DBAdapter;
import com.makemeold.agefaceeditor.database.ImportDatabase;
import com.makemeold.agefaceeditor.share.MainApplication;
import com.google.android.gms.ads.AdListener;


import java.io.File;
import java.io.InputStream;

public class SplashScreen extends AppCompatActivity {


    final DBAdapter dba = new DBAdapter(this);
    public static InputStream databaseInputStream1;

    Handler handler = new Handler();
    Runnable myRunnable;

    String TAG = "TAG";

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initViews();

    }

    private void initViews() {

        handler = new Handler();

        myRunnable = new Runnable() {
            public void run() {

                if (!MainApplication.getInstance().requestNewInterstitial()) {

                    Intent i = new Intent(SplashScreen.this, HomeScreen.class);
                    startActivity(i);
                    finish();

                } else {

                    MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();

                            MainApplication.getInstance().mInterstitialAd.setAdListener(null);
                            MainApplication.getInstance().mInterstitialAd = null;
                            MainApplication.getInstance().ins_adRequest = null;
                            MainApplication.getInstance().LoadAds();

                            Intent i = new Intent(SplashScreen.this, HomeScreen.class);
                            startActivity(i);
                            finish();

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

            }
        };
        handler.postDelayed(myRunnable, 2000);
    }

    public void showProgressbar() {

        pd = new ProgressDialog(SplashScreen.this);
        pd.setMessage(getString(R.string.please_wait));
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

    }


    public class CopyDB extends AsyncTask<String, Void, Boolean> {
        @SuppressLint("SdCardPath")
        @Override
        protected void onPreExecute() {

            try {

                File f = new File("/data/data/" + getPackageName() + "/databases/Suitdb.sql");

                Log.e("File of Local DataBase", "f  : " + f);
                if (f.exists()) {

                } else {
                    try {
                        dba.open();
                        dba.close();
                        System.out.println("Database is copying.....");
                        databaseInputStream1 = getAssets().open("Suitdb.sql");
                        ImportDatabase.copyDataBase();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {

            Boolean success = false;

            try {
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        new CopyDB().execute("");

    }


}