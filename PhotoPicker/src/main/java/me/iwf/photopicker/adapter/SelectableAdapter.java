package me.iwf.photopicker.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPathsEntity;
import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.event.Selectable;

public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements Selectable {

    private static final String TAG = SelectableAdapter.class.getSimpleName();

    protected PhotoPathsEntity photosPathEntity;


    public SelectableAdapter() {
        photosPathEntity = PhotoPathsEntity.getInstance();
    }


    /**
     * Indicates if the item at position where is selected
     *
     * @param photo Photo of the item to check
     * @return true if the item is selected, false otherwise
     */
    @Override
    public boolean isSelected(Photo photo) {
        return photosPathEntity.isAdded(photo.getPath());
    }

    /**
     * Toggle the selection status of the item at a given position
     *
     * @param photo Photo of the item to toggle the selection status for
     */
    @Override
    public void toggleSelection(Photo photo) {
        if (photosPathEntity.isAdded(photo.getPath())) {
            photosPathEntity.removePath(photo.getPath());
        } else {
            photosPathEntity.addPath(photo.getPath());

        }
    }


    /**
     * Clear the selection status for all items
     */
    @Override
    public void clearSelection() {
        photosPathEntity.removeAll();
    }


    /**
     * Count the selected items
     *
     * @return Selected items count
     */
    @Override
    public int getSelectedItemCount() {
        return photosPathEntity.getSize();
    }


    public List<String> getSelectedPhotos() {
        return photosPathEntity.getPaths();
    }

}