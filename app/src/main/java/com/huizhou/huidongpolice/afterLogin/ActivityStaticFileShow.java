package com.huizhou.huidongpolice.afterLogin;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.BuildConfig;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.huizhou.huidongpolice.R;
import com.huizhou.huidongpolice.utils.FileDownThread;


import java.io.File;

public class ActivityStaticFileShow extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_file_show);

        Intent it = getIntent();
        String url = it.getStringExtra("url");
        String fileName = it.getStringExtra("fileName");


    }

    /**
     * 打开pdf文件
     *
     * @param path
     */
    public void openPdfFile(String path)
    {
        Intent intent = getPdfFileIntent(path);
        startActivity(intent);
    }

    public Intent getPdfFileIntent(String path)
    {

        Intent intent = new Intent(Intent.ACTION_VIEW);//Intent.ACTION_VIEW = "android.intent.action.VIEW"

        intent.addCategory(Intent.CATEGORY_DEFAULT);//Intent.CATEGORY_DEFAULT = "android.intent.category.DEFAULT"

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri = Uri.fromFile(new File(path));

        intent.setDataAndType(uri, "application/pdf");

        return intent;
    }





}
