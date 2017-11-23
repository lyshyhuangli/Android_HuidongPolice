package com.huizhou.huidongpolice.afterLogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.huizhou.huidongpolice.R;

public class ActivityAbout extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void aboutdBack(View view)
    {
        super.onBackPressed();
    }
}
