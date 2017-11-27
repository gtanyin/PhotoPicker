package me.iwf.photopicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.adapter.PhotoGridAdapter;
import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.entity.PhotoDirectory;
import me.iwf.photopicker.event.OnItemCheckListener;
import me.iwf.photopicker.utils.MediaStoreHelper;

import static me.iwf.photopicker.PhotoPicker.EXTRA_ORIGINAL_PHOTOS;
import static me.iwf.photopicker.PhotoPicker.EXTRA_SHOW_GIF;

/**
 * Created by ghtan on 2017/11/27.
 */

public class PhotoPickerActy extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private PhotoGridAdapter photoGridAdapter;
    private TextView confirmBT;
    private RequestManager mGlideRequestManager;
    private List<PhotoDirectory> directories = new ArrayList<>();
    private ArrayList<String> originalPhotos = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__picker_activity_photoo_picker);
        initView();
        initEvent();
    }

    private void initEvent() {
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.title_finish).setOnClickListener(this);

        photoGridAdapter.setOnItemCheckListener(new OnItemCheckListener() {
            @Override
            public boolean onItemCheck(int position, Photo path, int selectedItemCount) {
                updateNumber(selectedItemCount);
                return true;
            }
        });

        confirmBT.setOnClickListener(this);
    }

    private void initView() {
        initDirectories();
        originalPhotos = getIntent().getStringArrayListExtra(EXTRA_ORIGINAL_PHOTOS);

        ((TextView) findViewById(R.id.album_name)).setText(getIntent().getStringExtra(AlbumPickerActivity.ALBUM_NAME));
        confirmBT = (TextView) findViewById(R.id.confirm);
        recyclerView = (RecyclerView) findViewById(R.id.rv_photos);

        mGlideRequestManager = Glide.with(this);
        photoGridAdapter = new PhotoGridAdapter(this, mGlideRequestManager, directories, originalPhotos, 4);
        photoGridAdapter.setShowCamera(false);
        photoGridAdapter.setPreviewEnable(true);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(photoGridAdapter);
    }

    private void initDirectories() {
        Bundle mediaStoreArgs = new Bundle();
        mediaStoreArgs.putBoolean(EXTRA_SHOW_GIF, true);
        MediaStoreHelper.getPhotoDirs(this, mediaStoreArgs,
                new MediaStoreHelper.PhotosResultCallback() {
                    @Override
                    public void onResultCallback(List<PhotoDirectory> dirs) {
                        directories.clear();
                        directories.addAll(dirs);
                        photoGridAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void updateNumber(int selectedSize) {
        if (selectedSize == 0) {
            confirmBT.setText(getString(R.string.__picker_yes));
        } else {
            confirmBT.setText(getString(R.string.__picker_confirm, selectedSize));
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cancel) {
            finish();
            AlbumPickerActivity.getActivity().finish();
        } else if (i == R.id.title_finish) {
            finish();
        }else if (i ==R.id.confirm){
            List list=photoGridAdapter.getSelectedPhotoPaths();
            Toast.makeText(this,"你选择了"+list.size()+"张图片",Toast.LENGTH_SHORT).show();
            finish();
            AlbumPickerActivity.getActivity().finish();
        }
    }
}
