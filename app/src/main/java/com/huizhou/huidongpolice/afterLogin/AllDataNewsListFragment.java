package com.huizhou.huidongpolice.afterLogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.huizhou.huidongpolice.R;
import com.huizhou.huidongpolice.Request.GetJingQingMoreInfoByCountReq;
import com.huizhou.huidongpolice.database.vo.JingQingInfoRecord;
import com.huizhou.huidongpolice.utils.CommonCache;
import com.huizhou.huidongpolice.utils.CommonUtils;
import com.huizhou.huidongpolice.utils.Flags;
import com.huizhou.huidongpolice.utils.Log4J;
import com.huizhou.huidongpolice.utils.ProcessDataBaseThread;
import com.huizhou.huidongpolice.utils.StaticNumber;

import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 所有警讯列表界面
 */
public class AllDataNewsListFragment extends Fragment implements AbsListView.OnScrollListener
{

    //private View loadmoreView;
    private LayoutInflater inflater;
    private ListView listView;
    private int last_index;
    private int total_index;

    //数据保存Map
    private Map<Integer, String> mapBriefly = new HashMap<Integer, String>();
    private List<Integer> listIds = new ArrayList<Integer>(StaticNumber.ONE_HUNDRED);
    private Map<Integer, String> mapComformXingZhiTab2 = new HashMap<Integer, String>();
    private Map<Integer, String> mapAlarmCompanyTab2 = new HashMap<Integer, String>();
    private Map<Integer, String> mapAlarmDateTab2 = new HashMap<Integer, String>();
    private List<String> briefQery = new ArrayList<String>();

    private boolean isLoading = false;//表示是否正处于加载状态
    private ListViewAdapterTab2 adapter;

    private int getDataNo = 1;
    private String startTime;
    private String endTime;
    private String briefly;
    private String conformJingqingXingzhi;
    private String alarmCompany;
    private String chooseIdNo;
    private String informantPhoneChoose;

    private View view;
    private SearchView sv_wd;
    private Button searchMore;
    private Button searchReset;

    private String userName;
    private String classInfo;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        view = inflater.inflate(R.layout.tab02, null);

        searchMore = (Button) view.findViewById(R.id.searchMore);
        searchReset = (Button) view.findViewById(R.id.searchReset);

        SharedPreferences userSettings = getActivity().getSharedPreferences("userInfo", 0);
        userName = userSettings.getString("loginUserName", "default");
        classInfo = CommonUtils.getFileLineMethod(new Exception());


        //获取查询的参数
        if (null != getFragmentManager().findFragmentByTag("queryParams") &&
                null != getFragmentManager().findFragmentByTag("queryParams").getArguments())
        {
            String query = getFragmentManager().findFragmentByTag("queryParams").getArguments().getString("query");
            if (StringUtils.isNotBlank(query) && "1".equals(query))
            {
                startTime = getFragmentManager().findFragmentByTag("queryParams").getArguments().getString("startTime");
                endTime = getFragmentManager().findFragmentByTag("queryParams").getArguments().getString("endTime");
                conformJingqingXingzhi = getFragmentManager().findFragmentByTag("queryParams").getArguments().getString("conformJingqingXingzhi");
                alarmCompany = getFragmentManager().findFragmentByTag("queryParams").getArguments().getString("alarmCompany");
                chooseIdNo = getFragmentManager().findFragmentByTag("queryParams").getArguments().getString("chooseIdNo");
                informantPhoneChoose = getFragmentManager().findFragmentByTag("queryParams").getArguments().getString("informantPhoneChoose");

                searchMore.setVisibility(View.GONE);
                searchReset.setVisibility(View.VISIBLE);
                getFragmentManager().findFragmentByTag("queryParams").setArguments(new Bundle());
            }
            else
            {
                startTime = null;
                endTime = null;
                briefly = null;
                conformJingqingXingzhi = null;
                alarmCompany = null;
                chooseIdNo = null;
                informantPhoneChoose = null;

                searchMore.setVisibility(View.VISIBLE);
                searchReset.setVisibility(View.GONE);
            }
        }
        else
        {
            startTime = null;
            endTime = null;
            briefly = null;
            searchMore.setVisibility(View.VISIBLE);
            searchReset.setVisibility(View.GONE);
        }

