package com.sd.lib.progress.core.interceptor;

import com.sd.lib.progress.core.ProgressView;

public class CombineProgressInterceptor implements ProgressView.ProgressInterceptor
{
    private final ProgressView.ProgressInterceptor[] mInterceptors;

    public CombineProgressInterceptor(ProgressView.ProgressInterceptor... interceptors)
    {
        if (interceptors == null || interceptors.length <= 0)
            throw new IllegalArgumentException("interceptors is empty");

        mInterceptors = interceptors;
    }

    @Override
    public boolean interceptProgress(ProgressView progressView, int futureProgress)
    {
        for (ProgressView.ProgressInterceptor item : mInterceptors)
        {
            if (item.interceptProgress(progressView, futureProgress))
                return true;
        }
        return false;
    }
}
