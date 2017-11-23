package com.huizhou.huidongpolice.afterLogin;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.huizhou.huidongpolice.R;

/**
 * Created by Administrator on 2017/8/17.
 */

public class ActivityMainPage extends FragmentActivity implements View.OnClickListener
{
    //底部的4个导航控件
    private LinearLayout mTabWeixin;

    private LinearLayout mTabSetting;

    //底部4个导航控件中的图片按钮
    private ImageButton mImgWeixin;

    private ImageButton mImgSetting;

    //初始化4个Fragment
    private Fragment tab01;
    private Fragment tab04;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_afterlogin);
        initView();//初始化所有的view
        initEvents();
        setSelect(0);//默认显示微信聊天界面
    }

    private void initEvents()
    {
        mTabWeixin.setOnClickListener(this);

        mTabSetting.setOnClickListener(this);

    }

    private void initView()
    {
        mTabWeixin = (LinearLayout) findViewById(R.id.id_tab_weixin_main);

        mTabSetting = (LinearLayout) findViewById(R.id.id_tab_setting_main);

        mImgWeixin = (ImageButton) findViewById(R.id.id_tab_weixin_img_main);

        mImgSetting = (ImageButton) findViewById(R.id.id_tab_setting_img_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        resetImg();
        switch (v.getId())
        {
            case R.id.id_tab_weixin_main://当点击微信按钮时，切换图片为亮色，切换fragment为微信聊天界面
                setSelect(0);
                break;

            case R.id.id_tab_setting_main:
                setSelect(3);
                break;

            default:
                break;
        }

    }

    /*
     * 将图片设置为亮色的；切换显示内容的fragment
     * */
    public void setSelect(int i)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();//创建一个事务
        hideFragment(transaction);//我们先把所有的Fragment隐藏了，然后下面再开始处理具体要显示的Fragment
        switch (i)
        {
            case 0:


                tab01 = new MainPageFragment();
                transaction.add(R.id.id_content_main, tab01);//将微信聊天界面的Fragment添加到Activity中
                mImgWeixin.setImageResource(R.mipmap.tab_weixin_pressed);
                break;
            case 3:
                if (tab04 == null)
                {
                    tab04 = new SettingFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("mainsetting", "1");
                    tab04.setArguments(bundle);
                    transaction.add(R.id.id_content_main, tab04,"mainsetttingflag");
                }
                else
                {
                    transaction.show(tab04);
                }
                mImgSetting.setImageResource(R.mipmap.tab_settings_pressed);
                break;

            default:
                break;
        }
        transaction.commit();//提交事务
    }

    /*
     * 隐藏所有的Fragment
     * */
    private void hideFragment(FragmentTransaction transaction)
    {
        if (tab01 != null)
        {
            transaction.hide(tab01);
        }

        if (tab04 != null)
        {
            transaction.hide(tab04);
        }

    }

    private void resetImg()
    {
        mImgWeixin.setImageResource(R.mipmap.tab_weixin_normal);
        mImgSetting.setImageResource(R.mipmap.tab_settings_normal);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == RESULT_OK){
//            String startTime = data.getStringExtra("startTime");
//            String endTime = data.getStringExtra("endTime");
//            FragmentManager fm = getSupportFragmentManager();
//            FragmentTransaction transaction = fm.beginTransaction();//创建一个事务
//            transaction.remove(tab02);
//            tab02 = new AllDataNewsListFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("startTime", startTime);
//            bundle.putString("endTime", endTime);
//            bundle.putString("query","1");
//            tab02.setArguments(bundle);
//            transaction.replace(R.id.id_content, tab02, "queryParams");
//            mImgFrd.setImageResource(R.drawable.tab_find_frd_pressed);
//            transaction.commit();//提交事务
//        }
//    }
}
