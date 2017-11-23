package com.huizhou.huidongpolice.afterLogin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huizhou.huidongpolice.R;

/**
 * Created by Administrator on 2017/8/17.
 */

public class MainSettingFragment extends Fragment
{
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {

        View view = inflater.inflate(R.layout.activity_main_business_setting, container, false);

        //引入布局
        return view;
    }
}
