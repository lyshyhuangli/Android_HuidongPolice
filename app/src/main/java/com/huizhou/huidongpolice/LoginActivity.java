package com.huizhou.huidongpolice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huizhou.huidongpolice.Request.CheckUserByUserAndPwdReq;
import com.huizhou.huidongpolice.afterLogin.AfterLogin;
import com.huizhou.huidongpolice.response.CheckUserByUserAndPwdResp;
import com.huizhou.huidongpolice.utils.CommonUtils;
import com.huizhou.huidongpolice.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;

/**
 * 登录页面
 */
public class LoginActivity extends AppCompatActivity
{
    private String userName;
    private String password;
    private ProgressBar progressBar;
    private MyTask mTask;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar = (ProgressBar) findViewById(R.id.progressLogin);
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

        mTask = new MyTask();
        mTask.execute();
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

    private void hideKeyboard()
    {
        View view = getCurrentFocus();
        if (view != null)
        {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    private class MyTask extends AsyncTask<String, Integer, Boolean>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected Boolean doInBackground(String... params)
        {
            String classInfo = CommonUtils.getFileLineMethod(new Exception());

            try
            {
                CheckUserByUserAndPwdReq
                 req = new CheckUserByUserAndPwdReq();
                req.setPwd(password);
                req.setUserName(userName);
                req.setOperatorId(userName);
                String result = HttpClientClass.httpPost(req, "checkUserByUserAndPwd");

                if (StringUtils.isBlank(result))
                {
                    return false;
                }

                Gson gson = new Gson();
                CheckUserByUserAndPwdResp info = gson.fromJson(result, CheckUserByUserAndPwdResp.class);
                if (null != info)
                {
                    if (0 == info.getResultCode() && info.getIsExist())
                    {
                        return true;
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                CommonUtils.sendLogsToServer(classInfo, "登录失败：" + e.getMessage(), userName);
            }

            return false;
        }

        //onProgressUpdate方法用于更新进度信息
        @Override
        protected void onProgressUpdate(Integer... progresses)
        {

        }

        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(Boolean result)
        {
            if (!result)
            {
                progressBar.setVisibility(View.GONE);
                Toast tos = Toast.makeText(getApplicationContext(), "警员编号或密码错误", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
                return;
            }
            else
            {
                progressBar.setVisibility(View.GONE);
                //保存登录用户信息
                SharedPreferences userSettings = getSharedPreferences("userInfo", 0);
                SharedPreferences.Editor editor = userSettings.edit();
                editor.putString("loginUserName", userName);
                editor.commit();

                SharedPreferences pwSettings = getSharedPreferences("password", 0);
                SharedPreferences.Editor editorPw = pwSettings.edit();
                editorPw.putString("passwordLg", password);
                editorPw.commit();

                //登录后台展示
                enterAfterLoginActivity();
            }
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }
}
