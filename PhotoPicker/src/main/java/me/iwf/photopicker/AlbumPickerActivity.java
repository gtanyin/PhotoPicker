package me.iwf.photopicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.adapter.PopupDirectoryListAdapter;
import me.iwf.photopicker.entity.PhotoDirectory;
import me.iwf.photopicker.utils.MediaStoreHelper;

import static me.iwf.photopicker.PhotoPicker.EXTRA_ORIGINAL_PHOTOS;
import static me.iwf.photopicker.PhotoPicker.EXTRA_SHOW_GIF;

/**
 * Created by ghtan on 2017/11/27.
 */

public class AlbumPickerActivity extends AppCompatActivity {

    public static final String PHOTOS_LIST = "photos_list";

    //所有photos的路径
    private List<PhotoDirectory> directories = new ArrayList<>();
    private PopupDirectoryListAdapter listAdapter;
    private Bundle mediaStoreArgs;

    private static AppCompatActivity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.__picker_activity_album_picker);
        activity = this;
        initView();
    }

    private void initView() {

        initIntent();

        listAdapter = new PopupDirectoryListAdapter(Glide.with(this), directories);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(listAdapter);

        mediaStoreArgs = new Bundle();
        mediaStoreArgs.putBoolean(EXTRA_SHOW_GIF, true);
        MediaStoreHelper.getPhotoDirs(this, mediaStoreArgs,
                new MediaStoreHelper.PhotosResultCallback() {
                    @Override
                    public void onResultCallback(List<PhotoDirectory> dirs) {
                        directories.clear();
                        directories.addAll(dirs);
                        listAdapter.notifyDataSetChanged();
                    }
                });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(newIntent(position));
            }
        });
    }

    private void initIntent() {
        List<String> pathList = getIntent().getStringArrayListExtra(EXTRA_ORIGINAL_PHOTOS);
        if (null != pathList && pathList.size() > 0) {
            PhotoPathsEntity.getInstance().addPaths(pathList);
        }
    }

    private Intent newIntent(int position) {
        String jsonString = new Gson().toJson(directories.get(position));
        Intent intent = new Intent(this, PhotoPickerActivity.class);
        intent.putExtra(PHOTOS_LIST, jsonString);
        return intent;
    }

    public static AppCompatActivity getActivity() {
        return activity;
    }
}
