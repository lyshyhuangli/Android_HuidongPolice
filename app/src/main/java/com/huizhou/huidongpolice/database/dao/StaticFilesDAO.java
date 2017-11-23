package com.huizhou.huidongpolice.database.dao;

import com.huizhou.huidongpolice.database.vo.JingQingInfoRecord;
import com.huizhou.huidongpolice.database.vo.StaticFilesRecord;

import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */

public interface StaticFilesDAO
{
    /**
     * 滑动获取数据
     *
     * @param count
     * @return
     */
    public abstract List<StaticFilesRecord> getStaticFilsInfoByCount(int count, String fileType,String fileName);

}
