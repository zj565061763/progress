package com.sd.lib.progress.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sd.lib.progress.pgb.FProgressBar;
import com.sd.lib.progress.seek.FSeekLayout;
import com.sd.lib.progress.seek.SeekLayout;

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
        mProgressBar.setStartProgress(0);

        mSeekLayout.setOrientation(SeekLayout.Orientation.Vertical);
        mSeekLayout.setMax(50);
        mSeekLayout.setMin(-50);
        mSeekLayout.setProgress(0);

        mSeekLayout.setOnProgressChangeCallback(new SeekLayout.OnProgressChangeCallback()
        {
            @Override
            public void onProgressChanged(SeekLayout seekLayout, int progress, boolean isTouch)
            {
                Log.i(TAG, "onProgressChanged: " + progress + " isTouch:" + isTouch);
            }
        });

        mSeekLayout.setOnTrackingTouchCallback(new SeekLayout.OnTrackingTouchCallback()
        {
            @Override
            public void onStartTrackingTouch(SeekLayout seekLayout)
            {
                Log.i(TAG, "onStartTrackingTouch: " + seekLayout.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekLayout seekLayout, boolean hasActionMove)
            {
                Log.i(TAG, "onStopTrackingTouch: " + seekLayout.getProgress() + " hasActionMove:" + hasActionMove);
            }
        });
    }
}
