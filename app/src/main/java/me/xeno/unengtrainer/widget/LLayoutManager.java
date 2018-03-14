package me.xeno.unengtrainer.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import me.xeno.unengtrainer.util.Logger;

/**
 * Created by xeno on 2018/3/14.
 */

public class LLayoutManager extends RecyclerView.LayoutManager {
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        //子View没有LayoutParams参数的时候会默认生成一个
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        Logger.info("onLayoutChildren()开始:");
        detachAndScrapAttachedViews(recycler);
        int totalHeight = 0;//记录现在布局到的高度，用于布局下一个view时参考

        Logger.info("getItemCount():" + getItemCount());
        Logger.info("scrapList size:" + recycler.getScrapList().size());
        for (int i = 0; i < getItemCount(); i++) {
            View v = recycler.getViewForPosition(i);
            addView(v);
            measureChildWithMargins(v, 0, 0); // 通知测量view的margin值
            int width = getDecoratedMeasuredWidth(v); // 计算view实际大小，包括了ItemDecorator中设置的偏移量。
            int height = getDecoratedMeasuredHeight(v);
            Logger.info("width " + width + ", height " + height);
            Logger.info("totalHeight: " + totalHeight);
            layoutDecorated(v, 0, totalHeight, width, totalHeight + height);
        }
    }

    @Override
    public boolean canScrollHorizontally() {
        return super.canScrollHorizontally();
    }

    @Override
    public boolean canScrollVertically() {
        return super.canScrollVertically();
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.scrollVerticallyBy(dy, recycler, state);
    }
}
