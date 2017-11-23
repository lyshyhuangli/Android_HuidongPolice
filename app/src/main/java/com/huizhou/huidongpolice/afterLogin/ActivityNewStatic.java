package com.huizhou.huidongpolice.afterLogin;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.huizhou.huidongpolice.R;
import com.huizhou.huidongpolice.Request.GetWeekAlarmStaticReq;
import com.huizhou.huidongpolice.utils.AnQingRecord;
import com.huizhou.huidongpolice.utils.CommonCache;
import com.huizhou.huidongpolice.utils.CommonUtils;
import com.huizhou.huidongpolice.utils.Flags;
import com.huizhou.huidongpolice.utils.Log4J;
import com.huizhou.huidongpolice.utils.ProcessDataBaseThread;
import com.huizhou.huidongpolice.utils.StaticNumber;

import java.util.List;

public class ActivityNewStatic extends AppCompatActivity
{

    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int FP = ViewGroup.LayoutParams.FILL_PARENT;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_static);

        try
        {
            processTabActivity();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void tobackJingqing(View view)
    {
        super.onBackPressed();
    }

    private void processTabActivity()
    {
        processTable();

        TabHost th = (TabHost) findViewById(R.id.tabhost);
        th.setup();            //初始化TabHost容器

        //在TabHost创建标签，然后设置：标题／图标／标签页布局
        th.addTab(th.newTabSpec("tab1").setIndicator("一类派出所", getResources().getDrawable(R.drawable.ic_launcher)).setContent(R.id.tab1));
        th.addTab(th.newTabSpec("tab2").setIndicator("二类派出所", null).setContent(R.id.tab2));
        th.addTab(th.newTabSpec("tab3").setIndicator("三类派出所", null).setContent(R.id.tab3));

        for (int i = 0; i < th.getTabWidget().getChildCount(); i++)
        {
            //View view = th.getTabWidget().getChildAt(i);
            TextView tv = (TextView) th.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(15);
            tv.setTypeface(Typeface.SERIF, 1); // 设置字体和风格
        }

    }

    private void processTable()
    {
        //新建TableLayout01的实例
        TableLayout tableLayout1 = (TableLayout) findViewById(R.id.tableStatic1);
        TableLayout tableLayout2 = (TableLayout) findViewById(R.id.tableStatic2);
        TableLayout tableLayout3 = (TableLayout) findViewById(R.id.tableStatic3);
        //全部列自动填充空白处
        tableLayout1.setStretchAllColumns(true);
        tableLayout2.setStretchAllColumns(true);
        tableLayout3.setStretchAllColumns(true);

        TableRow tableRow1 = new TableRow(this);
        tableRow1.setGravity(Gravity.CENTER);
        TableRow tableRow2 = new TableRow(this);
        tableRow2.setGravity(Gravity.CENTER);
        TableRow tableRow3 = new TableRow(this);
        tableRow3.setGravity(Gravity.CENTER);

        TextView tv1 = new TextView(this);
        tv1.setText("排名");
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextSize(15);
        TextView tv2 = new TextView(this);
        tv2.setText("排名");
        tv2.setTextSize(15);
        tv2.setGravity(Gravity.CENTER);
        TextView tv3 = new TextView(this);
        tv3.setText("排名");
        tv3.setTextSize(15);
        tv3.setGravity(Gravity.CENTER);
        tableRow1.addView(tv1);
        tableRow2.addView(tv2);
        tableRow3.addView(tv3);

        TextView tv2_1 = new TextView(this);
        TextView tv2_2 = new TextView(this);
        TextView tv2_3 = new TextView(this);
        tv2_1.setText("单位");
        tv2_1.setTextSize(15);
        tv2_1.setGravity(Gravity.CENTER);
        tv2_2.setText("单位");
        tv2_2.setTextSize(15);
        tv2_2.setGravity(Gravity.CENTER);
        tv2_3.setText("单位");
        tv2_3.setTextSize(15);
        tv2_3.setGravity(Gravity.CENTER);
        tableRow1.addView(tv2_1);
        tableRow2.addView(tv2_2);
        tableRow3.addView(tv2_3);

        TextView tv3_1 = new TextView(this);
        TextView tv3_2 = new TextView(this);
        TextView tv3_3 = new TextView(this);
        tv3_1.setText("警情");
        tv3_2.setText("警情");
        tv3_3.setText("警情");
        tv3_1.setTextSize(15);
        tv3_2.setTextSize(15);
        tv3_3.setTextSize(15);
        tv3_1.setGravity(Gravity.CENTER);
        tv3_2.setGravity(Gravity.CENTER);
        tv3_3.setGravity(Gravity.CENTER);
        tableRow1.addView(tv3_1);
        tableRow2.addView(tv3_2);
        tableRow3.addView(tv3_3);

        tableLayout1.addView(tableRow1, new TableLayout.LayoutParams(FP, WC));
        tableLayout2.addView(tableRow2, new TableLayout.LayoutParams(FP, WC));
        tableLayout3.addView(tableRow3, new TableLayout.LayoutParams(FP, WC));

        List<List<AnQingRecord>> infoList = getData();
        if (null == infoList)
        {
            Log4J.getLogger(this.getClass()).error("查询一周统计失败");
            return;
        }

        List<AnQingRecord> type1 = infoList.get(0);
        for (int i = 0; i < type1.size(); i++)
        {
            TableRow tableRow2_1 = new TableRow(this);
            tableRow2_1.setGravity(Gravity.CENTER);
            AnQingRecord r = type1.get(i);

            TextView tvNo = new TextView(this);
            tvNo.setText(i + 1 + "");
            tvNo.setGravity(Gravity.CENTER);
            tvNo.setTextSize(15);
            tableRow2_1.addView(tvNo);

            TextView tvAlarmCompany = new TextView(this);
            tvAlarmCompany.setText(r.getAlarm_Company());
            tvAlarmCompany.setGravity(Gravity.LEFT);
            tvAlarmCompany.setTextSize(15);
            tableRow2_1.addView(tvAlarmCompany);

            TextView tvNumber = new TextView(this);
            tvNumber.setText(r.getNumber() + "");
            tvNumber.setGravity(Gravity.CENTER);
            tvNumber.setTextSize(15);
            tableRow2_1.addView(tvNumber);

            //新建的TableRow添加到TableLayout
            tableLayout1.addView(tableRow2_1, new TableLayout.LayoutParams(FP, WC));
        }

        List<AnQingRecord> type2 = infoList.get(1);
        for (int i = 0; i < type2.size(); i++)
        {
            TableRow tableRow2_2 = new TableRow(this);
            tableRow2_2.setGravity(Gravity.CENTER);
            AnQingRecord r = type2.get(i);

            TextView tvNo = new TextView(this);
            tvNo.setText(i + 1 + "");
            tvNo.setGravity(Gravity.CENTER);
            tvNo.setTextSize(15);
            tableRow2_2.addView(tvNo);

            TextView tvAlarmCompany = new TextView(this);
            tvAlarmCompany.setText(r.getAlarm_Company());
            tvAlarmCompany.setGravity(Gravity.LEFT);
            tvAlarmCompany.setTextSize(15);
            tableRow2_2.addView(tvAlarmCompany);

            TextView tvNumber = new TextView(this);
            tvNumber.setText(r.getNumber() + "");
            tvNumber.setGravity(Gravity.CENTER);
            tvNumber.setTextSize(15);
            tableRow2_2.addView(tvNumber);

            //新建的TableRow添加到TableLayout
            tableLayout2.addView(tableRow2_2, new TableLayout.LayoutParams(FP, WC));
        }

        List<AnQingRecord> type3 = infoList.get(2);
        for (int i = 0; i < type3.size(); i++)
        {
            TableRow tableRow2_3 = new TableRow(this);
            tableRow2_3.setGravity(Gravity.CENTER);
            AnQingRecord r = type3.get(i);

            TextView tvNo = new TextView(this);
            tvNo.setText(i + 1 + "");
            tvNo.setGravity(Gravity.CENTER);
            tvNo.setTextSize(15);
            tableRow2_3.addView(tvNo);

            TextView tvAlarmCompany = new TextView(this);
            tvAlarmCompany.setText(r.getAlarm_Company());
            tvAlarmCompany.setGravity(Gravity.LEFT);
            tvAlarmCompany.setTextSize(15);
            tableRow2_3.addView(tvAlarmCompany);

            TextView tvNumber = new TextView(this);
            tvNumber.setText(r.getNumber() + "");
            tvNumber.setGravity(Gravity.CENTER);
            tvNumber.setTextSize(15);
            tableRow2_3.addView(tvNumber);

            //新建的TableRow添加到TableLayout
            tableLayout3.addView(tableRow2_3, new TableLayout.LayoutParams(FP, WC));
        }
    }

    private List<List<AnQingRecord>> getData()
    {
        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        String userName = userSettings.getString("loginUserName", "default");
        String classInfo = CommonUtils.getFileLineMethod(new Exception());

        List<List<AnQingRecord>> infoList = null;
        //获取到集合数据
        ProcessDataBaseThread pd = new ProcessDataBaseThread();
        pd.setFlag(Flags.STATIC_WEEK_ALARM_COUNT);

        GetWeekAlarmStaticReq req = new GetWeekAlarmStaticReq();
        req.setOperatorId(userName);
        pd.setObject(req);

        Thread t = new Thread(pd);
        t.start();
        try
        {
            for (int i = 0; i <= StaticNumber.TWO_ZERO; i++)
            {
                Thread.sleep(StaticNumber.FIVE_HUNDRED);

                Object o = CommonCache.dataCache.get(Flags.STATIC_WEEK_ALARM_COUNT);
                if (null != o)
                {
                    infoList = (List<List<AnQingRecord>>) o;
                    CommonCache.dataCache.remove(Flags.STATIC_WEEK_ALARM_COUNT);
                    break;
                }
                else
                {
                    if (i == StaticNumber.TEN)
                    {
                        Thread t2 = new Thread(pd);
                        t2.start();

                        Thread.sleep(StaticNumber.FIVE_HUNDRED);
                        o = CommonCache.dataCache.get(Flags.STATIC_WEEK_ALARM_COUNT);
                        if (null != o)
                        {
                            infoList = (List<List<AnQingRecord>>) o;
                            CommonCache.dataCache.remove(Flags.STATIC_WEEK_ALARM_COUNT);
                            break;
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log4J.getLogger(this.getClass()).error("查询失败：" + e.getMessage());
            CommonUtils.sendLogsToServer(classInfo, "失败：" + e.getMessage(), userName);
        }

        return infoList;
    }
}
