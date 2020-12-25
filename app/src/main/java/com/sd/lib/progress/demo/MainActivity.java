package com.sd.lib.progress.demo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_progress_bar:
                startActivity(new Intent(this, ProgressBarActivity.class));
                break;
            case R.id.btn_seek_layout:
                startActivity(new Intent(this, SeekLayoutActivity.class));
                break;
            case R.id.btn_seek_bar:
                startActivity(new Intent(this, SeekBarActivity.class));
                break;
        }
    }
}
