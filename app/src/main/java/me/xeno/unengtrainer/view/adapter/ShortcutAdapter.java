package me.xeno.unengtrainer.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.view.holder.ShortcutHolder;

/**
 * Created by xeno on 2017/5/16.
 */

public class ShortcutAdapter extends RecyclerView.Adapter<ShortcutHolder> {

    @Override
    public ShortcutHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_shortcut, null);
        ShortcutHolder holder = new ShortcutHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ShortcutHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
