package me.iwf.photopicker.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghtan on 2017/11/27.
 */

public class PhotoPathsEntity {
    private static PhotoPathsEntity instance;
    private List<String> mPaths = new ArrayList<>();

    public static PhotoPathsEntity getInstance() {
        if (null == instance) {
            instance = new PhotoPathsEntity();
        }
        return instance;
    }

    public List<String> getPaths() {
        return mPaths;
    }

    public void addPaths(List<String> paths) {
        for (String path : paths) {
            if (!mPaths.contains(path)) {
                mPaths.add(path);
            }
        }
    }

    public void addPath(String path) {
        if (!mPaths.contains(path)) {
            mPaths.add(path);
        }
    }

    public int getSize() {
        return mPaths.size();
    }

    public boolean isAdded(String path) {
        return mPaths.contains(path);
    }

    public void removePath(String path) {
        mPaths.remove(path);
    }

    public void removeAll() {
        mPaths.clear();
    }
}
