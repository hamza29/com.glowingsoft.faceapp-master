package com.makemeold.agefaceeditor.share;


import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.makemeold.agefaceeditor.Constant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MainApplication extends MultiDexApplication {


    public PublisherInterstitialAd mInterstitialAd;
    public PublisherAdRequest ins_adRequest;

    private static final String TAG = "Application";

    private static MainApplication appInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        appInstance = this;
        MultiDex.install(this);
        //TODO to solve camera issue
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .diskCacheExtraOptions(480, 800, null)
                .diskCacheSize(100 * 1024 * 1024)
                .diskCacheFileCount(100)
                .build();
        ImageLoader.getInstance().init(config);

        LoadAds();

    }

    public void LoadAds() {

        try {

            Log.e("HairColorApplication", "LoadAds Called");
            mInterstitialAd = new PublisherInterstitialAd(this);

            mInterstitialAd.setAdUnitId(Constant.interstitialId);

            ins_adRequest = new PublisherAdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("EA965DE183B804F71E5E6D353E6607DE")
                    .addTestDevice("5CE992DB43E8F2B50F7D2201A724526D")
                    .addTestDevice("6E5543AE954EAD6702405BFCCC34C9A2")
                    .addTestDevice("28373E4CC308EDBD5C5D39795CD4956A") //samsung
                    .addTestDevice("79E8DED973BDF7477739501E228D88E1") //samdung max|
                    .build();

            mInterstitialAd.loadAd(ins_adRequest);
        } catch (Exception e) {
        }
    }

    public boolean requestNewInterstitial() {

        try {
            if (mInterstitialAd.isLoaded()) {
                Log.e("HairColorApplication", "requestNewInterstitial isLoaded Called");
                mInterstitialAd.show();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isLoaded() {

        try {
            if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static synchronized MainApplication getInstance() {
        return appInstance;
    }

}
