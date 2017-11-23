package com.huizhou.huidongpolice.database.dao.impl;


import com.huizhou.huidongpolice.database.DbConnect;
import com.huizhou.huidongpolice.database.dao.UserInfoDAO;
import com.huizhou.huidongpolice.utils.Log4J;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Administrator on 2017/8/3.
 */

public class UserInfoDAOImpl implements UserInfoDAO
{
    /**
     * 根据用户名和密码校验用户真实性
     *
     * @param userName
     * @param pwd
     * @return
     */
    public boolean checkUserByUserAndPwd(String userName, String pwd)
    {
        Connection conn = DbConnect.getConnection();
        String strsql = "select * from tb_userinfo where username  = ? and pwd = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try
        {
            pstmt = conn.prepareStatement(strsql);
            pstmt.setString(1, userName);
            pstmt.setString(2, pwd);
            rs = pstmt.executeQuery();

            if (rs.next())
            {
                return true;
            }
        }
        catch (Exception e)
        {
            Log4J.getLogger(DbConnect.class.getClass()).error("查询用户存在性失败：" + e.getMessage());
        }
        finally
        {
            try
            {
                if (null != rs)
                {
                    rs.close();
                }
                if (null != pstmt)
                {
                    pstmt.close();
                }
            }
            catch (Exception e)
            {
                Log4J.getLogger(DbConnect.class.getClass()).error("查询用户存在性失败：" + e.getMessage());

            }

        }

        return false;
    }

    /**
     * 修改用户密码
     *
     * @param userName
     * @param pwd
     * @return
     */
    public boolean modifyPwdByUserName(String userName, String pwd)
    {
        Connection conn = DbConnect.getConnection();
        String strsql = "update tb_userinfo set pwd = ? where username  = ? ";
        PreparedStatement pstmt = null;
        try
        {
            pstmt = conn.prepareStatement(strsql);
            pstmt.setString(1, pwd);
            pstmt.setString(2, userName);

            if (pstmt.executeUpdate() != 0)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            Log4J.getLogger(DbConnect.class.getClass()).error("修改用户密码失败：" + e.getMessage());
        }
        finally
        {
            try
            {
                if (null != pstmt)
                {
                    pstmt.close();
                }
            }
            catch (Exception e)
            {
                Log4J.getLogger(DbConnect.class.getClass()).error("修改用户密码失败：" + e.getMessage());
            }
        }

        return false;

    }
}


