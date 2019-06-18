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
     * 返回最大进度，默认100
     *
     * @return
     */
    int getMax();

    /**
     * 返回限制的最小进度值，默认为0
     *
     * @return
     */
    int getLimitMin();

    /**
     * 返回限制的最大进度值，即和Max一致
     *
     * @return
     */
    int getLimitMax();

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
     * 设置最大进度，默认100
     *
     * @param max
     */
    void setMax(int max);

    /**
     * 限制进度最小值
     *
     * @param limit null-表示不限制，即默认值0
     */
    void setLimitMin(Integer limit);

    /**
     * 限制进度最大值
     *
     * @param limit null-表示不限制，即和Max一致
     */
    void setLimitMax(Integer limit);
}
