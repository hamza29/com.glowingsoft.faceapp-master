package com.makemeold.agefaceeditor.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.makemeold.agefaceeditor.R;
import com.makemeold.agefaceeditor.activity.FullScreenImageScreen;
import com.makemeold.agefaceeditor.adapter.MyFavouriteAdapter;
import com.makemeold.agefaceeditor.database.DBAdapter;
import com.makemeold.agefaceeditor.share.Share;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FavouriteFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private MyFavouriteAdapter myFavouriteAdapter;
    private ArrayList<File> allphotoArraylist = new ArrayList<>();
    private ArrayList<File> dummyArraylist = new ArrayList<>();
    private RelativeLayout rl_my_photos;
    private LinearLayout ll_no_photos;
    DBAdapter dbAdapter;

    Cursor cursor;
    String data;
    private static final String KEY_IMAGE_PATH = "image_path";
    ImageView iv_all_delete;
    private List<String> listPermissionsNeeded = new ArrayList<>();
    public int posi;

    public static FavouriteFragment newInstance() {

        Bundle args = new Bundle();
        FavouriteFragment fragment = new FavouriteFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.gc();
        Runtime.getRuntime().gc();

        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        if (checkPermissions()) {

            dbAdapter = new DBAdapter(getActivity());
            findViews(view);

        }

        return view;
    }

    private void findViews(View view) {

        recyclerView =  view.findViewById(R.id.rcv_images);

        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rl_my_photos =  view.findViewById(R.id.rl_my_photos);
        ll_no_photos = view.findViewById(R.id.ll_no_photos);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(myFavouriteAdapter);
        iv_all_delete = (ImageView) getActivity().findViewById(R.id.iv_all_delete);
        iv_all_delete.setOnClickListener(this);
        Share.Fragment = "FavouriteFragment";
    }

    private void fill_Array() {

        dummyArraylist.clear();
        allphotoArraylist.clear();
        Share.al_my_photos_favourite.clear();

        cursor = dbAdapter.getFavData();

        Log.e("PATH------", cursor.getCount() + "fgbuh");

        if (cursor.getCount() == 0) {
            iv_all_delete.setAlpha(0.5f);
            iv_all_delete.setEnabled(false);
            rl_my_photos.setVisibility(View.GONE);
            ll_no_photos.setVisibility(View.VISIBLE);
        } else {
            if (cursor.moveToFirst()) {
                do {
                    data = cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH));
                    Log.e("PATH------", cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH)) + "");
                    File path = new File(data).getAbsoluteFile();

                    if (!path.exists()) {
                        try {
                            path.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    dummyArraylist.add(path);

                } while (cursor.moveToNext());

                iv_all_delete.setAlpha(1f);
                iv_all_delete.setEnabled(true);
                rl_my_photos.setVisibility(View.VISIBLE);
                ll_no_photos.setVisibility(View.GONE);
            }
        }

        for (int i = dummyArraylist.size() - 1; i >= 0; i--) {
            Log.e("TAG", "i1 = " + i + "");
            allphotoArraylist.add(dummyArraylist.get(i));
        }

        Share.al_my_photos_photo.clear();
        Share.al_my_photos_favourite.addAll(allphotoArraylist);

        myFavouriteAdapter = new MyFavouriteAdapter(getActivity(), allphotoArraylist, new MyFavouriteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                posi = position;
                go_on_fullscreenimage();
            }
        });

        recyclerView.setAdapter(myFavouriteAdapter);

    }


    private void go_on_fullscreenimage() {

        nextActivity();



    }

    private void nextActivity() {

        Intent intent = new Intent(getActivity(), FullScreenImageScreen.class);
        Share.Fragment = "FavouriteFragment";
        Share.my_favourite_position = posi;
        getActivity().startActivity(intent);
        Activity activity = (Activity) getActivity();
        activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    private boolean checkPermissions() {

        listPermissionsNeeded.clear();

        int writeStorage = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readStorage = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

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


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Log.e("TAG", "onRequestPermissionsResult: deny");

            } else {
                Log.e("TAG", "onRequestPermissionsResult: dont ask again");

                final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getContext());
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
                                        Uri.fromParts("package", getActivity().getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }

        } else {


        }

    }

    @Override
    public void onResume() {
        super.onResume();


        if (checkPermissions()) {
            Share.Fragment = "FavouriteFragment";
            fill_Array();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_all_delete:

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialog);
                builder.setMessage("Are you sure want to delete all Favourite photos ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dbAdapter.deleteFavData();
                        Log.e("count", dbAdapter.GetRowCountofTable() + "");
                        long count = dbAdapter.GetRowCountofTable();
                        if (count == 0) {
                            Log.e("count1", dbAdapter.GetRowCountofTable() + "");
                            iv_all_delete.setAlpha(0.5f);
                            iv_all_delete.setEnabled(false);
                            rl_my_photos.setVisibility(View.GONE);
                            ll_no_photos.setVisibility(View.VISIBLE);
                        }

                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                break;
        }
    }
}

