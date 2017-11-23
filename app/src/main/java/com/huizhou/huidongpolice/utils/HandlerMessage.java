package com.huizhou.huidongpolice.utils;

/**
 * Created by Administrator on 2017/8/7.
 */

public class HandlerMessage
{
    public Object handleMessage(android.os.Message msg)
    {
        switch (msg.what)
        {
            //登录校验
            case 1001:
                boolean str = msg.getData().getBoolean("result");
                return str;
            default:
                return null;
        }
    }

}
