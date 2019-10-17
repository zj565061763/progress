package com.sd.lib.progress.core.interceptor;

import com.sd.lib.progress.core.ProgressView;

/**
 * 最小进度拦截器
 */
public class MinProgressInterceptor implements ProgressView.ProgressInterceptor
{
    private final int mMinProgress;

    public MinProgressInterceptor(int minProgress)
    {
        mMinProgress = minProgress;
    }

    @Override
    public boolean interceptProgress(ProgressView progressView, int futureProgress)
    {
        if (futureProgress < mMinProgress)
            return true;
        return false;
    }
}
