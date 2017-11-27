package me.iwf.photopicker;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.adapter.PhotoPagerAdapter;
import me.iwf.photopicker.entity.PhotoPathsEntity;

import static me.iwf.photopicker.PhotoPicker.EXTRA_CURRENT_ITEM;
import static me.iwf.photopicker.PhotoPicker.EXTRA_PHOTOS;


/**
 * Created by donglua on 15/6/24.
 */
public class PhotoPagerActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private TextView confirmBT;
    private ImageView selectIV;

    private PhotoPagerAdapter mPagerAdapter;
    private List<String> paths = new ArrayList<>();
    private PhotoPathsEntity photoPathsEntity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.__picker_activity_photo_pager);

        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        findViewById(R.id.title_finish).setOnClickListener(this);
        selectIV.setOnClickListener(this);
        confirmBT.setOnClickListener(this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                selectIV.setSelected(photoPathsEntity.isAdded(paths.get(i)));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void initData() {
        photoPathsEntity = PhotoPathsEntity.getInstance();

        int currentPosition = getIntent().getIntExtra(EXTRA_CURRENT_ITEM, -1);
        if (currentPosition == -1) {
            paths.addAll(PhotoPathsEntity.getInstance().getPaths());
            viewPager.setCurrentItem(0);
            selectIV.setSelected(photoPathsEntity.isAdded(paths.get(0)));
        } else {
            paths.addAll(getIntent().getStringArrayListExtra(EXTRA_PHOTOS));
            viewPager.setCurrentItem(currentPosition, false);
            selectIV.setSelected(photoPathsEntity.isAdded(paths.get(currentPosition)));
        }
        mPagerAdapter.notifyDataSetChanged();




    }

    private void initView() {
        confirmBT = (TextView) findViewById(R.id.confirm);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        selectIV = (ImageView) findViewById(R.id.v_selected);
        mPagerAdapter = new PhotoPagerAdapter(Glide.with(this), paths);
        viewPager.setAdapter(mPagerAdapter);
        updateNumber();
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
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.title_finish) {
            finish();
        } else if (i == R.id.v_selected) {
            String pathSelected = paths.get(viewPager.getCurrentItem());
            boolean isAdded = photoPathsEntity.isAdded(pathSelected);
            if (isAdded){
                selectIV.setSelected(false);
                photoPathsEntity.removePath(pathSelected);
            }else {
                selectIV.setSelected(true);
                photoPathsEntity.addPath(pathSelected);
            }
            updateNumber();
        }

    }
}
