package me.iwf.photopicker;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.adapter.PhotoPagerAdapter;
import me.iwf.photopicker.fragment.ImagePagerFragment;

import static me.iwf.photopicker.PhotoPreview.EXTRA_CURRENT_ITEM;
import static me.iwf.photopicker.PhotoPreview.EXTRA_PHOTOS;

/**
 * Created by donglua on 15/6/24.
 */
public class PhotoPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PhotoPagerAdapter mPagerAdapter;
    private List<String> paths = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.__picker_activity_photo_pager);

        initView();
        initData();
    }

    private void initData() {
        int currentPosition = getIntent().getIntExtra(EXTRA_CURRENT_ITEM, -1);
        if (currentPosition == -1) {
            viewPager.setCurrentItem(0);
            paths = PhotoPathsEntity.getInstance().getPaths();
        } else {
            viewPager.setCurrentItem(currentPosition, false);
            paths = getIntent().getStringArrayListExtra(EXTRA_PHOTOS);
        }
        mPagerAdapter.notifyDataSetChanged();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new PhotoPagerAdapter(Glide.with(this), paths);
        viewPager.setAdapter(mPagerAdapter);
    }

}
