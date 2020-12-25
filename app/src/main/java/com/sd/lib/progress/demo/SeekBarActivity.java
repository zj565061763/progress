package com.sd.lib.progress.demo;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.sd.lib.progress.pgb.FProgressBar;
import com.sd.lib.progress.seek.FSeekLayout;
import com.sd.lib.progress.seek.ISeekLayout;

public class SeekBarActivity extends AppCompatActivity
{
    public static final String TAG = SeekBarActivity.class.getSimpleName();

    private FSeekLayout mSeekLayout;
    private FProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_bar);
        mSeekLayout = findViewById(R.id.seek_layout);
        mProgressBar = findViewById(R.id.progress_bar);

        mProgressBar.setOrientation(FProgressBar.Orientation.Vertical);
        // 设置从0开始绘制起始进度
        mProgressBar.setStartProgress(0);

        mSeekLayout.setOrientation(ISeekLayout.Orientation.Vertical);
        mSeekLayout.setMax(50);
        mSeekLayout.setMin(-50);
        mSeekLayout.setProgress(0);

        mSeekLayout.setOnProgressChangeCallback(new ISeekLayout.OnProgressChangeCallback()
        {
            @Override
            public void onProgressChanged(ISeekLayout seekLayout, int progress, boolean isTouch)
            {
                Log.i(TAG, "onProgressChanged: " + progress + " isTouch:" + isTouch);
            }
        });

        mSeekLayout.setOnTrackingTouchCallback(new ISeekLayout.OnTrackingTouchCallback()
        {
            @Override
            public void onStartTrackingTouch(ISeekLayout seekLayout)
            {
                Log.i(TAG, "onStartTrackingTouch: " + seekLayout.getProgress());
            }

            @Override
            public void onStopTrackingTouch(ISeekLayout seekLayout, boolean hasActionMove)
            {
                Log.i(TAG, "onStopTrackingTouch: " + seekLayout.getProgress() + " hasActionMove:" + hasActionMove);
            }
        });
    }
}
