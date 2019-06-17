package com.sd.lib.progress;

public interface ProgressView
{
    /**
     * 返回当前的进度
     *
     * @return
     */
    int getProgress();

    /**
     * 返回最小进度，默认0
     *
     * @return
     */
    int getMinProgress();

    /**
     * 返回最大进度，默认100
     *
     * @return
     */
    int getMaxProgress();

    /**
     * 返回进度百分比[0-1]
     *
     * @return
     */
    float getProgressPercent();

    /**
     * 设置进度
     *
     * @param progress
     * @return true-进度发生变化
     */
    boolean setProgress(int progress);

    /**
     * 设置最小进度，默认0
     *
     * @param progress
     */
    void setMinProgress(int progress);

    /**
     * 设置最大进度，默认100
     *
     * @param progress
     */
    void setMaxProgress(int progress);
}
