package com.huizhou.huidongpolice.afterLogin;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.huizhou.huidongpolice.LoginActivity;
import com.huizhou.huidongpolice.R;

import org.apache.commons.lang3.StringUtils;

/**
 * 设置主页
 */
public class SettingFragment extends Fragment
{
    private Button buttonLogout;
    private Button buttonModifyPwd;
    private  Button onAbout;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        SharedPreferences userSettings = getActivity().getSharedPreferences("userInfo", 0);
        String name = userSettings.getString("loginUserName", "default");

        View view = inflater.inflate(R.layout.tab04, container, false);

        buttonLogout = (Button) view.findViewById(R.id.logoutTb4);
        buttonModifyPwd = (Button)view.findViewById(R.id.modifyPwd);
        onAbout = (Button) view.findViewById(R.id.about);

        //获取查询的参数
        if (null != getFragmentManager().findFragmentByTag("mainsetttingflag") &&
                null != getFragmentManager().findFragmentByTag("mainsetttingflag").getArguments())
        {
            String mainSetting = getFragmentManager().findFragmentByTag("mainsetttingflag").getArguments().getString("mainsetting");
            if (StringUtils.isNotBlank(mainSetting) && "1".equals(mainSetting))
            {
                getFragmentManager().findFragmentByTag("mainsetttingflag").setArguments(new Bundle());
            }
        }

        Button button = (Button) view.findViewById(R.id.nameShow);
        button.setText(name.toLowerCase());

        //引入布局
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        /**
         * 跳转登录界面
         *
         * @param view
         */
        //Button buttonLogout = (Button) getActivity().findViewById(R.id.logout);
        buttonLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //清楚缓存
                SharedPreferences userSettings = getActivity().getSharedPreferences("userInfo", 0);
                SharedPreferences.Editor editor = userSettings.edit();
                editor.remove("loginUserName");
                editor.commit();

                SharedPreferences pwSettings = getActivity().getSharedPreferences("password", 0);
                SharedPreferences.Editor editorPw = pwSettings.edit();
                editorPw.remove("passwordLg");
                editorPw.commit();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        /**
         * 跳转到修改密码界面
         *
         * @param view
         */
        //Button buttonModifyPwd = (Button) getActivity().findViewById(R.id.modifyPwd);
        buttonModifyPwd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), ActivityModifyPwd.class);
                startActivity(intent);
            }
        });

        /**
         * 弹出 关于 对话框
         *
         * @param view
         */
        //Button onAbout = (Button) getActivity().findViewById(R.id.about);
        onAbout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), ActivityAbout.class);
                startActivity(intent);
            }
        });

//        /**
//         * 返回主页
//         */
//        Button goBackMainPage = (Button) getActivity().findViewById(R.id.goBackMainPage);
//        if (null != goBackMainPage)
//        {
//            goBackMainPage.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    Intent intent = new Intent(getActivity(), ActivityMainPage.class);
//                    startActivity(intent);
//                    getActivity().finish();
//                }
//            });
//        }

    }

    /**
     * 这是兼容的 AlertDialog
     */
    private void showDialog(String message)
    {
  /*
  这里使用了 android.support.v7.app.AlertDialog.Builder
  可以直接在头部写 import android.support.v7.app.AlertDialog
  那么下面就可以写成 AlertDialog.Builder
  */
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle("关于");
        builder.setMessage(message);
        builder.setPositiveButton("确定", null);
        builder.show();
    }

}


