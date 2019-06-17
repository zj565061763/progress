package com.sd.lib.progress.seek;

import com.sd.lib.progress.ProgressView;

public interface SeekLayout extends ProgressView
{
    /**
     * 设置进度变化回调
     *
     * @param callback
     */
    void setOnProgressChangeCallback(OnProgressChangeCallback callback);

    /**
     * 返回当前方向{@link Orientation}
     *
     * @return
     */
    Orientation getOrientation();

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

    interface OnProgressChangeCallback
    {
        /**
         * 进度变化回调
         *
         * @param seekLayout
         * @param progress   进度[0-1]
         * @param isTouch    true-拖动
         */
        void onProgressChanged(SeekLayout seekLayout, int progress, boolean isTouch);
    }

    enum Orientation
    {
        Horizontal,
        Vertical
    }
}
