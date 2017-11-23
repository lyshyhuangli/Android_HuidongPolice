package com.huizhou.huidongpolice.afterLogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.huizhou.huidongpolice.LoginActivity;
import com.huizhou.huidongpolice.R;
import com.huizhou.huidongpolice.Request.CheckUserByUserAndPwdReq;
import com.huizhou.huidongpolice.utils.CommonCache;
import com.huizhou.huidongpolice.utils.CommonUtils;
import com.huizhou.huidongpolice.utils.Flags;
import com.huizhou.huidongpolice.utils.Log4J;
import com.huizhou.huidongpolice.utils.ProcessDataBaseThread;
import com.huizhou.huidongpolice.utils.StaticNumber;

import org.apache.commons.lang3.StringUtils;

/**
 * 修改密码界面
 */
public class ActivityModifyPwd extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
    }

    public void modifyPwdBack(View view)
    {
        super.onBackPressed();
    }

    public void modifyPwd(View view)
    {
        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        String userName = userSettings.getString("loginUserName", "default");

        TextInputLayout pwd1Wrapper = (TextInputLayout) findViewById(R.id.pwd1Wrapper);
        TextInputLayout pwd2Wrapper = (TextInputLayout) findViewById(R.id.pwd2Wrapper);

        String pwd1 = pwd1Wrapper.getEditText().getText().toString();
        String pwd2 = pwd2Wrapper.getEditText().getText().toString();

        if (!StringUtils.isBlank(pwd1) && !StringUtils.isEmpty(pwd1))
        {
            if (!pwd1.equals(pwd2))
            {
                Toast tos = Toast.makeText(this, "2次输入的密码不匹配!", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.TOP, 0, 200);
                tos.show();
                return;
            }
        }
        else
        {
            Toast tos = Toast.makeText(this, "密码不能为空!", Toast.LENGTH_LONG);
            tos.setGravity(Gravity.TOP, 0, 200);
            tos.show();
            return;
        }

        if (!modify(userName, pwd2))
        {
            Toast tos = Toast.makeText(this, "密码修改失败，请重试!", Toast.LENGTH_LONG);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }
        else
        {
            Toast tos = Toast.makeText(this, "密码修改成功!", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            super.onBackPressed();
        }
    }

    /**
     * 用户修改密码
     *
     * @param userName
     * @param password
     * @return
     */
    public boolean modify(String userName, String password)
    {
        String classInfo = CommonUtils.getFileLineMethod(new Exception());

        ProcessDataBaseThread pd = new ProcessDataBaseThread();
        pd.setFlag(Flags.ONE_ZERO_ZERO_FIVE);

        CheckUserByUserAndPwdReq req = new CheckUserByUserAndPwdReq();
        req.setPwd(password);
        req.setUserName(userName);
        req.setOperatorId(userName);
        pd.setObject(req);

        Thread t = new Thread(pd);
        t.start();
        try
        {
            for (int i = 0; i <= StaticNumber.TWO_ZERO; i++)
            {
                Thread.sleep(StaticNumber.FIVE_HUNDRED);

                Object o = CommonCache.dataCache.get(Flags.ONE_ZERO_ZERO_FIVE);
                if (null != o)
                {
                    CommonCache.dataCache.remove(Flags.ONE_ZERO_ZERO_FIVE);
                    return (boolean) o;
                }
                else
                {
                    if (i == StaticNumber.TEN)
                    {
                        Thread t2 = new Thread(pd);
                        t2.start();

                        Thread.sleep(StaticNumber.FIVE_HUNDRED);
                        o = CommonCache.dataCache.get(Flags.ONE_ZERO_ZERO_FIVE);
                        if (null != o)
                        {
                            CommonCache.dataCache.remove(Flags.ONE_ZERO_ZERO_FIVE);
                            return (boolean) o;
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log4J.getLogger(LoginActivity.class.getClass()).error("修改密码失败：" + e.getMessage());
            CommonUtils.sendLogsToServer(classInfo, "修改密码失败：" + e.getMessage(), userName);
        }
        return false;
    }
}
