package com.huizhou.huidongpolice.database.dao;

/**
 * Created by Administrator on 2017/8/3.
 */

public interface UserInfoDAO {


    /**
     * 根据用户名和密码校验用户真实性
     * @param userName
     * @param pwd
     * @return
     */
    public abstract boolean checkUserByUserAndPwd(String userName,String pwd);

    /**
     * 修改用户密码
     * @param userName
     * @param pwd
     * @return
     */
    public abstract boolean modifyPwdByUserName(String userName,String pwd);

}
