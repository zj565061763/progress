package com.sd.lib.progress.seek;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.sd.lib.progress.ProgressHolder;
import com.sd.lib.progress.R;

public class FSeekLayout extends FrameLayout implements SeekLayout
{
    private View mThumbView;

    private Orientation mOrientation;
    private OrientationHandler mOrientationHandler;

    private boolean mIsTouchable = true;
    private OnProgressChangeCallback mOnProgressChangeCallback;

    private ProgressHolder mHolder;

    public FSeekLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setOrientation(Orientation.Horizontal);
    }

    private ProgressHolder getHolder()
    {
        if (mHolder == null)
        {
            mHolder = new ProgressHolder()
            {
                @Override
                protected void onProgressFixIntoRange()
                {
                    notifyProgressChanged(false);
                }
            };
        }
        return mHolder;
    }

    @Override
    public int getProgress()
    {
        return getHolder().getProgress();
    }

    @Override
    public int getMinProgress()
    {
        return getHolder().getMinProgress();
    }

    @Override
    public int getMaxProgress()
    {
        return getHolder().getMaxProgress();
    }

    @Override
    public float getProgressPercent()
    {
        return getHolder().getProgressPercent();
    }

    @Override
    public boolean setProgress(int progress)
    {
        final boolean result = getHolder().setProgress(progress);
        if (result)
            notifyProgressChanged(false);
        return result;
    }

    @Override
    public void setMinProgress(int progress)
    {
        getHolder().setMinProgress(progress);
    }

    @Override
    public void setMaxProgress(int progress)
    {
        getHolder().setMaxProgress(progress);
    }

    @Override
    public Orientation getOrientation()
    {
        return mOrientation;
    }

    @Override
    public void setOnProgressChangeCallback(OnProgressChangeCallback callback)
    {
        mOnProgressChangeCallback = callback;
    }

    @Override
    public void setOrientation(Orientation orientation)
    {
        if (orientation == null)
            orientation = Orientation.Horizontal;

        if (mOrientation != orientation)
        {
            mOrientation = orientation;

            if (mOrientation == Orientation.Horizontal)
                mOrientationHandler = new HorizontalHandler();
            else
                mOrientationHandler = new VerticalHandler();

            requestLayout();
        }
    }

    @Override
    public void setTouchable(boolean touchable)
    {
        mIsTouchable = touchable;
    }

    private void notifyProgressChanged(boolean isTouch)
    {
        layoutThumb();
        if (mOnProgressChangeCallback != null)
            mOnProgressChangeCallback.onProgressChanged(this, getHolder().getProgress(), isTouch);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        layoutThumb();
    }

    private void layoutThumb()
    {
        if (mOrientation == null)
            throw new RuntimeException("mOrientation is null");

        if (mOrientationHandler.check())
        {
            if (mOrientation == Orientation.Horizontal)
            {
                final int l = mOrientationHandler.getLayoutStart();
                mThumbView.layout(l, mThumbView.getTop(), l + mThumbView.getMeasuredWidth(), mThumbView.getBottom());
            } else
            {
                final int t = mOrientationHandler.getLayoutStart();
                mThumbView.layout(mThumbView.getLeft(), t, mThumbView.getRight(), t + mThumbView.getMeasuredHeight());
            }
        }
    }

    @Override
    public void onViewAdded(View child)
    {
        super.onViewAdded(child);
        if (child.getId() == R.id.lib_progress_seek_thumb)
            mThumbView = child;

        final LayoutParams params = (LayoutParams) child.getLayoutParams();
        if (params.gravity == LayoutParams.UNSPECIFIED_GRAVITY)
            params.gravity = Gravity.CENTER;
    }

    @Override
    public void onViewRemoved(View child)
    {
        super.onViewRemoved(child);
        if (mThumbView == child)
            mThumbView = null;
    }

    private LayoutParams getThumbViewParams()
    {
        return mThumbView == null ? null : (LayoutParams) mThumbView.getLayoutParams();
    }

    private abstract class OrientationHandler
    {
        public boolean check()
        {
            if (getWidth() == 0 || getHeight() == 0)
                return false;

            if (mThumbView == null)
                return false;

            final int width = mThumbView.getWidth();
            final int height = mThumbView.getHeight();
            if (width == 0 || height == 0)
                return false;

            final int availableSize = getAvailableSize();
            if (availableSize <= 0)
                return false;

            return true;
        }

        public int getLayoutStart()
        {
            return (int) (getStartBound() + getAvailableSize() * getProgressPercent());
        }

        // 可用的大小
        public int getAvailableSize()
        {
            return Math.abs(getEndBound() - getStartBound());
        }

        public int getStartBound()
        {
            return getPaddingStart() + getMarginStart();
        }

        public int getEndBound()
        {
            return getTotalSize() - getThumbSize() - getPaddingEnd() - getMarginEnd();
        }

        public int getBoundProgress(int bound)
        {
            float percent = 0.0f;

            final int availableSize = getAvailableSize();
            if (availableSize > 0)
            {
                final int start = getStartBound();
                final int end = getEndBound();

                if (bound <= start)
                {
                    percent = 0.0f;
                } else if (bound >= end)
                {
                    percent = 1.0f;
                } else
                {
                    percent = Math.abs(bound - start) / (float) availableSize;
                }
            }

            final int progress = (int) (percent * getHolder().getMaxProgress());
            return progress;
        }

        // 布局总的大小
        public abstract int getTotalSize();

        // 拖动手柄的大小
        public abstract int getThumbSize();

        public abstract int getMarginStart();

        public abstract int getMarginEnd();

        public abstract int getPaddingStart();

        public abstract int getPaddingEnd();

        public final void onTouchEvent(MotionEvent event)
        {
            if (mIsTouchable)
            {
                final int value = getTouchEventValue(event) - getThumbSize() / 2;
                final int progress = getBoundProgress(value);
                if (getHolder().setProgress(progress))
                    notifyProgressChanged(true);
            }
        }

        protected abstract int getTouchEventValue(MotionEvent event);
    }

    private class HorizontalHandler extends OrientationHandler
    {
        @Override
        public int getTotalSize()
        {
            return FSeekLayout.this.getWidth();
        }

        @Override
        public int getThumbSize()
        {
            return mThumbView.getWidth();
        }

        @Override
        public int getMarginStart()
        {
            return getThumbViewParams() == null ? 0 : getThumbViewParams().leftMargin;
        }

        @Override
        public int getMarginEnd()
        {
            return getThumbViewParams() == null ? 0 : getThumbViewParams().rightMargin;
        }

        @Override
        public int getPaddingStart()
        {
            return FSeekLayout.this.getPaddingLeft();
        }

        @Override
        public int getPaddingEnd()
        {
            return FSeekLayout.this.getPaddingRight();
        }

        @Override
        protected int getTouchEventValue(MotionEvent event)
        {
            return (int) event.getX();
        }
    }

    private class VerticalHandler extends OrientationHandler
    {
        @Override
        public int getTotalSize()
        {
            return FSeekLayout.this.getHeight();
        }

        @Override
        public int getThumbSize()
        {
            return mThumbView.getHeight();
        }

        @Override
        public int getMarginStart()
        {
            return getThumbViewParams() == null ? 0 : getThumbViewParams().topMargin;
        }

        @Override
        public int getMarginEnd()
        {
            return getThumbViewParams() == null ? 0 : getThumbViewParams().bottomMargin;
        }

        @Override
        public int getPaddingStart()
        {
            return FSeekLayout.this.getPaddingTop();
        }

        @Override
        public int getPaddingEnd()
        {
            return FSeekLayout.this.getPaddingBottom();
        }

        @Override
        protected int getTouchEventValue(MotionEvent event)
        {
            return (int) event.getY();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (mOrientationHandler.check())
            mOrientationHandler.onTouchEvent(event);
        return true;
    }
}
