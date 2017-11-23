package com.huizhou.huidongpolice.afterLogin;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huizhou.huidongpolice.R;
import com.huizhou.huidongpolice.utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/10.
 */

public class ListViewAdapterTab2 extends BaseAdapter
{

    private List<Integer> listIds = new ArrayList<Integer>();
    private Map<Integer, String> mapBriefly = new HashMap<Integer, String>();
    private Map<Integer, String> mapComformXingZhiTab2 = new HashMap<Integer, String>();
    private Map<Integer, String> mapAlarmCompanyTab2 = new HashMap<Integer, String>();
    private Map<Integer, String> mapAlarmDateTab2 = new HashMap<Integer, String>();
    private List<String> briefQery = new ArrayList<String>();

    private LayoutInflater inflater;

    public ListViewAdapterTab2()
    {
    }

    public ListViewAdapterTab2(
            Context context, Map<Integer, String> mapBriefly, List<Integer> listIds,
            Map<Integer, String> mapComformXingZhiTab2, Map<Integer, String> mapAlarmCompanyTab2,
            Map<Integer, String> mapAlarmDateTab2, List<String> briefQery
    )
    {
        this.mapBriefly = mapBriefly;
        this.inflater = LayoutInflater.from(context);
        this.listIds = listIds;
        this.mapComformXingZhiTab2 = mapComformXingZhiTab2;
        this.mapAlarmCompanyTab2 = mapAlarmCompanyTab2;
        this.mapAlarmDateTab2 = mapAlarmDateTab2;
        this.briefQery = briefQery;
        //this.notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return listIds.size();
    }

    @Override
    public Integer getItem(int position)
    {
        return listIds.get(position);
    }


    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    public void updateView(
            Map<Integer, String> mapBriefly, List<Integer> listIds, Map<Integer,
            String> mapComformXingZhiTab2, Map<Integer, String> mapAlarmCompanyTab2,
            Map<Integer, String> mapAlarmDateTab2, List<String> briefQery, ListView listView
    )
    {
        try
        {
            this.mapBriefly = mapBriefly;
            this.listIds = listIds;
            this.mapComformXingZhiTab2 = mapComformXingZhiTab2;
            this.mapAlarmCompanyTab2 = mapAlarmCompanyTab2;
            this.mapAlarmDateTab2 = mapAlarmDateTab2;
            this.briefQery = briefQery;
            this.notifyDataSetChanged();//强制动态刷新数据进而调用getView方法
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = null;
        try
        {
            ViewHolder holder = null;
            if (convertView == null)
            {
                view = inflater.inflate(R.layout.all_data_news_item_tab2, null);
                holder = new ViewHolder();
                holder.id = (TextView) view.findViewById(R.id.idData);
                holder.comformXingZhiTab2 = (TextView) view.findViewById(R.id.comformXingZhiTab2);
                holder.brieflyTab2 = (TextView) view.findViewById(R.id.brieflyTab2);
                //holder.alarmCompanyTab2 = (TextView) view.findViewById(R.id.alarmCompanyTab2);
                holder.alarmDateTab2 = (TextView) view.findViewById(R.id.alarmDateTab2);
                holder.brieflyType = (ImageView)  view.findViewById(R.id.brieflyType);
                view.setTag(holder);
            }
            else
            {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }

            int index = listIds.get(position);
            holder.id.setText(String.valueOf(index));
            String temp = mapComformXingZhiTab2.get(index);
            holder.comformXingZhiTab2.setText(temp);

            holder.brieflyType.setImageResource(getXingzhi(temp));

            //holder.alarmCompanyTab2.setText(mapAlarmCompanyTab2.get(index));

            holder.alarmDateTab2.setText(mapAlarmDateTab2.get(index));
            String brief = mapBriefly.get(index);

            if (!briefQery.isEmpty())
            {
                SpannableString sp = new SpannableString(brief);
                for (String s : briefQery)
                {
                    if (brief.contains(s))
                    {
                        int size = s.length();
                        int indexs = brief.indexOf(s);
                        sp.setSpan(new ForegroundColorSpan(Color.RED), indexs, indexs + size, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    }
                }

                holder.brieflyTab2.setText(sp);
            }
            else
            {
                holder.brieflyTab2.setText(mapBriefly.get(index));
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return view;
    }

    static private int getXingzhi(String str)
    {
        if (str.contains("盗"))
        {
            return R.mipmap.dao;
        }
        else if (str.contains("毒"))
        {
            return R.mipmap.du;
        }

        else if (str.contains("赌"))
        {
            return R.mipmap.du2;
        }

        else if (str.contains("夺"))
        {
            return R.mipmap.duo;
        }
        else if (str.contains("黄"))
        {
            return R.mipmap.huang;
        }
        else if (str.contains("奸"))
        {
            return R.mipmap.jian;
        }
        else if (str.contains("劫"))
        {
            return R.mipmap.jie;
        }
        else if (str.contains("扒"))
        {
            return R.mipmap.pa;
        }
        else if (str.contains("抢"))
        {
            return R.mipmap.qiang;
        }
        else if (str.contains("入"))
        {
            return R.mipmap.ru;
        }
        else if (str.contains("杀"))
        {
            return R.mipmap.sha;
        }
        else if (str.contains("伤"))
        {
            return R.mipmap.shang;
        }
        else if (str.contains("诈"))
        {
            return R.mipmap.zha;
        }
        else
        {
            return R.mipmap.qita;
        }
    }

    static public class ViewHolder
    {
        TextView comformXingZhiTab2;
        TextView brieflyTab2;
        //TextView alarmCompanyTab2;
        TextView alarmDateTab2;
        TextView id;
        ImageView brieflyType;
    }
}
