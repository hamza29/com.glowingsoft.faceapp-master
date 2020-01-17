package com.makemeold.agefaceeditor.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.makemeold.agefaceeditor.adapter.MyPhotosAdapter;
import com.makemeold.agefaceeditor.database.DBAdapter;
import com.makemeold.agefaceeditor.share.Share;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyPhotosFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private MyPhotosAdapter myPhotosAdapter;
    private ArrayList<File> almyphotoArraylist = new ArrayList<>();
    private File[] allFiles;
    private RelativeLayout rlPhoto;
    private LinearLayout lnnoPhoto;
    DBAdapter dbAdapter;

    ImageView imgallDelet;
    public int posi;

    private List<String> listPermissionsNeeded = new ArrayList<>();

    public static MyPhotosFragment newInstance() {
        Bundle args = new Bundle();
        MyPhotosFragment fragment = new MyPhotosFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_photo, container, false);

        if (checkAndRequestPermissions()) {

            findViews(view);

        } else {

        }
        return view;
    }

    private void findViews(View view) {

        dbAdapter = new DBAdapter(getActivity());
        recyclerView = view.findViewById(R.id.rcv_images);

        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rlPhoto = view.findViewById(R.id.rl_my_photos);
        lnnoPhoto = view.findViewById(R.id.ll_no_photos);
        recyclerView.setLayoutManager(gridLayoutManager);
        imgallDelet = getActivity().findViewById(R.id.iv_all_delete);
        imgallDelet.setOnClickListener(this);

        Share.Fragment = "MyPhotosFragment";
    }


    private void setData() {

        File path = new File(Share.IMAGE_PATH);

        almyphotoArraylist.clear();
        Share.al_my_photos_photo.clear();
        allFiles = null;

        if (path.exists()) {

            Log.e("if1", "if1");
            allFiles = path.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"));
                }
            });

            Log.e("array_size", "" + allFiles.length);
            if (allFiles.length > 0) {
                Log.e("if2", "if2");

                rlPhoto.setVisibility(View.VISIBLE);
                lnnoPhoto.setVisibility(View.GONE);
                imgallDelet.setAlpha(1f);
                imgallDelet.setEnabled(true);
                for (int i = 0; i < allFiles.length; i++) {
                    almyphotoArraylist.add(allFiles[i]);
                    ///Now set this bitmap on imageview
                }

                Share.al_my_photos_favourite.clear();
                Collections.sort(almyphotoArraylist, Collections.reverseOrder());
                Share.al_my_photos_photo.addAll(almyphotoArraylist);
                myPhotosAdapter = new MyPhotosAdapter(getActivity(), Share.al_my_photos_photo, new MyPhotosAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        posi = position;
                        go_on_fullscreenimage();
                    }
                });
                recyclerView.setAdapter(myPhotosAdapter);
            } else {
                Log.e("else2", "else2");
                almyphotoArraylist.clear();
                imgallDelet.setAlpha(0.5f);
                imgallDelet.setEnabled(false);
                rlPhoto.setVisibility(View.GONE);
                lnnoPhoto.setVisibility(View.VISIBLE);
            }
        } else {

            Log.e("else1", "else1");
            almyphotoArraylist.clear();
            imgallDelet.setAlpha(0.5f);
            imgallDelet.setEnabled(false);
            rlPhoto.setVisibility(View.GONE);
            lnnoPhoto.setVisibility(View.VISIBLE);
        }
    }

    private void go_on_fullscreenimage() {

        nextActivity();
    }

    private void nextActivity() {

        Intent intent = new Intent(getActivity(), FullScreenImageScreen.class);
        Share.Fragment = "MyPhotosFragment";
        Share.my_photos_position = posi;
        getActivity().startActivity(intent);
        Activity activity = (Activity) getActivity();
        activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_all_delete:

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialog);
                builder.setMessage("Are you sure want to delete all photos ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for (int i = 0; i < almyphotoArraylist.size(); i++) {

                            File f = new File(String.valueOf(almyphotoArraylist.get(i)));
                            Log.e("images file 12345 :  ", " ==============" + almyphotoArraylist.get(i) + "  -----------" + f.toString());

                            f.delete();
                            almyphotoArraylist.get(i).delete();
                            //al_my_photos.remove(i);

                        }
                        almyphotoArraylist.clear();

                        dbAdapter.deleteFavData();
                        Log.e("count", dbAdapter.GetRowCountofTable() + "");
                        long count = dbAdapter.GetRowCountofTable();

                        if (count == 0) {

                            Log.e("count1", dbAdapter.GetRowCountofTable() + "");
                            imgallDelet.setAlpha(0.5f);
                            imgallDelet.setEnabled(false);
                            rlPhoto.setVisibility(View.GONE);
                            lnnoPhoto.setVisibility(View.VISIBLE);
                        }

                        if (almyphotoArraylist.size() == 0) {
                            imgallDelet.setAlpha(0.5f);
                            imgallDelet.setEnabled(false);
                            rlPhoto.setVisibility(View.GONE);
                            lnnoPhoto.setVisibility(View.VISIBLE);
                        }
                        setData();
                        myPhotosAdapter.notifyDataSetChanged();


                        almyphotoArraylist.clear();
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

    private boolean checkAndRequestPermissions() {

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

        if (checkAndRequestPermissions()) {
            Share.Fragment = "MyPhotosFragment";


            setData();
            Log.e("fragment", "onresume");
        }

    }

}
