package com.huizhou.huidongpolice.afterLogin.checkbox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.huizhou.huidongpolice.R;
import com.huizhou.huidongpolice.Request.GetAllConformJingqingXingzhiReq;
import com.huizhou.huidongpolice.afterLogin.ActivitySearchMoreFloat;
import com.huizhou.huidongpolice.utils.CommonCache;
import com.huizhou.huidongpolice.utils.CommonUtils;
import com.huizhou.huidongpolice.utils.Flags;
import com.huizhou.huidongpolice.utils.Log4J;
import com.huizhou.huidongpolice.utils.ProcessDataBaseThread;
import com.huizhou.huidongpolice.utils.StaticNumber;

import java.util.ArrayList;
import java.util.List;

public class ActivityComformXingzhiCheckbox extends AppCompatActivity
{

    private ListView mListView;
    private List<Model> models;
    private CheckBox mMainCkb;
    private CheckboxAdapter checkboxAdapter;

    //监听来源
    public boolean mIsFromItem = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comform_xingzhi_checkbox);

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.8);   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 0.8);    //宽度设置为屏幕的0.8
        p.alpha = 1.0f;      //设置本身透明度
        p.dimAmount = 0.0f;      //设置黑暗度
        getWindow().setAttributes(p);     //设置生效
        getWindow().setGravity(Gravity.RIGHT);       //设置靠右对齐

        initView();
        initData();
        initViewOper();
    }

    /**
     * view初始化
     */
    private void initView()
    {
        mListView = (ListView) findViewById(R.id.list_main_comformxingzhi);
        mMainCkb = (CheckBox) findViewById(R.id.ckb_main_comformxingzhi);
    }

    public void getAllconformJingqingXingzhi(View view)
    {
        StringBuilder sb = new StringBuilder();
        for (Model m : models)
        {
            if (m.ischeck())
            {
                sb.append(m.getStr());
                sb.append(",");
            }
        }

        Intent intent = new Intent(ActivityComformXingzhiCheckbox.this, ActivitySearchMoreFloat.class);
        intent.putExtra("result", sb.toString());
        intent.putExtra("type", "conformJingqingXingzhi");
        setResult(RESULT_OK, intent);
        onBackPressed();
    }

    /**
     * 数据加载
     */
    private void initData()
    {
        models = new ArrayList<Model>();

        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        String userName = userSettings.getString("loginUserName", "default");

        String classInfo = CommonUtils.getFileLineMethod(new Exception());

        //获取到集合数据
        ProcessDataBaseThread pd = new ProcessDataBaseThread();
        pd.setFlag(Flags.SEARCH_ALL_CONFORM_JINGQING_XINGZHI);

        GetAllConformJingqingXingzhiReq req = new GetAllConformJingqingXingzhiReq();
        req.setOperatorId(userName);
        pd.setObject(req);

        Thread t = new Thread(pd);
        t.start();
        try
        {
            for (int i = 0; i <= StaticNumber.TWO_ZERO; i++)
            {
                Thread.sleep(StaticNumber.FIVE_HUNDRED);

                Object o = CommonCache.dataCache.get(Flags.SEARCH_ALL_CONFORM_JINGQING_XINGZHI);
                if (null != o)
                {
                    List<String> jingQingList = (List<String>) o;
                    Model model;
                    for (String s : jingQingList)
                    {
                        model = new Model();
                        model.setStr(s);
                        model.setIscheck(false);
                        models.add(model);
                    }

                    CommonCache.dataCache.remove(Flags.SEARCH_ALL_CONFORM_JINGQING_XINGZHI);
                    break;
                }
                else
                {
                    if (i == StaticNumber.TEN)
                    {
                        Thread t2 = new Thread(pd);
                        t2.start();

                        Thread.sleep(StaticNumber.FIVE_HUNDRED);
                        o = CommonCache.dataCache.get(Flags.ONE_ZERO_ZERO_THREE);
                        if (null != o)
                        {
                            List<String> jingQingList = (List<String>) o;
                            Model model;
                            for (String s : jingQingList)
                            {
                                model = new Model();
                                model.setStr(s);
                                model.setIscheck(false);
                                models.add(model);
                            }

                            CommonCache.dataCache.remove(Flags.SEARCH_ALL_CONFORM_JINGQING_XINGZHI);
                            break;
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log4J.getLogger(this.getClass()).error("查询失败：" + e.getMessage());
            CommonUtils.sendLogsToServer(classInfo, "查询失败：" + e.getMessage(), userName);
        }
    }

    /**
     * 数据绑定
     */
    private void initViewOper()
    {
        checkboxAdapter = new CheckboxAdapter(models, this, new AllCheckListener()
        {

            @Override
            public void onCheckedChanged(boolean b)
            {
                //根据不同的情况对maincheckbox做处理
                if (!b && !mMainCkb.isChecked())
                {
                    return;
                }
                else if (!b && mMainCkb.isChecked())
                {
                    mIsFromItem = true;
                    mMainCkb.setChecked(false);
                }
                else if (b)
                {
                    mIsFromItem = true;
                    mMainCkb.setChecked(true);
                }
            }
        });
        mListView.setAdapter(checkboxAdapter);
        //全选的点击监听
        mMainCkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                //当监听来源为点击item改变maincbk状态时不在监听改变，防止死循环
                if (mIsFromItem)
                {
                    mIsFromItem = false;
                    return;
                }

                //改变数据
                for (Model model : models)
                {
                    model.setIscheck(b);
                }
                //刷新listview
                checkboxAdapter.notifyDataSetChanged();
            }
        });

    }

    //对item导致maincheckbox改变做监听
    interface AllCheckListener
    {
        void onCheckedChanged(boolean b);
    }
}
