package com.sd.lib.progress.demo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.sd.lib.progress.core.interceptor.MinProgressInterceptor;
import com.sd.lib.progress.seek.FSeekLayout;
import com.sd.lib.progress.seek.ISeekLayout;

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

        mSeekLayout.setOrientation(ISeekLayout.Orientation.Horizontal);

        mSeekLayout.setProgress(50);
        mSeekLayout.setMax(100);

        // 设置进度拦截器，最小进度为20
        mSeekLayout.setProgressInterceptor(new MinProgressInterceptor(20));

        mSeekLayout.setOnProgressChangeCallback(new ISeekLayout.OnProgressChangeCallback()
        {
            @Override
            public void onProgressChanged(ISeekLayout seekLayout, int progress, boolean isTouch)
            {
                Log.i(TAG, "onProgressChanged: " + progress + " isTouch:" + isTouch);
            }
        });
    }
}
