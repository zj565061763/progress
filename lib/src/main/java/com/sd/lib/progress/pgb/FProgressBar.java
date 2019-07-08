package com.sd.lib.progress.pgb;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.sd.lib.progress.ProgressHolder;
import com.sd.lib.progress.ProgressView;
import com.sd.lib.progress.R;

public class FProgressBar extends View implements ProgressView
{
    public enum Orientation
    {
        Horizontal,
        Vertical
    }

    private Orientation mOrientation = Orientation.Horizontal;

    private ImageView mImageView;
    private int mProgressImage;
    private int mProgressColor;

    private final Rect mProgressRect = new Rect();
    private boolean mReverseProgress = false;

    private ProgressHolder mHolder;

    public FProgressBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mImageView = new ImageView(context);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);

        if (getBackground() == null)
            setBackgroundColor(getResources().getColor(R.color.lib_progress_pgb_background));

        setProgressColor(getResources().getColor(R.color.lib_progress_pgb_progress));

        if (attrs != null)
        {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LibProgressPgb);
            if (a.hasValue(R.styleable.LibProgressPgb_fProgressColor))
            {
                final int progressColor = a.getColor(R.styleable.LibProgressPgb_fProgressColor, 0);
                setProgressColor(progressColor);
            }

            if (a.hasValue(R.styleable.LibProgressPgb_fProgressImage))
            {
                final int progressImage = a.getResourceId(R.styleable.LibProgressPgb_fProgressImage, 0);
                setProgressImage(progressImage);
            }

            a.recycle();
        }
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
                    invalidate();
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
    public int getMax()
    {
        return getHolder().getMax();
    }

    @Override
    public Integer getLimitMin()
    {
        return getHolder().getLimitMin();
    }

    @Override
    public Integer getLimitMax()
    {
        return getHolder().getLimitMax();
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
            invalidate();
        return result;
    }

    @Override
    public void setMax(int max)
    {
        getHolder().setMax(max);
    }

    @Override
    public void setLimitMin(Integer limit)
    {
        getHolder().setLimitMin(limit);
    }

    @Override
    public void setLimitMax(Integer limit)
    {
        getHolder().setLimitMax(limit);
    }

    /**
     * 返回当前方向{@link Orientation}
     *
     * @return
     */
    public Orientation getOrientation()
    {
        return mOrientation;
    }

    /**
     * 返回是否反转进度增长方向
     *
     * @return
     */
    public boolean isReverseProgress()
    {
        return mReverseProgress;
    }

    /**
     * 设置方向{@link Orientation}
     *
     * @param orientation
     */
    public void setOrientation(Orientation orientation)
    {
        if (orientation == null)
            orientation = Orientation.Horizontal;

        if (mOrientation != orientation)
        {
            mOrientation = orientation;
            invalidate();
        }
    }

    /**
     * 设置进度图片
     *
     * @param resId
     */
    public void setProgressImage(int resId)
    {
        if (mProgressImage != resId)
        {
            mProgressImage = resId;

            mProgressColor = 0;
            mImageView.setBackgroundColor(0);
            mImageView.setImageResource(resId);

            invalidate();
        }
    }

    /**
     * 设置进度颜色
     *
     * @param color
     */
    public void setProgressColor(int color)
    {
        if (mProgressColor != color)
        {
            mProgressColor = color;

            mProgressImage = 0;
            mImageView.setBackgroundColor(color);
            mImageView.setImageBitmap(null);

            invalidate();
        }
    }

    /**
     * 设置是否反转进度增长方向，默认false
     *
     * @param reverse
     */
    public void setReverseProgress(boolean reverse)
    {
        mReverseProgress = reverse;
    }

    private Rect getProgressRect()
    {
        if (mOrientation == Orientation.Horizontal)
        {
            mProgressRect.top = 0;
            mProgressRect.bottom = getHeight();

            if (mReverseProgress)
            {
                mProgressRect.left = (int) (getWidth() * (1 - getProgressPercent()));
                mProgressRect.right = getWidth();
            } else
            {
                mProgressRect.left = 0;
                mProgressRect.right = (int) (getWidth() * getProgressPercent());
            }
        } else
        {
            mProgressRect.left = 0;
            mProgressRect.right = getWidth();

            if (mReverseProgress)
            {
                mProgressRect.top = 0;
                mProgressRect.bottom = (int) (getHeight() * getProgressPercent());
            } else
            {
                mProgressRect.top = (int) (getHeight() * (1 - getProgressPercent()));
                mProgressRect.bottom = getHeight();
            }
        }
        return mProgressRect;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mImageView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        mImageView.layout(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.clipRect(getProgressRect());
        mImageView.draw(canvas);
    }
}
