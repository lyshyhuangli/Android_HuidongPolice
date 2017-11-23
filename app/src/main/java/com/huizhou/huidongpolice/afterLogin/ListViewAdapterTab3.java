package com.huizhou.huidongpolice.afterLogin;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhou.huidongpolice.R;
import com.huizhou.huidongpolice.utils.StaticNumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/22.
 */

public class ListViewAdapterTab3 extends BaseAdapter
{

    private Map<Integer, String> mapFileNames = new HashMap<Integer, String>();
    private List<Integer> listIds = new ArrayList<Integer>(StaticNumber.ONE_HUNDRED);
    private Map<Integer, String> mapUrls = new HashMap<Integer, String>();
    private Map<Integer, String> fileTimes = new HashMap<Integer, String>();
    private String queryStaticFileName = null;


    private LayoutInflater inflater;

    public ListViewAdapterTab3()
    {
    }

    public ListViewAdapterTab3(
            Context context, Map<Integer, String> mapFileNames, List<Integer> listIds,
            Map<Integer, String> mapUrls, Map<Integer, String> fileTime, String queryStaticFileName
    )
    {
        this.mapFileNames = mapFileNames;
        this.inflater = LayoutInflater.from(context);
        this.listIds = listIds;
        this.mapUrls = mapUrls;
        this.fileTimes = fileTime;
        this.queryStaticFileName = queryStaticFileName;
        this.notifyDataSetChanged();
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
            Map<Integer, String> mapFileNames, List<Integer> listIds,
            Map<Integer, String> mapUrls, Map<Integer, String> fileTime, String staticFileName
    )
    {
        this.mapFileNames = mapFileNames;
        this.listIds = listIds;
        this.mapUrls = mapUrls;
        this.fileTimes = fileTime;
        this.queryStaticFileName = staticFileName;
        this.notifyDataSetChanged();//强制动态刷新数据进而调用getView方法
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = null;
        try
        {
            ListViewAdapterTab3.ViewHolder holder = null;
            if (convertView == null)
            {
                view = inflater.inflate(R.layout.all_data_news_item_tab3, null);
                holder = new ListViewAdapterTab3.ViewHolder();
                holder.id = (TextView) view.findViewById(R.id.idStaticFile);
                holder.staticFilesName = (TextView) view.findViewById(R.id.staticFilesName);
                holder.urls = (TextView) view.findViewById(R.id.staticFilsUrl);
                holder.fileType = (ImageView) view.findViewById(R.id.fileType);
                holder.fileTime = (TextView) view.findViewById(R.id.fileTime);
                view.setTag(holder);
            }
            else
            {
                view = convertView;
                holder = (ListViewAdapterTab3.ViewHolder) view.getTag();
            }

            int index = listIds.get(position);
            holder.id.setText(String.valueOf(index));
            holder.urls.setText(mapUrls.get(index));
            String fileName = mapFileNames.get(index);

            holder.fileTime.setText(fileTimes.get(index));

            if (fileName.endsWith("docx") || fileName.endsWith("doc"))
            {
                holder.fileType.setImageResource(R.mipmap.word);
            }
            else if (fileName.endsWith("txt"))
            {
                holder.fileType.setImageResource(R.mipmap.text);
            }
            else if (fileName.endsWith("xlsx") || fileName.endsWith("xls"))
            {
                holder.fileType.setImageResource(R.mipmap.excel);
            }
            else
            {
                holder.fileType.setImageResource(R.mipmap.other);
            }

            if (null != queryStaticFileName)
            {
                SpannableString sp = new SpannableString(fileName);


                if (fileName.contains(queryStaticFileName))
                {
                    int size = queryStaticFileName.length();
                    int indexs = fileName.indexOf(queryStaticFileName);
                    sp.setSpan(new ForegroundColorSpan(Color.RED), indexs, indexs + size, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }

                holder.staticFilesName.setText(sp);
            }
            else
            {
                holder.staticFilesName.setText(fileName);
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return view;
    }

    static public class ViewHolder
    {
        TextView urls;
        TextView staticFilesName;
        TextView id;
        TextView fileTime;
        ImageView fileType;
    }

}
