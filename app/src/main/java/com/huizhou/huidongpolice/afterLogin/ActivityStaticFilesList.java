package com.huizhou.huidongpolice.afterLogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huizhou.huidongpolice.R;
import com.huizhou.huidongpolice.Request.CheckUserByUserAndPwdReq;
import com.huizhou.huidongpolice.Request.GetStaticFilsInfoByCountReq;
import com.huizhou.huidongpolice.database.vo.StaticFilesRecord;
import com.huizhou.huidongpolice.response.CheckUserByUserAndPwdResp;
import com.huizhou.huidongpolice.utils.CommonCache;
import com.huizhou.huidongpolice.utils.CommonUtils;
import com.huizhou.huidongpolice.utils.FileDownThread;
import com.huizhou.huidongpolice.utils.Flags;
import com.huizhou.huidongpolice.utils.HttpClientClass;
import com.huizhou.huidongpolice.utils.Log4J;
import com.huizhou.huidongpolice.utils.ProcessDataBaseThread;
import com.huizhou.huidongpolice.utils.ServerConfig;
import com.huizhou.huidongpolice.utils.StaticNumber;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class ActivityStaticFilesList extends AppCompatActivity implements AbsListView.OnScrollListener
{

    //private View loadmoreView;
    private LayoutInflater inflater;
    private ListView listView;
    private int last_index;
    private int total_index;
    private ProgressBar progressBar;

    private TextView tv;

    //数据保存Map
    private Map<Integer, String> mapFileNames = new HashMap<Integer, String>();
    private List<Integer> listIds = new ArrayList<Integer>(StaticNumber.ONE_HUNDRED);
    private Map<Integer, String> mapUrls = new HashMap<Integer, String>();
    private Map<Integer, String> fileTime = new HashMap<Integer, String>();

    private boolean isLoading = false;//表示是否正处于加载状态
    private ListViewAdapterTab3 adapter;

    private int getDataNo = 1;
    private String staticFileName;
    private String staticFileType;

    private SearchView searchStaticFilesView;

    private String name;
    private String classInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_files_list);

        progressBar = (ProgressBar) findViewById(R.id.progressFile);

        //获取文件夹权限
        Log4J.verifyStoragePermissions(this);

        SharedPreferences userSettings = getApplicationContext().getSharedPreferences("userInfo", 0);
        name = userSettings.getString("loginUserName", "default");

        classInfo = CommonUtils.getFileLineMethod(new Exception());

        Intent it = getIntent();
        staticFileType = it.getStringExtra("fileType");

        loadFirstTime();

        //hideKeyboard();

        //移除搜索焦点
        searchStaticFilesView = (SearchView) this.findViewById(R.id.searchStaticFiles);
        searchStaticFilesView.setFocusable(false);
        searchStaticFilesView.setFocusableInTouchMode(false);
        searchStaticFilesView.clearFocus();

        if (null != searchStaticFilesView)
        {
            int id = searchStaticFilesView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView textView = (TextView) searchStaticFilesView.findViewById(id);
            textView.setTextSize(15);//字体、提示字体大小
        }

        tv = (TextView) findViewById(R.id.processTv);

        // 设置搜索文本监听
        searchStaticFilesView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                staticFileName = query;
                loadFirstTime();
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText)
            {
                if (StringUtils.isBlank(newText))
                {
                    staticFileName = newText;
                    loadFirstTime();
                }
                return false;
            }
        });
    }

    public void tobackStatic(View view)
    {
        super.onBackPressed();
    }

    /**
     * 第一次加载数据
     */
    private void loadFirstTime()
    {
        inflater = null;
        //loadmoreView = null;
        listView = null;
        mapFileNames.clear();
        listIds.clear();
        mapUrls.clear();
        fileTime.clear();
        getDataNo = 1;

        inflater = LayoutInflater.from(this);
        listView = (ListView) this.findViewById(R.id.staticFilesLv);

        listView.setOnScrollListener(this);

        //初始化一次数据
        getMoreData(staticFileType, staticFileName);
        adapter = new ListViewAdapterTab3(this, mapFileNames, listIds,
                mapUrls, fileTime, staticFileName
        );

        listView.setAdapter(adapter);

        //条目点击事件
        listView.setOnItemClickListener(new ActivityStaticFilesList.ItemClickListener());
    }

    public boolean delAllFile(String path)
    {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists())
        {
            return flag;
        }
        if (!file.isDirectory())
        {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++)
        {
            if (path.endsWith(File.separator))
            {
                temp = new File(path + tempList[i]);
            }
            else
            {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile())
            {
                temp.delete();
            }
            if (temp.isDirectory())
            {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                flag = true;
            }
        }
        return flag;
    }

    //获取点击事件
    private final class ItemClickListener implements AdapterView.OnItemClickListener
    {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            //移除搜索焦点
            searchStaticFilesView.setFocusable(false);
            searchStaticFilesView.setFocusableInTouchMode(false);
            searchStaticFilesView.clearFocus();

            //创建文件夹
            String dirStr = ServerConfig.FILE_SAVE_PATH;
            File dir = new File(dirStr);
            if (!dir.exists())
            {
                if (dir.mkdirs())
                {
                    CommonUtils.sendLogsToServer(classInfo, "创建目录成功！", name);
                }
                else
                {
                    CommonUtils.sendLogsToServer(classInfo, "创建目录失败！", name);
                }
            }

            ListViewAdapterTab3.ViewHolder holder = (ListViewAdapterTab3.ViewHolder) view.getTag();
            String url = holder.urls.getText().toString();
            String fileName = holder.staticFilesName.getText().toString();
            String temp = url.replace("\\", "/");
            String urlStr = "http://" + ServerConfig.SERVER_IP + ":" + ServerConfig.SERVER_PORT + "/huidongpolice/staticFiles" + temp;

            try
            {
                String xmString = new String(urlStr.getBytes("UTF-8"));
                MyTask m = new MyTask();
                m.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,xmString, dirStr + fileName);
                //m.execute(xmString, dirStr + fileName);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


        }
    }


    /**
     * 滑动获取更多数据
     */
    private void getMoreData(String staticFileType, String staticFileName)
    {
        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        String userName = userSettings.getString("loginUserName", "default");
        String classInfo = CommonUtils.getFileLineMethod(new Exception());

        //获取到集合数据
        ProcessDataBaseThread pd = new ProcessDataBaseThread();
        pd.setFlag(Flags.SEARCH_STATIC_FILES);

        GetStaticFilsInfoByCountReq req = new GetStaticFilsInfoByCountReq();
        req.setCount(getDataNo);
        req.setFileName(staticFileName);
        req.setFileType(staticFileType);
        req.setOperatorId(userName);
        pd.setObject(req);

        Thread t = new Thread(pd);
        t.start();
        try
        {
            for (int i = 0; i <= StaticNumber.TWO_ZERO; i++)
            {
                Thread.sleep(StaticNumber.FIVE_HUNDRED);

                Object o = CommonCache.dataCache.get(Flags.SEARCH_STATIC_FILES);
                if (null != o)
                {
                    List<StaticFilesRecord> fileList = (List<StaticFilesRecord>) o;
                    for (StaticFilesRecord info : fileList)
                    {
                        listIds.add(info.getId());
                        mapFileNames.put(info.getId(), info.getFileName());
                        mapUrls.put(info.getId(), info.getUrl());
                        fileTime.put(info.getId(), info.getModifyTime());
                    }

                    CommonCache.dataCache.remove(Flags.SEARCH_STATIC_FILES);
                    break;
                }
                else
                {
                    if (i == StaticNumber.TEN)
                    {
                        Thread t2 = new Thread(pd);
                        t2.start();

                        Thread.sleep(StaticNumber.FIVE_HUNDRED);
                        o = CommonCache.dataCache.get(Flags.SEARCH_STATIC_FILES);
                        if (null != o)
                        {
                            List<StaticFilesRecord> fileList = (List<StaticFilesRecord>) o;
                            for (StaticFilesRecord info : fileList)
                            {
                                listIds.add(info.getId());
                                mapFileNames.put(info.getId(), info.getFileName());
                                mapUrls.put(info.getId(), info.getUrl());
                                fileTime.put(info.getId(), info.getModifyTime());
                            }

                            CommonCache.dataCache.remove(Flags.SEARCH_STATIC_FILES);
                            break;
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log4J.getLogger(this.getClass()).error("查询最新警讯失败：" + e.getMessage());
            CommonUtils.sendLogsToServer(classInfo, "查询失败：" + e.getMessage(), userName);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        last_index = firstVisibleItem + visibleItemCount;
        total_index = totalItemCount;
        System.out.println("last:  " + last_index);
        System.out.println("total:  " + total_index);
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
                    //listView.addFooterView(loadmoreView, null, false);
                    onLoad();
                }
            }
        }
        catch (Exception e)
        {
            CommonUtils.sendLogsToServer(classInfo, "获取更多信息失败：" + e.getMessage(), name);
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
                adapter = new ListViewAdapterTab3(this, mapFileNames, listIds,
                        mapUrls, fileTime, staticFileName
                );
                listView.setAdapter(adapter);
            }
            else
            {
                //获取更多数据
                mapFileNames.clear();
                listIds.clear();
                getMoreData(staticFileType, staticFileName);
                adapter.updateView(mapFileNames, listIds, mapUrls, fileTime, staticFileName
                );
            }
            loadComplete();//刷新结束

        }
        catch (Exception e)
        {
            CommonUtils.sendLogsToServer(classInfo, "获取更多信息失败：" + e.getMessage(), name);
        }
    }

    /**
     * 加载完成
     */
    public void loadComplete()
    {
        //loadmoreView.setVisibility(View.GONE);//设置刷新界面不可见
        isLoading = false;//设置正在刷新标志位false
        this.invalidateOptionsMenu();
        //listView.removeFooterView(loadmoreView);//如果是最后一页的话，则将其从ListView中移出
    }

    @Override
    public void onResume()
    {
        super.onResume();
        searchStaticFilesView.setFocusable(false);
        searchStaticFilesView.setFocusableInTouchMode(false);
        searchStaticFilesView.clearFocus();
        searchStaticFilesView.requestFocus();
    }

    private void openFile(File file)
    {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type = getMIMEType(file);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {

            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, "com.huizhou.huidongpolice.fileProvider", file);
            intent.setDataAndType(contentUri, type);
        }
        else
        {
            intent.setDataAndType(Uri.fromFile(file), type);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    private String getMIMEType(File file)
    {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0)
        {
            return type;
        }
        /* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++)
        { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    private final String[][] MIME_MapTable = {
            //{后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/html"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    private class MyTask extends AsyncTask<String, Integer, String>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);

            Toast tos = Toast.makeText(getApplicationContext(), "正在读取文件，请稍等 1-2分钟.....", Toast.LENGTH_LONG);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected String doInBackground(String... params)
        {
            String serverpath = params[0];
            String savedfilepath = params[1];
            FileOutputStream fos = null;
            InputStream is = null;
            try
            {
                URL url = new URL(serverpath);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(30000);

                if (conn.getResponseCode() == 200)
                {
                    int max = conn.getContentLength();
                    is = conn.getInputStream();
                    File file = new File(savedfilepath);
                    if (file.exists())
                    {
                        file.delete();
                        file = new File(savedfilepath);
                    }

                    fos = new FileOutputStream(file);
                    int len = 0;
                    byte[] buffer = new byte[1024];
                    int total = 0;
                    while ((len = is.read(buffer)) != -1)
                    {
                        fos.write(buffer, 0, len);
                        total += len;
                    }

                    fos.flush();

                    return savedfilepath;
                }
                else
                {
                    return null;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (is != null)
                {
                    try
                    {
                        is.close();
                    }
                    catch (Exception e)
                    {

                    }
                }

                if (fos != null)
                {
                    try
                    {
                        fos.close();
                    }
                    catch (Exception e)
                    {

                    }
                }
            }

            return null;
        }

        //onProgressUpdate方法用于更新进度信息
        @Override
        protected void onProgressUpdate(Integer... progresses)
        {

        }

        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(String result)
        {
            if (StringUtils.isNotBlank(result))
            {
                try
                {
                    openFile(new File(result));
                    progressBar.setVisibility(View.GONE);
                }
                catch (Exception e)
                {
                    progressBar.setVisibility(View.GONE);
                    CommonUtils.sendLogsToServer(classInfo, "读取文件失败：" + e.getMessage(), name);
                    e.printStackTrace();
                }
            }
            else
            {
                Toast tos = Toast.makeText(getApplicationContext(), "读取文件失败.", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
            }


        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }

}
