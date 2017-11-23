package com.huizhou.huidongpolice.afterLogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.huizhou.huidongpolice.R;
import com.huizhou.huidongpolice.Request.GetLastSevenNumberJingQingReq;
import com.huizhou.huidongpolice.database.vo.JingQingInfoRecord;
import com.huizhou.huidongpolice.utils.CommonCache;
import com.huizhou.huidongpolice.utils.CommonUtils;
import com.huizhou.huidongpolice.utils.Flags;
import com.huizhou.huidongpolice.utils.GlideImageLoader;
import com.huizhou.huidongpolice.utils.Log4J;
import com.huizhou.huidongpolice.utils.ProcessDataBaseThread;
import com.huizhou.huidongpolice.utils.ServerConfig;
import com.huizhou.huidongpolice.utils.StaticNumber;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.transformer.AccordionTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 登录后主页显示
 */
public class MainNewsFragment2 extends Fragment implements OnBannerListener
{

    Banner banner;
    private String userName;
    private String classInfo;
    private Button newStatic;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        View view = inflater.inflate(R.layout.tab01, container, false);
        newStatic = (Button) view.findViewById(R.id.btnewstatic);

        //图片轮播
        banner = (Banner) view.findViewById(R.id.banner);
        String urlImg = "http://" + ServerConfig.SERVER_IP + ":" + ServerConfig.SERVER_PORT + "/huidongpolice/imageshow/";
        List<String> list = new ArrayList<String>();
        list.add(urlImg+"tab1top.png");
        list.add(urlImg+"tab2top.png");
        list.add(urlImg+"tab3top.png");
        list.add(urlImg+"tab4top.png");
        banner.setBannerAnimation(AccordionTransformer.class);
        banner.setImages(list)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(this)
                .start();

        onCreateMySelf(view);

        //引入布局
        return view;
    }

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart()
    {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    @Override
    public void OnBannerClick(int position)
    {

    }

    private void onCreateMySelf(View view)
    {
        ListView listView = (ListView) view.findViewById(R.id.mainListView);

        SharedPreferences userSettings = getActivity().getSharedPreferences("userInfo", 0);
        userName = userSettings.getString("loginUserName", "default");
        classInfo = CommonUtils.getFileLineMethod(new Exception());

        //获取到集合数据
        ProcessDataBaseThread pd = new ProcessDataBaseThread();
        pd.setFlag(Flags.ONE_ZERO_ZERO_TWO);

        GetLastSevenNumberJingQingReq req = new GetLastSevenNumberJingQingReq();
        req.setOperatorId(userName);
        pd.setObject(req);

        Thread t = new Thread(pd);
        t.start();
        List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        try
        {
            for (int i = 0; i <= StaticNumber.TWO_ZERO; i++)
            {
                Thread.sleep(StaticNumber.FIVE_HUNDRED);

                Object o = CommonCache.dataCache.get(Flags.ONE_ZERO_ZERO_TWO);
                if (null != o)
                {
                    List<JingQingInfoRecord> jingQingList = (List<JingQingInfoRecord>) o;
                    for (JingQingInfoRecord jingQing : jingQingList)
                    {
                        HashMap<String, Object> item = new HashMap<String, Object>();
                        item.put("id", jingQing.getId());
                        String temp = jingQing.getBriefly();
                        if (temp.length() > StaticNumber.EIGHTY)
                        {
                            item.put("brieflyTab1", jingQing.getBriefly().substring(0, StaticNumber.EIGHTY) + "....");
                        }
                        else
                        {
                            item.put("brieflyTab1", jingQing.getBriefly());
                        }
                        item.put("comformXingZhiTab1", jingQing.getComformXingZhi());
                        item.put("alarmCompanyTab1", jingQing.getAlarmCompany());
                        item.put("alarmDateTab1", jingQing.getAlarmDate());
                        data.add(item);
                    }

                    CommonCache.dataCache.remove(Flags.ONE_ZERO_ZERO_TWO);
                    break;
                }
                else
                {
                    if (i == StaticNumber.TEN)
                    {
                        Thread t2 = new Thread(pd);
                        t2.start();

                        Thread.sleep(StaticNumber.FIVE_HUNDRED);
                        o = CommonCache.dataCache.get(Flags.ONE_ZERO_ZERO_TWO);
                        if (null != o)
                        {
                            List<JingQingInfoRecord> jingQingList = (List<JingQingInfoRecord>) o;
                            for (JingQingInfoRecord jingQing : jingQingList)
                            {
                                HashMap<String, Object> item = new HashMap<String, Object>();
                                item.put("id", jingQing.getId());
                                String temp = jingQing.getBriefly();
                                if (temp.length() > StaticNumber.SEVENTY)
                                {
                                    item.put("brieflyTab1", jingQing.getBriefly().substring(0, StaticNumber.SEVENTY) + "....");
                                }
                                else
                                {
                                    item.put("brieflyTab1", jingQing.getBriefly());
                                }
                                item.put("comformXingZhiTab1", jingQing.getComformXingZhi());
                                item.put("alarmCompanyTab1", jingQing.getAlarmCompany());
                                item.put("alarmDateTab1", jingQing.getAlarmDate());
                                data.add(item);
                            }

                            CommonCache.dataCache.remove(Flags.ONE_ZERO_ZERO_TWO);
                            break;
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log4J.getLogger(this.getClass()).error("查询最新警讯失败：" + e.getMessage());
            CommonUtils.sendLogsToServer(classInfo, "失败：" + e.getMessage(), userName);
        }

        //创建SimpleAdapter适配器将数据绑定到item显示控件上
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, R.layout.main_news_item,
                new String[]{"brieflyTab1", "comformXingZhiTab1", "alarmDateTab1"},
                new int[]{R.id.brieflyTab1, R.id.comformXingZhiTab1, R.id.alarmDateTab1}
        );
        //实现列表的显示
        listView.setAdapter(adapter);
        //条目点击事件
        listView.setOnItemClickListener(new ItemClickListener());
    }


    //获取点击事件
    private final class ItemClickListener implements AdapterView.OnItemClickListener
    {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            ListView listView = (ListView) parent;
            HashMap<String, Object> data = (HashMap<String, Object>) listView.getItemAtPosition(position);
            String jinQingId = data.get("id").toString();
            //Toast.makeText(getActivity(), jinQingId, Toast.LENGTH_SHORT).show();

            //跳转到详细页面
            Intent it = new Intent(getActivity(), ActivityJingInfoDetail.class);
            it.putExtra("jinQingId", jinQingId);
            startActivity(it);
            //getActivity().finish();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        try
        {
            /**
             * 跳转 警情排名
             *
             * @param view
             */
            newStatic.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent it = new Intent(getActivity(), ActivityNewStatic.class);
                    startActivityForResult(it, 100);
                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
            CommonUtils.sendLogsToServer(classInfo, "点击统计排名失败：" + e.getMessage(), userName);
        }


    }

}