        loadFirstTime();
        //引入布局
        return view;
    }

    /**
     * 第一次加载数据
     */
    private void loadFirstTime()
    {
        //移除搜索焦点
        sv_wd = (SearchView) view.findViewById(R.id.searchView);
        sv_wd.setFocusable(false);
        sv_wd.setFocusableInTouchMode(false);
        sv_wd.clearFocus();

        if (null != sv_wd)
        {
            int id = sv_wd.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView textView = (TextView) sv_wd.findViewById(id);
            textView.setTextSize(15);//字体、提示字体大小
        }

        inflater = null;
        //loadmoreView = null;
        listView = null;

        mapBriefly.clear();
        listIds.clear();
        mapComformXingZhiTab2.clear();
        mapAlarmCompanyTab2.clear();
        mapAlarmDateTab2.clear();
        briefQery.clear();
        getDataNo = 1;

        inflater = LayoutInflater.from(getActivity());
        //loadmoreView = inflater.inflate(R.layout.load_more, null);//获得刷新视图
       // loadmoreView.setVisibility(View.INVISIBLE);//设置刷新视图默认情况下是不可见的
        listView = (ListView) view.findViewById(R.id.allDataListView);

        listView.setOnScrollListener(this);
        //listView.addFooterView(loadmoreView, null, false);

        if (StringUtils.isNotBlank(briefly))
        {
            String[] strs = briefly.split(" ");
            for (String s : strs)
            {
                if (StringUtils.isNotBlank(s))
                {
                    briefQery.add(s);
                }
            }
        }

        //初始化一次数据
        getMoreData(startTime, endTime, briefly);
        adapter = new ListViewAdapterTab2(getActivity(), mapBriefly, listIds,
                mapComformXingZhiTab2, mapAlarmCompanyTab2,
                mapAlarmDateTab2, briefQery
        );
        listView.setAdapter(adapter);


        //条目点击事件
        listView.setOnItemClickListener(new AllDataNewsListFragment.ItemClickListener());
    }

    //获取点击事件
    private final class ItemClickListener implements AdapterView.OnItemClickListener
    {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            //移除搜索焦点
            sv_wd.setFocusable(false);
            sv_wd.setFocusableInTouchMode(false);
            sv_wd.clearFocus();

            //ListView listView = (ListView) parent;
            ListViewAdapterTab2.ViewHolder holder = (ListViewAdapterTab2.ViewHolder) view.getTag();
            String jinQingId = holder.id.getText().toString();
            //跳转到详细页面
            Intent it = new Intent(getActivity(), ActivityJingInfoDetail.class);
            it.putExtra("jinQingId", jinQingId);
            startActivity(it);
        }
    }


    /**
     * 滑动获取更多数据
     */
    private void getMoreData(String startTime, String endTime, String briefly)
    {
        try
        {
            //获取到集合数据
            ProcessDataBaseThread pd = new ProcessDataBaseThread();
            pd.setFlag(Flags.ONE_ZERO_ZERO_FOUR);

            GetJingQingMoreInfoByCountReq req = new GetJingQingMoreInfoByCountReq();
            req.setCount(getDataNo);
            req.setBriefly(briefly);
            req.setChooseIdNo(chooseIdNo);
            req.setConformJingqingXingzhi(conformJingqingXingzhi);
            req.setAlarmCompany(alarmCompany);
            req.setEndTime(endTime);
            req.setStartTime(startTime);
            req.setInformantPhoneChoose(informantPhoneChoose);
            req.setOperatorId(userName);
            pd.setObject(req);

            Thread t = new Thread(pd);
            t.start();

            for (int i = 0; i <= StaticNumber.TWO_ZERO; i++)
            {
                Thread.sleep(StaticNumber.FIVE_HUNDRED);

                Object o = CommonCache.dataCache.get(Flags.ONE_ZERO_ZERO_FOUR);
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

                    CommonCache.dataCache.remove(Flags.ONE_ZERO_ZERO_FOUR);
                    break;
                }
                else
                {
                    if (i == StaticNumber.TEN)
                    {
                        Thread t2 = new Thread(pd);
                        t2.start();

                        Thread.sleep(StaticNumber.FIVE_HUNDRED);
                        o = CommonCache.dataCache.get(Flags.ONE_ZERO_ZERO_FOUR);
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

                            CommonCache.dataCache.remove(Flags.ONE_ZERO_ZERO_FOUR);
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
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        try
        {
            last_index = firstVisibleItem + visibleItemCount;
            total_index = totalItemCount;
            //System.out.println("last:  " + last_index);
            //System.out.println("total:  " + total_index);
        }
        catch (Exception e)
        {
            CommonUtils.sendLogsToServer(classInfo, "获取更多信息失败：" + e.getMessage(), userName);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
        try
        {
            if (last_index == total_index && (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE))
            {
                //表示此时需要显示刷新视图界面进行新数据的加载(要等滑动停止)
                if (!isLoading)
                {
                    //不处于加载状态的话对其进行加载
                    isLoading = true;
                    //滑动次数加1
                    getDataNo += 1;
                    //设置刷新界面可见
                    //loadmoreView.setVisibility(View.VISIBLE);
                    //listView.addFooterView(loadmoreView, null, true);
                    onLoad();
                }
            }
        }
        catch (Exception e)
        {
            CommonUtils.sendLogsToServer(classInfo, "获取更多信息失败：" + e.getMessage(), userName);
        }

    }

    /**
     * 刷新加载
     */
    public void onLoad()
    {
        try
        {
            if (adapter == null)
            {
                adapter = new ListViewAdapterTab2(getActivity(), mapBriefly, listIds,
                        mapComformXingZhiTab2, mapAlarmCompanyTab2,
                        mapAlarmDateTab2, briefQery
                );
                listView.setAdapter(adapter);
            }
            else
            {
                //获取更多数据
                mapBriefly.clear();
                listIds.clear();
                mapComformXingZhiTab2.clear();
                mapAlarmCompanyTab2.clear();
                mapAlarmDateTab2.clear();
                //briefQery.clear();
                getMoreData(startTime, endTime, briefly);
                //listView.setVisibility(View.GONE);
                adapter.updateView(mapBriefly, listIds, mapComformXingZhiTab2, mapAlarmCompanyTab2,
                        mapAlarmDateTab2, briefQery, listView
                );

                //listView.setVisibility(View.VISIBLE);
            }

            loadComplete();//刷新结束
        }
        catch (Exception e)
        {
            CommonUtils.sendLogsToServer(classInfo, "获取更多信息失败：" + e.getMessage(), userName);
        }
    }

    /**
     * 加载完成
     */
    public void loadComplete()
    {
        try
        {
            //loadmoreView.setVisibility(View.GONE);//设置刷新界面不可见
            isLoading = false;//设置正在刷新标志位false
            getActivity().invalidateOptionsMenu();
           // listView.removeFooterView(loadmoreView);//如果是最后一页的话，则将其从ListView中移出
        }
        catch (Exception e)
        {
            CommonUtils.sendLogsToServer(classInfo, "获取更多信息失败：" + e.getMessage(), userName);
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        /**
         * 跳转更多关键字搜索界面
         *
         * @param view
         */
        if (null != searchMore)
        {
            searchMore.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //searchMore.setText("筛选");
                    Intent it = new Intent(getActivity(), ActivitySearchMoreFloat.class);
                    startActivityForResult(it, 100);
                    //searchMore.setVisibility(View.GONE);
                    //searchReset.setVisibility(View.VISIBLE);
                }
            });
        }

        /**
         * 重置按钮
         *
         * @param view
         */
        if (null != searchReset)
        {
            searchReset.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    startTime = null;
                    endTime = null;
                    briefly = null;
                    conformJingqingXingzhi = null;
                    alarmCompany = null;
                    chooseIdNo = null;
                    informantPhoneChoose = null;
                    searchReset.setVisibility(View.GONE);
                    searchMore.setVisibility(View.VISIBLE);

                    loadFirstTime();
                }
            });
        }

        // 设置搜索文本监听
        sv_wd.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                briefly = query;
                loadFirstTime();
                //Toast.makeText(getActivity(), "您的选择是:" + query, Toast.LENGTH_LONG).show();
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText)
            {
                if (StringUtils.isBlank(newText))
                {
                    briefly = newText;
                    loadFirstTime();
                }

                return false;
            }
        });


    }


}
