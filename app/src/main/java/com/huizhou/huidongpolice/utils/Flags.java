package com.huizhou.huidongpolice.utils;

/**
 * Created by Administrator on 2017/8/10.
 */

public interface Flags
{
    //数据库连接校验
    public final static int DB_CONNECT = 1000;

    //登录校验
    public final static int ONE_ZERO_ZERO_ONE = 1001;

    //查询最新5条警讯
    public final static int ONE_ZERO_ZERO_TWO = 1002;

    //根据Id查询信息
    public final static int ONE_ZERO_ZERO_THREE = 1003;

    //滑动获取数据
    public final static int ONE_ZERO_ZERO_FOUR = 1004;

    //修改密码
    public final static int ONE_ZERO_ZERO_FIVE = 1005;

    //查询统计文件
    public final static int SEARCH_STATIC_FILES = 1006;

    //查询所有 确认警情性质
    public final static int SEARCH_ALL_CONFORM_JINGQING_XINGZHI = 1007;

    //查询所有 派出所
    public final static int SEARCH_ALL_ALARM_COMPANY = 1008;

    //统计一周案情
    public final static int STATIC_WEEK_ALARM_COUNT = 1009;

    //上传日志到服务器
    public final static int SEND_LOGS_TO_SERVER = 1010;

    //读取文件
    public final  static int READ_FILE = 1011;
}
