package com.huizhou.huidongpolice.utils;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.signature.StringSignature;
import com.youth.banner.loader.ImageLoader;

import java.io.File;
import java.util.UUID;


public class GlideImageLoader extends ImageLoader
{
    @Override
    public void displayImage(Context context, Object path, ImageView imageView)
    {
       // clearImageDiskCache(context);
        //clearImageMemoryCache(context);
       // clearImageAllCache( context);
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择

        Glide
                .with( context )
                .load(path )
                .signature(new StringSignature(UUID.randomUUID().toString()))
                .into( imageView );
    }

    /**
     * 清除图片磁盘缓存
     */
    public void clearImageDiskCache(final Context context)
    {
        try
        {
            if (Looper.myLooper() == Looper.getMainLooper())
            {
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Glide.get(context).clearDiskCache();
//                        BusUtil.getBus().post(new GlideCacheClearSuccessEvent());
                    }
                }).start();
            }
            else
            {
                Glide.get(context).clearDiskCache();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     */
    public void clearImageMemoryCache(Context context)
    {
        try
        {
            if (Looper.myLooper() == Looper.getMainLooper())
            { //只能在主线程执行
                Glide.get(context).clearMemory();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片所有缓存
     */
    public void clearImageAllCache(Context context)
    {
        clearImageDiskCache(context);
        clearImageMemoryCache(context);
        String ImageExternalCatchDir = context.getExternalCacheDir() + ExternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR;
        deleteFolderFile(ImageExternalCatchDir, true);
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath       filePath
     * @param deleteThisPath deleteThisPath
     */
    private void deleteFolderFile(String filePath, boolean deleteThisPath)
    {
        if (!TextUtils.isEmpty(filePath))
        {
            try
            {
                File file = new File(filePath);
                if (file.isDirectory())
                {
                    File files[] = file.listFiles();
                    for (File file1 : files)
                    {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath)
                {
                    if (!file.isDirectory())
                    {
                        file.delete();
                    }
                    else
                    {
                        if (file.listFiles().length == 0)
                        {
                            file.delete();
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ImageView createImageView(Context context) {
        //圆角
        //return new RoundAngleImageView(context);
        return  new ImageView(context);
    }
}
