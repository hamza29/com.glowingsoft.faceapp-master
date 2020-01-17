package com.makemeold.agefaceeditor.activity;

import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;


import com.makemeold.agefaceeditor.R;
import com.makemeold.agefaceeditor.StickerView.DrawableSticker;
import com.makemeold.agefaceeditor.adapter.StickerAdapter_Assets;
import com.makemeold.agefaceeditor.common.StickerModel_Assets;
import com.makemeold.agefaceeditor.share.Share;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class StickerScreen extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rv_sticker;
    private ImageView iv_back, iv_more_app, iv_blast;
    private AssetManager assetManager;
    private ArrayList<StickerModel_Assets> stickerarray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker);

            assetManager = getAssets();

            rv_sticker = (RecyclerView) findViewById(R.id.rv_sticker);
            iv_back = (ImageView) findViewById(R.id.iv_back_again);



            iv_back.setOnClickListener(this);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(StickerScreen.this, 3);
            rv_sticker.setLayoutManager(gridLayoutManager);

            LoadSticker();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void LoadSticker() {
        new GetSticker().execute();
    }

    @Override
    public void onClick(View view) {

        if (view == iv_back) {
            finish();
        }
    }

    private class GetSticker extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(StickerScreen.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            final String[] imgPath;
            try {
                imgPath = assetManager.list("stickers");
                sortArray(imgPath, "sticker_");
                for (int j = 0; j < imgPath.length; j++) {

                    InputStream is = assetManager.open("stickers/" + imgPath[j]);
                    Drawable drawable = Drawable.createFromStream(is, null);

                    StickerModel_Assets stickerModel_assets = new StickerModel_Assets();
                    stickerModel_assets.setDrawable(drawable);

                    stickerarray.add(stickerModel_assets);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressDialog.dismiss();

            final StickerAdapter_Assets stickerAdapter_assets = new StickerAdapter_Assets(StickerScreen.this, stickerarray);
            rv_sticker.setAdapter(stickerAdapter_assets);

            stickerAdapter_assets.setEventListener(new StickerAdapter_Assets.EventListener() {
                @Override
                public void onItemViewClicked(int position) {

                    DrawableSticker drawableSticker = new DrawableSticker(stickerarray.get(position).getDrawable());
                    drawableSticker.setTag("cartoon");
                    ResultScreen.stickerView.addSticker(drawableSticker);

                    ResultScreen.drawables_sticker.add(drawableSticker);
                    Share.isStickerAvail = true;
                    Share.isStickerTouch = true;
                    //ResultScreen.stickerView.setLocked(false);

                    finish();
                }

                @Override
                public void onDeleteMember(int position) {

                }
            });
        }
    }

    private void sortArray(String[] arrayList, final String frontStr) {

        Arrays.sort(arrayList, new Comparator<String>() {
            @Override
            public int compare(String entry1, String entry2) {
                Integer file1 = Integer.parseInt((entry1.split(frontStr)[1]).split("\\.")[0]);
                Integer file2 = Integer.parseInt((entry2.split(frontStr)[1]).split("\\.")[0]);
                return file1.compareTo(file2);
            }
        });
    }
}
