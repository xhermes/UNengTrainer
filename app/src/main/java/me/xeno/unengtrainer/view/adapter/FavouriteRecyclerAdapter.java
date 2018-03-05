package me.xeno.unengtrainer.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.listener.OnFavItemSelectListener;
import me.xeno.unengtrainer.model.entity.FavouriteRecord;
import me.xeno.unengtrainer.view.holder.FavouriteHolder;

/**
 * Created by xeno on 2017/5/22.
 */

public class FavouriteRecyclerAdapter extends RecyclerView.Adapter<FavouriteHolder>  {


    private boolean mEditMode = false;

    private Context mContext;

    private OnFavItemSelectListener mListener;

    private List<FavouriteRecord> dataList = new ArrayList<>();

    public FavouriteRecyclerAdapter(OnFavItemSelectListener listener) {
        mListener = listener;
    }

    public void setDataList(List<FavouriteRecord> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void addDataToList(FavouriteRecord record) {
        dataList.add(record);
        notifyDataSetChanged();
    }

    public List<FavouriteRecord> getDataList() {
        return dataList;
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

        holder.getElevationAngleView().setText("仰角：" + record.getElevationAngle());
        holder.getSwingAngleView().setText("摆角：" + record.getSwingAngle());
        holder.getLeftSpeedView().setText("左转速：" + record.getLeftMotorSpeed());
        holder.getRightSpeedView().setText("右转速：" + record.getRightMotorSpeed());

        //长按进入编辑模式才显示checkbox显示
        if(mEditMode) {
            holder.getCheckBox().setVisibility(View.VISIBLE);
        } else {
            holder.getCheckBox().setVisibility(View.GONE);
        }

        if(mEditMode) {
            if(record.getChecked())
                holder.getCheckBox().setChecked(true);
            else
                holder.getCheckBox().setChecked(false);
        }

        holder.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSelect(record);
            }
        });

        holder.getRootView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //如果已经处在编辑模式，不响应长按事件，不消费事件
                if(mEditMode)
                    return false;

                //长按后要通知上层RecyclerView，还要通知Activity
                mListener.onItemLongClick(record);
                notifyDataSetChanged();

                mEditMode = true;

                //由子View消费长按事件
                return true;
            }
        });
        holder.getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                record.setChecked(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public boolean isEditMode() {
        return mEditMode;
    }

    public void setEditMode(boolean editMode) {
        this.mEditMode = editMode;
    }
}
