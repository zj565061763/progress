package com.sd.lib.progress.pgb;

import com.sd.lib.progress.core.IProgressView;

public interface IProgressBar extends IProgressView
{
    /**
     * 返回从哪个进度开始绘制起始进度
     *
     * @return 默认和最小进度一致
     */
    int getStartProgress();

    /**
     * 设置从某个进度开始绘制起始进度
     *
     * @param startProgress null-表示未设置，默认和最小进度一致
     * @return
     */
    boolean setStartProgress(Integer startProgress);

    /**
     * 返回起始进度的百分比
     *
     * @return
     */
    float getStartProgressPercent();
}
