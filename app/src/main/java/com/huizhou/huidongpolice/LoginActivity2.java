package com.huizhou.huidongpolice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.huizhou.huidongpolice.Request.CheckDbConnectReq;
import com.huizhou.huidongpolice.Request.CheckUserByUserAndPwdReq;
import com.huizhou.huidongpolice.afterLogin.ActivityMainPage;
import com.huizhou.huidongpolice.afterLogin.AfterLogin;
import com.huizhou.huidongpolice.utils.CommonCache;
import com.huizhou.huidongpolice.utils.CommonUtils;
import com.huizhou.huidongpolice.utils.Flags;
import com.huizhou.huidongpolice.utils.Log4J;
import com.huizhou.huidongpolice.utils.ProcessDataBaseThread;
import com.huizhou.huidongpolice.utils.StaticNumber;

import org.apache.commons.lang3.StringUtils;

/**
 * 登录页面
 */
public class LoginActivity2 extends AppCompatActivity
{
    private String userName;
    private String password;

    //private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //imageView = (ImageView) findViewById(R.id.gif);
        //Glide.with(this).load(R.drawable.loading).into(imageView);

//        if (!checkDbConnect())
//        {
//            showDialog("服务器连接失败，请检查网络或尝试登陆.");
//            return;
//        }
    }

    //检查数据库是否连接成功
    private boolean checkDbConnect()
    {
        String classInfo = CommonUtils.getFileLineMethod(new Exception());

        ProcessDataBaseThread pd = new ProcessDataBaseThread();
        pd.setFlag(Flags.DB_CONNECT);

        CheckDbConnectReq req = new CheckDbConnectReq();
        req.setOperatorId(userName);
        pd.setObject(req);

        Thread t = new Thread(pd);
        t.start();
        try
        {
            for (int i = 0; i <= StaticNumber.TEN; i++)
            {
                Thread.sleep(StaticNumber.ONE_HUNDRED);

                Object o = CommonCache.dataCache.get(Flags.DB_CONNECT);
                if (null != o)
                {
                    CommonCache.dataCache.remove(Flags.DB_CONNECT);
                    return (boolean) o;
                }
                else
                {
                    if (i == StaticNumber.FIVE)
                    {
                        pd.setFlag(Flags.DB_CONNECT);
                        Thread t2 = new Thread(pd);
                        t2.start();

                        Thread.sleep(StaticNumber.ONE_HUNDRED);
                        o = CommonCache.dataCache.get(Flags.DB_CONNECT);
                        if (null != o)
                        {
                            CommonCache.dataCache.remove(Flags.DB_CONNECT);
                            return (boolean) o;
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log4J.getLogger(LoginActivity.class.getClass()).error("数据库连接失败：" + e.getMessage());
            CommonUtils.sendLogsToServer(classInfo, "登录失败：" + e.getMessage(), userName);
        }

        return false;
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
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("错误");
        builder.setMessage(message);
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    public void onClick_Event(View view)
    {
        hideKeyboard();
        TextInputLayout usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        TextInputLayout passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);

        userName = usernameWrapper.getEditText().getText().toString();
        password = passwordWrapper.getEditText().getText().toString();
        if (!validateUserName(userName))
        {
            usernameWrapper.setError("请填写警员编号");
            return;
        }
        else
        {
            usernameWrapper.setErrorEnabled(false);
        }

        if (!validatePassword(password))
        {
            passwordWrapper.setError("请填写密码");
            return;
        }
        else
        {
            usernameWrapper.setErrorEnabled(false);
        }

        usernameWrapper.setErrorEnabled(false);
        passwordWrapper.setErrorEnabled(false);
        //校验用户存在性
        boolean result = doLogin(userName, password);
        if (!result)
        {
            Toast tos = Toast.makeText(this, "警员编号或密码错误", Toast.LENGTH_LONG);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }

        //保存登录用户信息
        SharedPreferences userSettings = getSharedPreferences("userInfo", 0);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putString("loginUserName", userName);
        editor.commit();

        //登录后台展示
        enterAfterLoginActivity();

    }

    private void enterAfterLoginActivity()
    {
        Intent intent = new Intent(this, AfterLogin.class);
        startActivityForResult(intent, 100);
        finish();
    }

    public boolean validatePassword(String password)
    {
        if (StringUtils.isBlank(password) || StringUtils.isEmpty(password))
        {
            return false;
        }
        return true;
    }

    public boolean validateUserName(String userName)
    {
        if (StringUtils.isBlank(userName) || StringUtils.isEmpty(userName))
        {
            return false;
        }
        return true;
    }

    /**
     * 校验登录用户的存在性
     *
     * @param userName
     * @param password
     * @return
     */
    public boolean doLogin(String userName, String password)
    {
        String classInfo = CommonUtils.getFileLineMethod(new Exception());

        ProcessDataBaseThread pd = new ProcessDataBaseThread();
        pd.setFlag(Flags.ONE_ZERO_ZERO_ONE);
        CheckUserByUserAndPwdReq req = new CheckUserByUserAndPwdReq();
        req.setPwd(password);
        req.setUserName(userName);
        req.setOperatorId(userName);
        pd.setObject(req);

        Thread t = new Thread(pd);
        t.start();

        try
        {
            for (int i = 0; i <= StaticNumber.TEN; i++)
            {
                Thread.sleep(StaticNumber.FIVE_HUNDRED);
                Object o = CommonCache.dataCache.get(Flags.ONE_ZERO_ZERO_ONE);
                if (null != o)
                {
                    CommonCache.dataCache.remove(Flags.ONE_ZERO_ZERO_ONE);
                    return (boolean) o;
                }
                else
                {
                    if (i == StaticNumber.FIVE)
                    {
                        Thread t2 = new Thread(pd);
                        t2.start();

                        Thread.sleep(StaticNumber.FIVE_HUNDRED);
                        o = CommonCache.dataCache.get(Flags.ONE_ZERO_ZERO_ONE);
                        if (null != o)
                        {
                            CommonCache.dataCache.remove(Flags.ONE_ZERO_ZERO_ONE);
                            return (boolean) o;
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log4J.getLogger(LoginActivity.class.getClass()).error("登录失败：" + e.getMessage());
            CommonUtils.sendLogsToServer(classInfo, "登录失败：" + e.getMessage(), userName);
        }

        return false;
    }


    private void hideKeyboard()
    {
        View view = getCurrentFocus();
        if (view != null)
        {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
