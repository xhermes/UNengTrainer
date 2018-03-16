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
        //super只是打印了一句警告应当重写本方法的Log，什么事都没做
//        super.onLayoutChildren(recycler, state);
        Logger.info("onLayoutChildren()开始:");

        if(getItemCount() == 0 && state.isPreLayout()) {
            Logger.info("onLayoutChildren被跳过");
            //state.isPreLayout() 是判断之前布局时动画有没有处理结束，什么意思？
            //没有item，且在预布局时，直接跳过onLayoutChildren，为什么？
            return;
        }

        detachAndScrapAttachedViews(recycler);
        int totalHeight = 0;//记录现在布局到的高度，用于布局下一个view时参考

        Logger.info("getItemCount():" + getItemCount());
        Logger.info("getChildCount():" + getChildCount());
        Logger.info("scrapList size:" + recycler.getScrapList().size());
        //循环布局view，相当于LinearLayoutManager中的layoutTrunk()
        //网文中此处是循环次数是getItemCount，事实上不应该是，getItemCount取到的是总的数据数量，而
        //在此处应该使用屏幕内可见的数量
        for (int i = 0; i < getItemCount(); i++) {
            View v = recycler.getViewForPosition(i);
            addView(v);//对应于detachAndScrpAttachedViews()，实际是在操作Bucket
            measureChildWithMargins(v, 0, 0); //测量View大小，考虑Margin
            int width = getDecoratedMeasuredWidth(v); // 计算view实际大小，包括了ItemDecorator中设置的偏移量。
            int height = getDecoratedMeasuredHeight(v);
            Logger.info("width " + width + ", height " + height);
            Logger.info("totalHeight: " + totalHeight);
            layoutDecorated(v, 0, totalHeight, width, totalHeight + height);
            totalHeight+= height;
        }
    }

    private void fill() {

    }

    private void layoutChunk() {

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
