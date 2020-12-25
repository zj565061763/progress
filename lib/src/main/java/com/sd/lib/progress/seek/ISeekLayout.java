package com.sd.lib.progress.seek;

import android.view.View;

import com.sd.lib.progress.core.IProgressView;

public interface ISeekLayout extends IProgressView
{
    /**
     * 返回当前方向{@link Orientation}
     *
     * @return
     */
    Orientation getOrientation();

    /**
     * 是否正在触摸
     *
     * @return
     */
    boolean isTouching();

    /**
     * 设置进度变化回调
     *
     * @param callback
     */
    void setOnProgressChangeCallback(OnProgressChangeCallback callback);

    /**
     * 设置触摸回调
     *
     * @param callback
     */
    void setOnTrackingTouchCallback(OnTrackingTouchCallback callback);

    /**
     * 设置方向，默认{@link Orientation#Horizontal}
     *
     * @param orientation
     */
    void setOrientation(Orientation orientation);

    /**
     * 设置是否可以触摸，默认true
     *
     * @param isTouchable
     */
    void setTouchable(boolean isTouchable);

    /**
     * 设置是否同步进度给Child，默认true-同步
     *
     * @param synchronize
     */
    void setSynchronizeProgress(boolean synchronize);

    /**
     * 设置手柄View
     *
     * @param view
     */
    void setThumbView(View view);

    interface OnProgressChangeCallback
    {
        /**
         * 进度变化回调
         *
         * @param seekLayout
         * @param progress   进度[0-1]
         * @param isTouch    true-拖动
         */
        void onProgressChanged(ISeekLayout seekLayout, int progress, boolean isTouch);
    }

    /**
     * 触摸回调
     */
    interface OnTrackingTouchCallback
    {
        /**
         * 开始触摸
         *
         * @param seekLayout
         */
        void onStartTrackingTouch(ISeekLayout seekLayout);

        /**
         * 停止触摸
         *
         * @param seekLayout
         * @param hasActionMove 开始到停止的过程中是否触发过{@link android.view.MotionEvent#ACTION_MOVE}，true-触发过
         */
        void onStopTrackingTouch(ISeekLayout seekLayout, boolean hasActionMove);
    }

    enum Orientation
    {
        Horizontal,
        Vertical
    }
}
