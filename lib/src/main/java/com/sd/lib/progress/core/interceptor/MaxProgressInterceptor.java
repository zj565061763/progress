package com.sd.lib.progress.core.interceptor;

import com.sd.lib.progress.core.ProgressView;

/**
 * 最大进度拦截器
 */
public class MaxProgressInterceptor implements ProgressView.ProgressInterceptor
{
    private final int mMaxProgress;

    public MaxProgressInterceptor(int maxProgress)
    {
        mMaxProgress = maxProgress;
    }

    @Override
    public boolean interceptProgress(ProgressView progressView, int futureProgress)
    {
        if (futureProgress > mMaxProgress)
            return true;
        return false;
    }
}
