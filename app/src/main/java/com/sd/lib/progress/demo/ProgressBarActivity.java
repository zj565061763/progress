package com.sd.lib.progress.demo;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sd.lib.progress.pgb.FProgressBar;

public class ProgressBarActivity extends AppCompatActivity
{
    private FProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);
        mProgressBar = findViewById(R.id.progress_bar);

        mProgressBar.setOrientation(FProgressBar.Orientation.Horizontal);
        mProgressBar.setProgressImage(R.drawable.timg);
        mProgressBar.setReverseProgress(false);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        startAnimator();
    }

    private ValueAnimator mAnimator;

    private ValueAnimator getAnimator()
    {
        if (mAnimator == null)
        {
            mAnimator = new ValueAnimator();
            mAnimator.setDuration(5 * 1000);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    mProgressBar.setProgress((Integer) animation.getAnimatedValue());
                }
            });
        }
        return mAnimator;
    }

    private void startAnimator()
    {
        cancelAnimator();
        getAnimator().setIntValues(mProgressBar.getLimitMin(), mProgressBar.getMax());
        getAnimator().start();
    }

    private void cancelAnimator()
    {
        if (mAnimator != null)
            mAnimator.cancel();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        cancelAnimator();
    }
}
