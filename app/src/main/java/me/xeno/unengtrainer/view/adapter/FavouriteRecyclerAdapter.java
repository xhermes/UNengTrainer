package me.xeno.unengtrainer.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.model.entity.FavouriteRecord;
import me.xeno.unengtrainer.view.holder.FavouriteHolder;

/**
 * Created by xeno on 2017/5/22.
 */

public class FavouriteRecyclerAdapter extends RecyclerView.Adapter<FavouriteHolder>  {


    private Context mContext;

    private List<FavouriteRecord> dataList = new ArrayList<>();

    public FavouriteRecyclerAdapter() {
    }

    public void setDataList(List<FavouriteRecord> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void addDataToList(FavouriteRecord record) {
        dataList.add(record);
        notifyDataSetChanged();
    }

    @Override
    public FavouriteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_favourite, null);
        FavouriteHolder holder = new FavouriteHolder(view);

        mContext = parent.getContext();

        return holder;
    }

    @Override
    public void onBindViewHolder(FavouriteHolder holder, int position) {
        final FavouriteRecord record = dataList.get(position);

        holder.getNameView().setText(record.getName());
        holder.getModifyTimeView().setText(record.getModifyTime());

        holder.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 选择收藏列表项时
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
