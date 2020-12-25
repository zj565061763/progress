package com.sd.lib.progress.core.interceptor;

import com.sd.lib.progress.core.IProgressView;

/**
 * 最大进度拦截器
 */
public class MaxProgressInterceptor implements IProgressView.ProgressInterceptor
{
    private final int mMaxProgress;

    public MaxProgressInterceptor(int maxProgress)
    {
        mMaxProgress = maxProgress;
    }

    @Override
    public boolean interceptProgress(IProgressView progressView, int futureProgress)
    {
        if (futureProgress > mMaxProgress)
            return true;
        return false;
    }
}
