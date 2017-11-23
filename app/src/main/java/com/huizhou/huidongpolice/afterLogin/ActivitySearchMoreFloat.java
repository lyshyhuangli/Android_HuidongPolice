package com.huizhou.huidongpolice.afterLogin;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.huizhou.huidongpolice.R;
import com.huizhou.huidongpolice.afterLogin.checkbox.ActivityAlarmCompanycheckbox;
import com.huizhou.huidongpolice.afterLogin.checkbox.ActivityComformXingzhiCheckbox;
import com.squareup.timessquare.CalendarPickerView;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 弹出更多搜索条件
 */
public class ActivitySearchMoreFloat extends AppCompatActivity
{
    CalendarPickerView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_more_float);

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.68);   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 0.7);    //宽度设置为屏幕的0.8
        p.alpha = 1.0f;      //设置本身透明度
        p.dimAmount = 0.0f;      //设置黑暗度

        getWindow().setAttributes(p);     //设置生效
        getWindow().setGravity(Gravity.RIGHT);       //设置靠右对齐

        //setDefalutDate();
    }

    /**
     * 设置默认时间
     */
    private void setDefalutDate()
    {
        Button startTime = (Button) findViewById(R.id.startTimeBt);
        Button endTime = (Button) findViewById(R.id.endTimeBt);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance(); // 当时的日期和时间
        int day = c.get(Calendar.DAY_OF_MONTH) - 10;//提前10天的时间
        c.set(Calendar.DAY_OF_MONTH, day);
        String date = df.format(c.getTime());
        startTime.setText(date);

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateNew = sDateFormat.format(new java.util.Date());
        endTime.setText(dateNew);
    }

    public void getStartTime(View view)
    {
        Intent intent = new Intent(ActivitySearchMoreFloat.this, ActivityCalendarPickerView.class);
        intent.putExtra("type", "startTime");
        startActivityForResult(intent, 100);
    }

    public void getComformxingzhi(View view)
    {
        Intent intent = new Intent(ActivitySearchMoreFloat.this, ActivityComformXingzhiCheckbox.class);
        startActivityForResult(intent, 100);
    }

    public void getAllAlarmCompany_bt(View view)
    {
        Intent intent = new Intent(ActivitySearchMoreFloat.this, ActivityAlarmCompanycheckbox.class);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            String content = data.getStringExtra("result");
            String type = data.getStringExtra("type");
            if ("startTime".equalsIgnoreCase(type))
            {
                Button startTime = (Button) findViewById(R.id.startTimeBt);
                startTime.setText(content);
            }
            else if ("endTime".equalsIgnoreCase(type))
            {
                Button endTime = (Button) findViewById(R.id.endTimeBt);
                endTime.setText(content);
            }
            else if ("conformJingqingXingzhi".equalsIgnoreCase(type))
            {
                Button conformJingqingXingzhi = (Button) findViewById(R.id.comformxingzhi_bt);
                if (StringUtils.isNotBlank(content))
                {
                    String tmp = content.substring(0, content.lastIndexOf(","));
                    conformJingqingXingzhi.setText(tmp);
                }
                else
                {
                    conformJingqingXingzhi.setText("请选择");
                }
            }
            else if ("alarmCompany".equalsIgnoreCase(type))
            {
                Button alarmCompanyChoose = (Button) findViewById(R.id.alarmCompanyChoose);
                if (StringUtils.isNotBlank(content))
                {
                    String tmp = content.substring(0, content.lastIndexOf(","));
                    alarmCompanyChoose.setText(tmp);
                }
                else
                {
                    alarmCompanyChoose.setText("请选择");
                }
            }
            else
            {

            }

        }
    }

    public void getEndTime(View view)
    {
        Intent intent = new Intent(ActivitySearchMoreFloat.this, ActivityCalendarPickerView.class);
        intent.putExtra("type", "endTime");
        startActivityForResult(intent, 100);
    }

    public void conform(View view)
    {
        Button startTime = (Button) findViewById(R.id.startTimeBt);
        Button endTime = (Button) findViewById(R.id.endTimeBt);
        Button alarmCompanyChoose = (Button) findViewById(R.id.alarmCompanyChoose);
        Button conformJingqingXingzhi = (Button) findViewById(R.id.comformxingzhi_bt);
        Intent intent = new Intent(ActivitySearchMoreFloat.this, AfterLogin.class);
        if (!"开始日期".equals(startTime.getText().toString()))
        {
            intent.putExtra("startTime", startTime.getText().toString());
        }

        if (!"结束日期".equals(endTime.getText().toString()))
        {
            intent.putExtra("endTime", endTime.getText().toString());
        }

        if (!"请选择".equals(alarmCompanyChoose.getText().toString()))
        {
            intent.putExtra("alarmCompany", alarmCompanyChoose.getText().toString());
        }
        if (!"请选择".equals(conformJingqingXingzhi.getText().toString()))
        {
            intent.putExtra("conformJingqingXingzhi", conformJingqingXingzhi.getText().toString());
        }

        EditText chooseIdNo = (EditText) findViewById(R.id.chooseIdNo);
        if (chooseIdNo != null && StringUtils.isNotBlank(chooseIdNo.getText()))
        {
            intent.putExtra("chooseIdNo", chooseIdNo.getText().toString());
        }

        EditText informantPhoneChoose = (EditText) findViewById(R.id.informantPhoneChoose);
        if (null != informantPhoneChoose && StringUtils.isNotBlank(informantPhoneChoose.getText()))
        {
            intent.putExtra("informantPhoneChoose", informantPhoneChoose.getText().toString());
        }

        setResult(RESULT_OK, intent);
        this.setVisible(false);
        finish();
    }

    public void cancel(View view)
    {
        onBackPressed();
    }


}
