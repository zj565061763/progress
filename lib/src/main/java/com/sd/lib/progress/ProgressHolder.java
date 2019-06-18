package com.sd.lib.progress;

public abstract class ProgressHolder implements ProgressView
{
    private int mProgress = 0;
    // 暂时不对外开放修改
    private final int mMin = 0;
    private int mMax = 100;

    private Integer mLimitMin = null;
    private Integer mLimitMax = null;

    @Override
    public int getProgress()
    {
        return mProgress;
    }

    @Override
    public int getMax()
    {
        return mMax;
    }

    @Override
    public Integer getLimitMin()
    {
        return mLimitMin == null ? mMin : mLimitMin;
    }

    @Override
    public Integer getLimitMax()
    {
        return mLimitMax == null ? mMax : mLimitMax;
    }

    @Override
    public float getProgressPercent()
    {
        if (mMax < mMin)
            throw new RuntimeException("max < min max:" + mMax + " min:" + mMin);

        return mMax == mMin ? 0.0f : (float) mProgress / mMax;
    }

    @Override
    public boolean setProgress(int progress)
    {
        final int limitMin = getLimitMin();
        final int limitMax = getLimitMax();

        if (progress < limitMin)
            progress = limitMin;

        if (progress > limitMax)
            progress = limitMax;

        if (mProgress != progress)
        {
            mProgress = progress;
            return true;
        }
        return false;
    }

    @Override
    public void setMax(int max)
    {
        if (max < mMin)
            max = mMin;

        if (mMax != max)
        {
            mMax = max;
            checkLimitBound();

            if (setProgress(mProgress))
                onProgressFixIntoRange();
        }
    }

    @Override
    public void setLimitMin(Integer limit)
    {
        if (mLimitMin != limit)
        {
            mLimitMin = limit;
            checkLimitBound();

            if (setProgress(mProgress))
                onProgressFixIntoRange();
        }
    }

    @Override
    public void setLimitMax(Integer limit)
    {
        if (mLimitMax != limit)
        {
            mLimitMax = limit;
            checkLimitBound();

            if (setProgress(mProgress))
                onProgressFixIntoRange();
        }
    }

    private void checkLimitBound()
    {
        if (mLimitMin != null)
            checkBound(mLimitMin);

        if (mLimitMax != null)
            checkBound(mLimitMax);

        if (mLimitMin != null && mLimitMax != null)
        {
            if (mLimitMin > mLimitMax)
                throw new RuntimeException("limitMin > limitMax min:" + mLimitMin + " max:" + mLimitMax);
        }
    }

    private void checkBound(int value)
    {
        if (value < mMin || value > mMax)
            throw new RuntimeException("value out of range (" + mMin + "," + mMax + ")");
    }

    protected abstract void onProgressFixIntoRange();
}
