package me.iwf.photopicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.adapter.PhotoGridAdapter;
import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.entity.PhotoDirectory;
import me.iwf.photopicker.event.OnItemCheckListener;
import me.iwf.photopicker.event.OnPhotoClickListener;

import static me.iwf.photopicker.PhotoPicker.EXTRA_ORIGINAL_PHOTOS;

/**
 * Created by ghtan on 2017/11/27.
 */

public class PhotoPickerActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private PhotoGridAdapter photoGridAdapter;
    private TextView confirmBT;
    private RequestManager mGlideRequestManager;
    private PhotoDirectory photoDirectory;
    private List<Photo> photos = new ArrayList<>();
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

        photoGridAdapter.setOnPhotoClickListener(new OnPhotoClickListener() {
            @Override
            public void onClick(View v, int position) {
                Toast.makeText(PhotoPickerActivity.this, "你点击了第" + position + "个Item", Toast.LENGTH_SHORT).show();
            }
        });

        photoGridAdapter.setOnItemCheckListener(new OnItemCheckListener() {
            @Override
            public void onItemCheck(int position, Photo photo) {
                updateNumber();
            }

            @Override
            public void onItemUnCheck(int position, Photo photo) {
                updateNumber();
            }
        });

        confirmBT.setOnClickListener(this);
    }

    private void initView() {
        initDirectories();
        originalPhotos = getIntent().getStringArrayListExtra(EXTRA_ORIGINAL_PHOTOS);


        ((TextView) findViewById(R.id.album_name)).setText(photoDirectory.getName());
        confirmBT = (TextView) findViewById(R.id.confirm);
        recyclerView = (RecyclerView) findViewById(R.id.rv_photos);

        mGlideRequestManager = Glide.with(this);
        photoGridAdapter = new PhotoGridAdapter(this, mGlideRequestManager, photos, originalPhotos, 4);
        photoGridAdapter.setPreviewEnable(true);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(photoGridAdapter);

        //更新已选择的数字
        updateNumber();
    }

    private void initDirectories() {
        String photosJson = getIntent().getStringExtra(AlbumPickerActivity.PHOTOS_LIST);
        photoDirectory = new Gson().fromJson(photosJson, PhotoDirectory.class);
        photos = photoDirectory.getPhotos();
    }

    private void updateNumber() {
        int size = PhotoPathsEntity.getInstance().getSize();
        if (size == 0) {
            confirmBT.setText(getString(R.string.__picker_yes));
        } else {
            confirmBT.setText(getString(R.string.__picker_confirm, size));
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
        } else if (i == R.id.confirm) {
            Toast.makeText(this, "你选择了" + PhotoPathsEntity.getInstance().getPaths() + "张图片", Toast.LENGTH_SHORT).show();
            finish();
            AlbumPickerActivity.getActivity().finish();
        }
    }
}
