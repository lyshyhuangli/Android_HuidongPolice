package com.huizhou.huidongpolice.utils;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/9/13.
 */

public class FileDownThread implements Runnable
{

    private String serverpath;
    private String savedfilepath;
    public int flags;

    public void setParam(String serverpath, String savedfilepath, int flags)
    {
        this.serverpath = serverpath;
        this.savedfilepath = savedfilepath;
        this.flags = flags;
    }

    public void run()
    {
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
                CommonCache.dataCache.put(flags, true);
                return;
            }
            else
            {
                CommonCache.dataCache.put(flags, false);
                return;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            CommonCache.dataCache.put(flags, false);
            return;
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

    }
}
