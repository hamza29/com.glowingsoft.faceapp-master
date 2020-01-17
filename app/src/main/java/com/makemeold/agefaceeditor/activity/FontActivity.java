package com.makemeold.agefaceeditor.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makemeold.agefaceeditor.R;
import com.makemeold.agefaceeditor.StickerView.DrawableSticker;
import com.makemeold.agefaceeditor.adapter.FontAdapter;
import com.makemeold.agefaceeditor.common.FontModel;
import com.makemeold.agefaceeditor.share.Share;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.ArrayList;

public class FontActivity extends AppCompatActivity implements View.OnClickListener {

    private FontAdapter adapter;
    private ArrayList<FontModel> fontModelArrayList = new ArrayList<>();

    private Toolbar toolbar;
    private EditText edtText;
    private ImageView imgColor, imgClose, imgDone;
    private RecyclerView recyclerView;
    private LinearLayout ll_font_color;

    private String font_array[] = {"1", "6", "ardina_script", "beyondwonderland", "C", "coventry_garden_nf", "font3", "font6", "font10", "font16", "font20", "g", "h", "h2", "h3", "h6", "h7", "h8", "h15", "h18", "h19", "h20", "m", "o", "saman", "variane", "youmurdererbb"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fontlayout);

        findViews();
        initView();

    }

    private void findViews() {

        edtText = (EditText) findViewById(R.id.edittext);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ll_font_color = (LinearLayout) findViewById(R.id.ll_font_color);
        imgColor = (ImageView) findViewById(R.id.color);
        imgClose = (ImageView) findViewById(R.id.close);
        imgDone = (ImageView) findViewById(R.id.done);
    }

    private void initView() {

        recyclerView = (RecyclerView) findViewById(R.id.rv_font);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(FontActivity.this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.getLayoutParams().height = Share.screenHeight - toolbar.getHeight() - ll_font_color.getHeight();

        edtText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;
            }
        });

        imgColor.setOnClickListener(this);
        imgClose.setOnClickListener(this);
        imgDone.setOnClickListener(this);

        for (int i = 0; i < font_array.length; i++) {
            FontModel spinnerModel = new FontModel();
            spinnerModel.setFont_name(font_array[i]);
            fontModelArrayList.add(spinnerModel);
        }

        adapter = new FontAdapter(FontActivity.this, fontModelArrayList);
        recyclerView.setAdapter(adapter);

        adapter.setEventListener(new FontAdapter.EventListener() {
            @Override
            public void onItemViewClicked(int position) {

                Share.FONT_EFFECT = font_array[position].toString().toLowerCase();

                Typeface face = Typeface.createFromAsset(FontActivity.this.getAssets(), font_array[position].toLowerCase() + ".ttf");
                edtText.setTypeface(face);
            }

            @Override
            public void onDeleteMember(int position) {

            }
        });
    }


    @Override
    public void onClick(View v) {

        if (v == imgColor) {

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(imgColor.getWindowToken(), 0);
            ColorPickerDialogBuilder
                    .with(FontActivity.this)
                    .setTitle("Choose color")
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setOnColorSelectedListener(new OnColorSelectedListener() {
                        @Override
                        public void onColorSelected(int selectedColor) {
                        }
                    })
                    .setPositiveButton("ok", new ColorPickerClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                            edtText.setTextColor(selectedColor);
                            Share.COLOR = selectedColor;
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .build()
                    .show();
        } else if (v == imgClose) {

            onBackPressed();

        } else if (v == imgDone) {

            String str = edtText.getText().toString();

            Bitmap b2 = createBitmapFromLayoutWithText(getApplicationContext(), edtText.getText().toString(), edtText.getCurrentTextColor(), 0, edtText.getTypeface());
            Drawable d = new BitmapDrawable(getResources(), b2);

            if (!str.equals("")) {

                nextActivity();

            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.text_null), Toast.LENGTH_LONG).show();
            }

            DrawableSticker sticker = new DrawableSticker(d);


            if (Share.COLOR == 0) {
                Share.COLOR = getResources().getColor(R.color.colorPrimary);
            }

            Share.TEXT_DRAWABLE = sticker;
        }
    }

    private void nextActivity() {

        Share.FONT_FLAG = true;
        Share.FONT_TEXT = edtText.getText().toString();
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    public static Bitmap createBitmapFromLayoutWithText(Context context, String s, int color, int i, Typeface face) {

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.row_bitmap, null);

        TextView tv = (TextView) view.findViewById(R.id.tv_custom_text1);

        for (int j = 0; j < s.length(); j += 40) {
            if (s.length() >= 40) {
                if (j <= s.length() - 40) {
                    if (j == 0) {
                        String m = s.substring(0, 40);
                        tv.setText(m);
                    } else {
                        tv.append("\n");
                        String l = s.substring(j, j + 40);
                        tv.append(l);
                    }
                } else {
                    tv.append("\n");
                    String l = s.substring(j, s.length());
                    tv.append(l);
                }
            } else {
                tv.setText(s);
            }
        }
        tv.setTextColor(color);
        tv.setTypeface(face);
        view.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        view.draw(c);
        return bitmap;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);

        /*if (!ColorEffectsApplication.getInstance().requestNewInterstitial()) {

            // your code after close ad is here
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);

        } else {
            ColorEffectsApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();

                    ColorEffectsApplication.getInstance().mInterstitialAd.setAdListener(null);
                    ColorEffectsApplication.getInstance().mInterstitialAd = null;
                    ColorEffectsApplication.getInstance().ins_adRequest = null;
                    ColorEffectsApplication.getInstance().LoadAds();

                    // your code after close ad is here
                    finish();
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    Log.e("TAG", "onAdFailedToLoad: " + "");
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    Log.e("TAG", "onAdLoaded: " + "");
                }
            });
        }*/
    }
}
