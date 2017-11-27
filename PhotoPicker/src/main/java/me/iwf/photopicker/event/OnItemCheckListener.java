package me.iwf.photopicker.event;

import me.iwf.photopicker.entity.Photo;

/**
 * Created by donglua on 15/6/20.
 */
public interface OnItemCheckListener {
  void onItemCheck(int position, Photo photo);
  void onItemUnCheck(int position, Photo photo);
}
