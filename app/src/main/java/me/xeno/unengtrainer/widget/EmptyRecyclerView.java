package me.xeno.unengtrainer.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xeno on 2017/4/12.
 * 2017/05/09 修改触摸事件以适应嵌套scrollview
 */

public class EmptyRecyclerView extends RecyclerView implements ObservableView {

    private float downX;
    private float downY;
    /** 第一个可见的item的位置 */
    private int firstVisibleItemPosition;
    /** 第一个的位置 */
    private int[] firstPositions;
    /** 最后一个可见的item的位置 */
    private int lastVisibleItemPosition;
    /** 最后一个的位置 */
    private int[] lastPositions;
    private boolean isTop;
    private boolean isBottom;

    /**
     * 当数据为空时展示的View
     */
    private View mEmptyView;

    /**
     * 每次notifyDataChanged的时候，系统都会调用这个观察者的onChange函数
     */
    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {


        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            //判断数据为空否，在进行显示或者隐藏
            if (adapter != null && mEmptyView != null) {
                if (adapter.getItemCount() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    EmptyRecyclerView.this.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    EmptyRecyclerView.this.setVisibility(View.VISIBLE);
                }
            }

        }
    };

    public EmptyRecyclerView(Context context) {
        super(context);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @param emptyView 展示的空view
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }


    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if (adapter != null) {
            // TODO 这里用了观察者模式，同时把这个观察者添加进去，
            // TODO 至于这个模式怎么用，谷歌一下，不多讲了，因为这个涉及到了Adapter的一些原理，感兴趣可以点进去看看源码，还是受益匪浅的
//            if(!adapter.hasObservers())
            adapter.registerAdapterDataObserver(emptyObserver);
        }
        //当setAdapter的时候也调一次
        emptyObserver.onChanged();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
            } else if (layoutManager instanceof LinearLayoutManager) {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (lastPositions == null) {
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                    firstPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                staggeredGridLayoutManager.findFirstVisibleItemPositions(firstPositions);
                lastVisibleItemPosition = findMax(lastPositions);
                firstVisibleItemPosition = findMin(firstPositions);
            }
        } else {
            throw new RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                //如果滑动到了最底部，就允许继续向上滑动加载下一页，否者不允许
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = ev.getX() - downX;
                float dy = ev.getY() - downY;
                boolean allowParentTouchEvent;
                if (Math.abs(dy) >= Math.abs(dx)) {
                    if (dy > 0) {
                        //位于顶部时下拉，让父View消费事件
                        allowParentTouchEvent = isTop = firstVisibleItemPosition == 0 && getChildAt(0).getTop() >= 0;
                    } else {
                        //位于底部时上拉，让父View消费事件
                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                       isBottom = visibleItemCount > 0 && (lastVisibleItemPosition) >= totalItemCount - 1 && getChildAt(getChildCount() - 1).getBottom() <= getHeight();
                        allowParentTouchEvent = isBottom && dy < 0;
                    }
                } else {
                    //水平方向滑动
                    allowParentTouchEvent = true;
                }
                getParent().requestDisallowInterceptTouchEvent(!allowParentTouchEvent);
        }
        return super.dispatchTouchEvent(ev);

    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value >= max) {
                max = value;
            }
        }
        return max;
    }

    private int findMin(int[] firstPositions) {
        int min = firstPositions[0];
        for (int value : firstPositions) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    @Override
    public boolean isTop() {
        return isTop;
    }

    @Override
    public boolean isBottom() {
        return isBottom;
    }

    @Override
    public void goTop() {
        scrollToPosition(0);
    }
}