package com.sd.lib.progress.seek;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.sd.lib.progress.ProgressHolder;
import com.sd.lib.progress.ProgressView;
import com.sd.lib.progress.R;

import java.util.ArrayList;
import java.util.List;

public class FSeekLayout extends FrameLayout implements SeekLayout
{
    private View mThumbView;

    // 可以拖动的方向
    private Orientation mOrientation;
    private OrientationHandler mOrientationHandler;

    // 是否可以触摸改变进度
    private boolean mIsTouchable = true;

    // 是否同步进度给子view
    private boolean mSynchronizeProgress = true;
    // 保存所有子view
    private List<ProgressView> mListProgressView;

    /**
     * 在一次的手指接触到离开的过程中是否触发过{@link MotionEvent#ACTION_MOVE}
     */
    private boolean mHasActionMove = false;

    private OnProgressChangeCallback mOnProgressChangeCallback;
    private OnTrackingTouchCallback mOnTrackingTouchCallback;

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

    private List<ProgressView> getListProgressView()
    {
        if (mListProgressView == null)
            mListProgressView = new ArrayList<>(1);
        return mListProgressView;
    }

    @Override
    public int getProgress()
    {
        return getHolder().getProgress();
    }

    @Override
    public int getMax()
    {
        return getHolder().getMax();
    }

    @Override
    public int getMin()
    {
        return getHolder().getMin();
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
    public boolean setMax(int max)
    {
        final boolean result = getHolder().setMax(max);
        if (result)
            layoutThumb();

        synchronizeChildrenBound();
        return result;
    }

    @Override
    public boolean setMin(int min)
    {
        final boolean result = getHolder().setMin(min);
        if (result)
            layoutThumb();

        synchronizeChildrenBound();
        return result;
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
    public void setOnTrackingTouchCallback(OnTrackingTouchCallback callback)
    {
        mOnTrackingTouchCallback = callback;
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

    @Override
    public void setSynchronizeProgress(boolean synchronize)
    {
        if (mSynchronizeProgress != synchronize)
        {
            mSynchronizeProgress = synchronize;
            synchronizeChildrenProgress();
        }
    }

    private void notifyProgressChanged(boolean isTouch)
    {
        synchronizeChildrenProgress();

        layoutThumb();
        if (mOnProgressChangeCallback != null)
            mOnProgressChangeCallback.onProgressChanged(this, getHolder().getProgress(), isTouch);
    }

    //---------- synchronize bound ----------

    private void synchronizeChildrenProgress()
    {
        if (mListProgressView != null && mSynchronizeProgress)
        {
            for (ProgressView item : mListProgressView)
            {
                synchronizeChildProgress(item);
            }
        }
    }

    private void synchronizeChildProgress(ProgressView view)
    {
        if (mSynchronizeProgress)
            view.setProgress(getHolder().getProgress());
    }

    //---------- synchronize bound ----------

    private void synchronizeChildrenBound()
    {
        if (mListProgressView != null)
        {
            for (ProgressView item : mListProgressView)
            {
                synchronizeChildBound(item);
            }
        }
    }

    private void synchronizeChildBound(ProgressView view)
    {
        view.setMax(getHolder().getMax());
        view.setMin(getHolder().getMin());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        layoutThumb();
    }

    private void layoutThumb()
    {
        if (mOrientationHandler.check())
            mOrientationHandler.layoutThumb();
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

        if (child instanceof ProgressView)
        {
            final ProgressView view = (ProgressView) child;

            synchronizeChildBound(view);
            synchronizeChildProgress(view);
            getListProgressView().add(view);
        }
    }

    @Override
    public void onViewRemoved(View child)
    {
        super.onViewRemoved(child);
        if (mThumbView == child)
            mThumbView = null;

        if (mListProgressView != null && child instanceof ProgressView)
        {
            mListProgressView.remove(child);
            if (mListProgressView.isEmpty())
                mListProgressView = null;
        }
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

        public abstract void layoutThumb();

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

            if (mOrientation == Orientation.Vertical)
                percent = 1.0f - percent;

            final int progress = (int) (percent * getHolder().getMax());
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
                final int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN)
                {
                    mHasActionMove = false;
                    if (mOnTrackingTouchCallback != null)
                        mOnTrackingTouchCallback.onStartTrackingTouch(FSeekLayout.this);
                } else if (action == MotionEvent.ACTION_MOVE)
                {
                    mHasActionMove = true;
                }

                final int value = getTouchEventValue(event) - getThumbSize() / 2;
                final int progress = getBoundProgress(value);
                if (getHolder().setProgress(progress))
                    notifyProgressChanged(true);

                if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL)
                {
                    final boolean hasActionMove = mHasActionMove;
                    mHasActionMove = false;

                    if (mOnTrackingTouchCallback != null)
                        mOnTrackingTouchCallback.onStopTrackingTouch(FSeekLayout.this, hasActionMove);
                }
            }
        }

        protected abstract int getTouchEventValue(MotionEvent event);
    }

    private class HorizontalHandler extends OrientationHandler
    {
        @Override
        public void layoutThumb()
        {
            final float percent = getProgressPercent();
            final int left = (int) (getStartBound() + getAvailableSize() * percent);
            mThumbView.layout(left, mThumbView.getTop(), left + mThumbView.getMeasuredWidth(), mThumbView.getBottom());
        }

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
        public void layoutThumb()
        {
            final float percent = 1.0f - getProgressPercent();
            final int top = (int) (getStartBound() + getAvailableSize() * percent);
            mThumbView.layout(mThumbView.getLeft(), top, mThumbView.getRight(), top + mThumbView.getHeight());
        }

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
        return mIsTouchable;
    }
}
