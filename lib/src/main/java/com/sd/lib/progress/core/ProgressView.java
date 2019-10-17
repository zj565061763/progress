package com.sd.lib.progress.core;

public interface ProgressView
{
    /**
     * 返回最大进度，默认100
     *
     * @return
     */
    int getMax();

    /**
     * 返回最小进度，默认100
     *
     * @return
     */
    int getMin();

    /**
     * 返回当前的进度
     *
     * @return
     */
    int getProgress();

    /**
     * 返回进度百分比[0-1]
     *
     * @return
     */
    float getProgressPercent();

    /**
     * 返回百分比对应的进度值
     *
     * @param percent [0-1]
     * @return
     */
    int getPercentProgress(float percent);

    /**
     * 设置最大进度，默认100
     *
     * @param max
     * @return true-值发生变化
     */
    boolean setMax(int max);

    /**
     * 设置最小进度，默认0
     *
     * @param min
     * @return true-值发生变化
     */
    boolean setMin(int min);

    /**
     * 设置进度
     *
     * @param progress
     * @return true-进度发生变化
     */
    boolean setProgress(int progress);

    /**
     * 设置进度拦截器
     *
     * @param progressInterceptor
     */
    void setProgressInterceptor(ProgressInterceptor progressInterceptor);

    interface ProgressInterceptor
    {
        /**
         * 是否拦截进度设置
         *
         * @param progressView
         * @param futureProgress 即将被设置的进度
         * @return true-拦截掉
         */
        boolean interceptProgress(ProgressView progressView, int futureProgress);
    }
}
