package com.sd.lib.progress.core.holder;

import com.sd.lib.progress.core.ProgressBar;

public abstract class ProgressBarHolder extends ProgressHolder implements ProgressBar
{
    private Integer mStartProgress;

    @Override
    public int getStartProgress()
    {
        return mStartProgress != null ? mStartProgress : getMin();
    }

    @Override
    public boolean setStartProgress(Integer startProgress)
    {
        if (startProgress != null)
        {
            if (startProgress < getMin())
                throw new IllegalArgumentException("startProgress < min value");

            if (startProgress > getMax())
                throw new IllegalArgumentException("startProgress > max value");
        }

        if (mStartProgress != startProgress)
        {
            mStartProgress = startProgress;
            return true;
        }
        return false;
    }

    @Override
    public float getStartProgressPercent()
    {
        final int total = getMax() - getMin();
        if (total <= 0)
            return 0.0f;

        final int delta = getStartProgress() - getMin();
        return (float) delta / total;
    }
}
