package com.huizhou.huidongpolice.database.dao;

import com.huizhou.huidongpolice.database.vo.JingQingInfoRecord;
import com.huizhou.huidongpolice.utils.AnQingRecord;

import java.util.List;

/**
 * Created by Administrator on 2017/8/8.
 */

public interface JingQingInfoDAO
{
    public abstract List<JingQingInfoRecord> getLastFiveNumberJingQing();

    public abstract JingQingInfoRecord getJingQingInfoById(String id);

    /**
     * 滑动获取数据
     *
     * @param count
     * @return
     */
    public abstract List<JingQingInfoRecord> getJingQingMoreInfoByCount(
            int count, String startTime, String endTime,
            String briefly, String conformJingqingXingzhi,
            String alarmCompany, String chooseIdNo,
            String informantPhoneChoose
    );

    /**
     * 获取所有的确认警情性质
     *
     * @return
     */
    public abstract List<String> getAllConformJingqingXingzhi();

    /**
     * 获取所有的  派出所
     *
     * @return
     */
    public abstract List<String> getAllAlarmCompany();

    /**
     * 一周警情统计
     *
     * @return
     */
    public abstract List<List<AnQingRecord>> getWeekAlarmStatic();

}
