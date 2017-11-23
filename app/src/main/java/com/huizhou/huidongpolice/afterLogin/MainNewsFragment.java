package com.huizhou.huidongpolice.afterLogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录后主页显示
 */
public class MainNewsFragment extends Fragment implements OnBannerListener
{

    Banner banner;
    private String userName;
    private String classInfo;
    private Button newStatic;


    //数据保存Map
    private Map<Integer, String> mapBriefly = new HashMap<Integer, String>();
    private List<Integer> listIds = new ArrayList<Integer>(StaticNumber.ONE_HUNDRED);
    private Map<Integer, String> mapComformXingZhiTab2 = new HashMap<Integer, String>();
    private Map<Integer, String> mapAlarmCompanyTab2 = new HashMap<Integer, String>();
    private Map<Integer, String> mapAlarmDateTab2 = new HashMap<Integer, String>();
    private List<String> briefQery = new ArrayList<String>();

    private ListViewAdapterTab2 adapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        View view = inflater.inflate(R.layout.tab01, container, false);
        newStatic = (Button) view.findViewById(R.id.btnewstatic);

        SharedPreferences userSettings = getActivity().getSharedPreferences("userInfo", 0);
        userName = userSettings.getString("loginUserName", "default");
        classInfo = CommonUtils.getFileLineMethod(new Exception());

        //图片轮播
        banner = (Banner) view.findViewById(R.id.banner);
         String urlImg = "http://" + ServerConfig.SERVER_IP + ":" + ServerConfig.SERVER_PORT + "/huidongpolice/imageshow/";
        List<String> list = new ArrayList<String>();
        list.add(urlImg + "tab1top.png");
        list.add(urlImg + "tab2top.png");
        list.add(urlImg + "tab3top.png");
        list.add(urlImg + "tab4top.png");
        banner.setBannerAnimation(AccordionTransformer.class);

        banner.setImages(list);
        banner.setImageLoader(new GlideImageLoader());
        banner.setOnBannerListener(this);
       // banner.update(list);
        banner.start();

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
                    for (JingQingInfoRecord info : jingQingList)
                    {
                        listIds.add(info.getId());

                        String temp = info.getBriefly();
                        if (temp.length() > StaticNumber.EIGHTY)
                        {
                            mapBriefly.put(info.getId(), temp.substring(0, StaticNumber.EIGHTY) + "....");
                        }
                        else
                        {
                            mapBriefly.put(info.getId(), info.getBriefly());
                        }

                        mapAlarmCompanyTab2.put(info.getId(), info.getAlarmCompany());
                        mapComformXingZhiTab2.put(info.getId(), info.getComformXingZhi());
                        mapAlarmDateTab2.put(info.getId(), info.getAlarmDate());
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
                            for (JingQingInfoRecord info : jingQingList)
                            {
                                listIds.add(info.getId());

                                String temp = info.getBriefly();
                                if (temp.length() > StaticNumber.SEVENTY)
                                {
                                    mapBriefly.put(info.getId(), temp.substring(0, StaticNumber.SEVENTY) + "....");
                                }
                                else
                                {
                                    mapBriefly.put(info.getId(), info.getBriefly());
                                }

                                mapAlarmCompanyTab2.put(info.getId(), info.getAlarmCompany());
                                mapComformXingZhiTab2.put(info.getId(), info.getComformXingZhi());
                                mapAlarmDateTab2.put(info.getId(), info.getAlarmDate());
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


        adapter = new ListViewAdapterTab2(getActivity(), mapBriefly, listIds,
                mapComformXingZhiTab2, mapAlarmCompanyTab2,
                mapAlarmDateTab2, briefQery
        );
        listView.setAdapter(adapter);

        //条目点击事件
        listView.setOnItemClickListener(new MainNewsFragment.ItemClickListener());
    }


    //获取点击事件
    private final class ItemClickListener implements AdapterView.OnItemClickListener
    {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            ListViewAdapterTab2.ViewHolder holder = (ListViewAdapterTab2.ViewHolder) view.getTag();
            String jinQingId = holder.id.getText().toString();
            //跳转到详细页面
            Intent it = new Intent(getActivity(), ActivityJingInfoDetail.class);
            it.putExtra("jinQingId", jinQingId);
            startActivity(it);
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
