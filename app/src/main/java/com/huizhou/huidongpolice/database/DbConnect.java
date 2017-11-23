package com.huizhou.huidongpolice.database;

import android.widget.Toast;

import com.huizhou.huidongpolice.utils.Log4J;

import java.sql.Connection;
import java.sql.DriverManager;


/**
 * 数据库对接
 * Created by Administrator on 2017/8/3.
 */
public class DbConnect
{
    private static Connection conn;

    private static void setConn()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (Exception e)
        {
            Log4J.getLogger(DbConnect.class.getClass()).error("启动数据库驱动失败：" + e.getMessage());
        }

        String ip = DbConfig.DB_IP;
        String userName = DbConfig.DB_USERNAME;
        String password = DbConfig.DB_PASSWORD;
        String port = DbConfig.DB_PORT;
        String dbName = DbConfig.DB_NAME;

        try
        {
            //链接数据库语句
            String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?user="
                    + userName + "&password=" + password + "&useUnicode=true&characterEncoding=UTF-8";

            conn = (Connection) DriverManager.getConnection(url); //链接数据库

            if (null == conn)
            {
                conn = (Connection) DriverManager.getConnection(url); //链接数据库
            }
        }
        catch (Exception e)
        {
            Log4J.getLogger(DbConnect.class.getClass()).error("对接数据库失败：" + e.getMessage());
        }
    }

    public static Connection getConnection()
    {
        if (conn != null)
        {
            return conn;
        }
        else
        {
            setConn();
            return conn;
        }
    }
}

