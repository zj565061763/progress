package com.sd.lib.progress.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sd.lib.progress.seek.FSeekLayout;
import com.sd.lib.progress.seek.SeekLayout;

public class SeekLayoutActivity extends AppCompatActivity
{
    public static final String TAG = SeekLayoutActivity.class.getSimpleName();

    private FSeekLayout mSeekLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_layout);
        mSeekLayout = findViewById(R.id.seek_layout);

        mSeekLayout.setOrientation(SeekLayout.Orientation.Horizontal);
        mSeekLayout.setMaxProgress(90);
        mSeekLayout.setMinProgress(10);
        mSeekLayout.setOnProgressChangeCallback(new SeekLayout.OnProgressChangeCallback()
        {
            @Override
            public void onProgressChanged(SeekLayout seekLayout, int progress, boolean isTouch)
            {
                Log.i(TAG, "onProgressChanged: " + progress + " isTouch:" + isTouch);
            }
        });
    }
}
