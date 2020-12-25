package com.sd.lib.progress.core.interceptor;

import com.sd.lib.progress.core.IProgressView;

/**
 * 最小进度拦截器
 */
public class MinProgressInterceptor implements IProgressView.ProgressInterceptor
{
    private final int mMinProgress;

    public MinProgressInterceptor(int minProgress)
    {
        mMinProgress = minProgress;
    }

    @Override
    public boolean interceptProgress(IProgressView progressView, int futureProgress)
    {
        if (futureProgress < mMinProgress)
            return true;
        return false;
    }
}
