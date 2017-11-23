package com.huizhou.huidongpolice.startApp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.huizhou.huidongpolice.LoginActivity;
import com.huizhou.huidongpolice.afterLogin.AfterLogin;
import com.huizhou.huidongpolice.utils.Log4J;
import com.huizhou.huidongpolice.utils.SpUtils;

import org.apache.commons.lang3.StringUtils;


/**
 * @desc 启动屏
 */
public class SplashActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0)
        {
            finish();
            return;
        }

        //设置log文件权限
        Log4J.verifyStoragePermissions(this);

        // 判断是否是第一次开启应用
        int isFirstOpen = SpUtils.getIsFirstOpen();
        // 如果是第一次启动，则先进入功能引导页
        if (isFirstOpen == 0)
        {
            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // 如果不是第一次启动app，则正常显示启动屏
        //setContentView(R.layout.activity_splash);

//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                enterLoginActivity();
//            }
//        }, 1000);


        enterLoginActivity();
    }

    private void enterLoginActivity()
    {
        SharedPreferences userSettings = getApplicationContext().getSharedPreferences("userInfo", 0);
        String name = userSettings.getString("loginUserName", "default");

        SharedPreferences password = getApplicationContext().getSharedPreferences("password", 0);
        String passwordLg = password.getString("passwordLg", "default");

        if (StringUtils.isNotBlank(name) && !"default".equals(name) && StringUtils.isNotBlank(passwordLg)
                && !"default".equals(passwordLg))
        {
            Intent intent = new Intent(this, AfterLogin.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


    }
}
