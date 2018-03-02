package me.xeno.unengtrainer.listener;

import me.xeno.unengtrainer.model.entity.FavouriteRecord;

/**
 * Created by Administrator on 2017/6/25.
 */

public interface OnFavItemSelectListener {
    void onSelect(FavouriteRecord record);

    void onItemLongClick(FavouriteRecord record);
}
