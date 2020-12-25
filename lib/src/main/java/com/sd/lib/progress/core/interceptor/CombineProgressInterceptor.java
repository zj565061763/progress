package com.sd.lib.progress.core.interceptor;

import com.sd.lib.progress.core.IProgressView;

public class CombineProgressInterceptor implements IProgressView.ProgressInterceptor
{
    private final IProgressView.ProgressInterceptor[] mInterceptors;

    public CombineProgressInterceptor(IProgressView.ProgressInterceptor... interceptors)
    {
        if (interceptors == null || interceptors.length <= 0)
            throw new IllegalArgumentException("interceptors is empty");

        mInterceptors = interceptors;
    }

    @Override
    public boolean interceptProgress(IProgressView progressView, int futureProgress)
    {
        for (IProgressView.ProgressInterceptor item : mInterceptors)
        {
            if (item.interceptProgress(progressView, futureProgress))
                return true;
        }
        return false;
    }
}
