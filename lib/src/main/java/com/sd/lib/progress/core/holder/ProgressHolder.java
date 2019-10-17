package com.sd.lib.progress.core.holder;

import com.sd.lib.progress.core.ProgressView;

public abstract class ProgressHolder implements ProgressView
{
    private int mMin = 0;
    private int mMax = 100;

    private int mProgress = 0;
    private ProgressInterceptor mProgressInterceptor;

    @Override
    public int getMax()
    {
        return mMax;
    }

    public int getMin()
    {
        return mMin;
    }

    @Override
    public int getProgress()
    {
        return mProgress;
    }

    @Override
    public float getProgressPercent()
    {
        if (mMax < mMin)
            throw new RuntimeException("max < min max:" + mMax + " min:" + mMin);

        if (mMax == mMin)
            return 0.0f;

        final int total = mMax - mMin;
        final int delta = mProgress - mMin;
        return (float) delta / total;
    }

    @Override
    public int getPercentProgress(float percent)
    {
        if (percent <= 0)
            return mMin;

        final int progress = (int) (percent * (mMax - mMin)) + mMin;
        return progress;
    }

    @Override
    public boolean setMax(int max)
    {
        if (max < mMin)
            max = mMin;

        if (mMax != max)
        {
            mMax = max;

            if (setProgress(mProgress))
                onProgressFixIntoRange();
            return true;
        }
        return false;
    }

    @Override
    public boolean setMin(int min)
    {
        if (min > mMax)
            min = mMax;

        if (mMin != min)
        {
            mMin = min;

            if (setProgress(mProgress))
                onProgressFixIntoRange();
            return true;
        }
        return false;
    }

    @Override
    public boolean setProgress(int progress)
    {
        final int limitMin = mMin;
        final int limitMax = mMax;

        if (progress < limitMin)
            progress = limitMin;

        if (progress > limitMax)
            progress = limitMax;

        if (mProgress != progress)
        {
            if (mProgressInterceptor != null)
            {
                if (mProgressInterceptor.interceptProgress(getProgressView(), progress))
                    return false;
            }

            mProgress = progress;
            return true;
        }
        return false;
    }

    @Override
    public void setProgressInterceptor(ProgressInterceptor progressInterceptor)
    {
        mProgressInterceptor = progressInterceptor;
    }

    protected abstract ProgressView getProgressView();

    protected abstract void onProgressFixIntoRange();
}
