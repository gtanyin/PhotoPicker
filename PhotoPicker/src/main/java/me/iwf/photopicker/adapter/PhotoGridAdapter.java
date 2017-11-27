package me.iwf.photopicker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPathsEntity;
import me.iwf.photopicker.R;
import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.event.OnItemCheckListener;
import me.iwf.photopicker.event.OnPhotoClickListener;
import me.iwf.photopicker.utils.AndroidLifecycleUtils;

/**
 * Created by donglua on 15/5/31.
 */
public class PhotoGridAdapter extends SelectableAdapter<PhotoGridAdapter.PhotoViewHolder> {

    private RequestManager glide;

    private OnItemCheckListener onItemCheckListener = null;
    private OnPhotoClickListener onPhotoClickListener = null;

    public final static int ITEM_TYPE_PHOTO = 101;
    private List<Photo> photos;

    private boolean previewEnable = true;

    private int imageSize;


    public PhotoGridAdapter(Context context, RequestManager requestManager, List<Photo> photos, ArrayList<String> orginalPhotos, int columnNumber) {
        this.photos = photos;
        this.glide = requestManager;
        setColumnNumber(context, columnNumber);

        if (orginalPhotos != null) photosPathEntity.addPaths(orginalPhotos);
    }

    private void setColumnNumber(Context context, int columnNumber) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = widthPixels / columnNumber;
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE_PHOTO;
    }


    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.__picker_item_photo, parent, false);
        final PhotoViewHolder holder = new PhotoViewHolder(itemView);
        return holder;
    }


    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, int position) {
            final Photo photo;
            photo = photos.get(position);
            boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(holder.ivPhoto.getContext());

            if (canLoadImage) {
                final RequestOptions options = new RequestOptions();
                options.centerCrop()
                        .dontAnimate()
                        .override(imageSize, imageSize)
                        .placeholder(R.color.__picker_loading_bg)
                        .error(R.drawable.__picker_ic_broken_image_black_48dp);

                glide.setDefaultRequestOptions(options)
                        .load(new File(photo.getPath()))
                        .thumbnail(0.5f)
                        .into(holder.ivPhoto);
            }



            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onPhotoClickListener != null) {
                        int pos = holder.getAdapterPosition();
                        if (previewEnable) {
                            onPhotoClickListener.onClick(view, pos);
                        } else {
                            holder.vSelected.performClick();
                        }
                    }
                }
            });
            holder.vSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getAdapterPosition();

                    toggleSelection(photo);
                    notifyItemChanged(pos);

                    if (onItemCheckListener != null) {
                        if (photosPathEntity.isAdded(photo.getPath())) {
                            onItemCheckListener.onItemCheck(pos, photo);
                        } else {
                            onItemCheckListener.onItemUnCheck(pos, photo);
                        }
                    }
                }
            });
            holder.vSelected.setSelected(PhotoPathsEntity.getInstance().isAdded(photo.getPath()));
    }


    @Override
    public int getItemCount() {
        return photos.size();
    }


    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private View vSelected;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            vSelected = itemView.findViewById(R.id.v_selected);
        }
    }


    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        this.onItemCheckListener = onItemCheckListener;
    }


    public void setOnPhotoClickListener(OnPhotoClickListener onPhotoClickListener) {
        this.onPhotoClickListener = onPhotoClickListener;
    }

    public void setPreviewEnable(boolean previewEnable) {
        this.previewEnable = previewEnable;
    }

    @Override
    public void onViewRecycled(PhotoViewHolder holder) {
        glide.clear(holder.ivPhoto);
        super.onViewRecycled(holder);
    }
}
