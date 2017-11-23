package com.huizhou.huidongpolice.afterLogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.huizhou.huidongpolice.R;

/**
 * Created by Administrator on 2017/8/17.
 */

public class MainPageFragment extends Fragment
{
    private View view;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {

        view = inflater.inflate(R.layout.activity_business_type_list, container, false);

        //引入布局
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        /**
         * 跳转到移动警情
         *
         * @param view
         */
        Button yidongjingwu = (Button) view.findViewById(R.id.yidongjingwu);
        if (null != yidongjingwu)
        {
            yidongjingwu.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //跳转到详细页面
                    Intent it = new Intent(getActivity(), AfterLogin.class);
                    startActivityForResult(it, 100);
                }
            });
        }

    }
}
