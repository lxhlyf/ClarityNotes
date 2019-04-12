package com.claritynotes.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.claritynotes.utils.UIUtils;

/**
 * Created by 简言 on 2018/10/23.
 * 努力吧 ！ 少年 ！
 */

public class SlideItemLayout extends FrameLayout {

    private View menuView;
    private View contentView;

    private int contentWidth;
    private int menuWidth;
    private int viewHeigh;  //高是相同的

    //手势监听器
    private GestureDetector detector;
    private Scroller scroller;

    private Context context;
    private boolean isMenu = false;

    public SlideItemLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        scroller = new Scroller(context);
    }

    /**
     * true : 拦截孩子的事件，但会执行当前控件的onTouchEven
     * false : 不拦截孩子的事件，事件将继续传递
     *
     * @param ev
     * @return
     */
    private int lastIntercepterX;
    private int lastIntercepterY;
    //因为拦截之后，onTouchEvent的ACTION_DOWM事件可能不被触发，所以需要同步初始值
    private int lastX;
    private int lastY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercepter = false;
        int X = (int) ev.getX();
        int Y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("onInterceptTouchEvent", "SlideLayout-onTouchEvent-ACTION_DOWN");
                if (mOnStateChangeListener != null) {
                    mOnStateChangeListener.onDown(this);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = Math.abs(X - lastIntercepterX);
                int deltaY = Math.abs(Y - lastIntercepterY);
                if (deltaX > deltaY && deltaX > UIUtils.dip2px(context, 8)) {
                    isIntercepter = true;
                } else {
                    isIntercepter = false;
                }
                break;
        }
        lastX = X;
        lastY = Y;
        lastIntercepterX = X;
        lastIntercepterY = Y;
        return isIntercepter;
    }

    private Long preTime;
    private Long nowTime;
    private int deltaX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //super.onTouchEvent(event);
        int X = (int) event.getX();
        int Y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                preTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                deltaX = X - lastX;
                int deltaY = Y - lastY;

                if (Math.abs(deltaX) > Math.abs(deltaY) && Math.abs(deltaX) > UIUtils.dip2px(context, 3)) {
                    //水平方向滑动
                    //响应侧滑
                    //反拦截---事件给SlideLayout
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                int scrollX = getScrollX() - deltaX;
                if (scrollX < 0) {
                    scrollX = 0;
                } else if (scrollX > menuWidth) {
                    scrollX = menuWidth;
                }
                //这里用scrollBy不方便
                scrollTo(scrollX, 0);
                break;
            case MotionEvent.ACTION_UP:
                nowTime = System.currentTimeMillis();

                Log.e("getScrollX", "" + getScrollX());

                if (deltaX==0  && nowTime - preTime < 500) {
                    performClick();
                }

                if (deltaX==0 && nowTime - preTime > 1200) {
                    performLongClick();
                }

                if (getScrollX() > menuWidth / 2) {
                    openMenu();
                } else {
                    closeMenu();
                }
                break;
        }
        lastX = X;
        lastY = Y;
        return true;
    }

    public void smoothScrollTo(int distenceX, int distenceY) {
        //这哥们应该会自己测定方向，偏移量为正就向左，为负就向右。
        scroller.startScroll(getScrollX(), getScrollY(), distenceX - getScrollX(), distenceY - getScrollY(), 300);
        invalidate();
    }

    public void openMenu() {
        scroller.startScroll(getScrollX(), getScrollY(), menuWidth - getScrollX(), 0, 300);
        invalidate();
        if (mOnStateChangeListener != null) {
            mOnStateChangeListener.onOpen(this);
        }

    }

    public void closeMenu() {
        scroller.startScroll(getScrollX(), getScrollY(), 0 - getScrollX(), 0, 300);
        invalidate();
        if (mOnStateChangeListener != null) {
            mOnStateChangeListener.onClose(this);
        }
    }

    @Override
    public void computeScroll() {
        //super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    /**
     * 当布局文件加载完成的时候，会回调这个方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //拿到内部子view的方法
        contentView = getChildAt(0);
        menuView = getChildAt(1);
    }

    /**
     * 在测量方法中得到各个控件的宽
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        contentWidth = contentView.getMeasuredWidth();
        menuWidth = menuView.getMeasuredWidth();
        viewHeigh = getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //指定菜单的位置
        menuView.layout(contentWidth, 0, contentWidth + menuWidth, viewHeigh);
    }

    /**
     * 监听SlideLayout状态的改变
     */
    private OnStateChangeListener mOnStateChangeListener;

    public interface OnStateChangeListener {
        void onClose(SlideItemLayout layout);

        void onOpen(SlideItemLayout layout);

        void onDown(SlideItemLayout layout);
    }

    public void setOnStateChangeListener(OnStateChangeListener mOnStateChangeListener) {
        this.mOnStateChangeListener = mOnStateChangeListener;
    }
}
