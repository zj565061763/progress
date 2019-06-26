package com.sd.lib.progress.pgb;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.sd.lib.progress.ProgressHolder;
import com.sd.lib.progress.ProgressView;

public class FProgressBar extends View implements ProgressView
{
    public enum Orientation
    {
        Horizontal,
        Vertical
    }

    private Orientation mOrientation = Orientation.Horizontal;

    private final Paint mPaint = new Paint();
    private final Rect mProgressBitmapRect = new Rect();
    private final RectF mProgressRectF = new RectF();

    private Bitmap mProgressBitmap;

    private int mProgressImage;
    private int mProgressColor;

    private boolean mReverseProgress = false;

    private ProgressHolder mHolder;

    public FProgressBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        if (getBackground() == null)
            setBackgroundColor(Color.parseColor("#999999"));

        final TypedArray array = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.colorAccent});
        final int colorAccent = array.getColor(0, Color.RED);
        setProgressColor(colorAccent);
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

            mProgressBitmap = null;
            mProgressColor = 0;

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

            mProgressBitmap = null;
            mProgressImage = 0;

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

    private Bitmap getProgressBitmap()
    {
        final int viewWidth = getWidth();
        final int viewHeight = getHeight();

        if (viewWidth == 0 || viewHeight == 0)
            return null;

        if (mProgressImage == 0 && mProgressColor == 0)
            return null;

        if (mProgressBitmap == null)
        {
            if (mProgressImage != 0)
            {
                mProgressBitmap = imageResIdToBitmap(getContext(), mProgressImage);
                if (mProgressBitmap == null)
                    mProgressBitmap = imageResIdToBitmap(getContext(), mProgressImage, viewWidth, viewHeight);
            } else if (mProgressColor != 0)
            {
                mProgressBitmap = colorToBitmap(mProgressColor, viewWidth, viewHeight);
            }
        }

        return mProgressBitmap;
    }

    private RectF getProgressRectF()
    {
        if (mOrientation == Orientation.Horizontal)
        {
            mProgressRectF.top = 0;
            mProgressRectF.bottom = getHeight();

            if (mReverseProgress)
            {
                mProgressRectF.left = getWidth() * (1 - getProgressPercent());
                mProgressRectF.right = getWidth();
            } else
            {
                mProgressRectF.left = 0;
                mProgressRectF.right = getWidth() * getProgressPercent();
            }
        } else
        {
            mProgressRectF.left = 0;
            mProgressRectF.right = getWidth();

            if (mReverseProgress)
            {
                mProgressRectF.top = 0;
                mProgressRectF.bottom = getHeight() * getProgressPercent();
            } else
            {
                mProgressRectF.top = getHeight() * (1 - getProgressPercent());
                mProgressRectF.bottom = getHeight();
            }
        }
        return mProgressRectF;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        final Bitmap bitmap = getProgressBitmap();
        if (bitmap != null)
        {
            final RectF progressRectF = getProgressRectF();

            mProgressBitmapRect.left = (int) progressRectF.left;
            mProgressBitmapRect.top = (int) progressRectF.top;
            mProgressBitmapRect.right = (int) progressRectF.right;
            mProgressBitmapRect.bottom = (int) progressRectF.bottom;

            scaleBitmapIfNeed();
            canvas.drawBitmap(bitmap, mProgressBitmapRect, progressRectF, mPaint);
        }
    }

    private void scaleBitmapIfNeed()
    {
        final int viewWidth = getWidth();
        final int viewHeight = getHeight();

        if (viewWidth == 0 || viewHeight == 0)
            return;

        if (mProgressBitmap.getWidth() != viewWidth
                || mProgressBitmap.getHeight() != viewHeight)
        {
            mProgressBitmap = Bitmap.createScaledBitmap(mProgressBitmap, viewWidth, viewHeight, true);
        }
    }

    private static Bitmap imageResIdToBitmap(Context context, int resId)
    {
        if (resId == 0)
            return null;

        final Resources resources = context.getResources();
        final TypedValue typedValue = new TypedValue();
        resources.openRawResource(resId, typedValue);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDensity = typedValue.density;

        return BitmapFactory.decodeResource(resources, resId, options);
    }

    private static Bitmap imageResIdToBitmap(Context context, int resId, int width, int height)
    {
        if (resId == 0)
            return null;

        final Drawable drawable = context.getResources().getDrawable(resId);
        return drawableToBitmap(drawable, width, height);
    }

    private static Bitmap colorToBitmap(int color, int width, int height)
    {
        if (color == 0)
            return null;

        final Drawable drawable = new ColorDrawable(color);
        return drawableToBitmap(drawable, width, height);
    }

    private static Bitmap drawableToBitmap(Drawable drawable, int width, int height)
    {
        final Bitmap bitmap = Bitmap.createBitmap(width, height,
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);

        final Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }
}
