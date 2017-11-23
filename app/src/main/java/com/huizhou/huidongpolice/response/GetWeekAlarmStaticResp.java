package com.huizhou.huidongpolice.response;



import com.huizhou.huidongpolice.utils.AnQingRecord;

import java.util.LinkedList;
import java.util.List;

public class GetWeekAlarmStaticResp extends CommonResult
{
    private List<List<AnQingRecord>> infoList = new LinkedList<List<AnQingRecord>>();

    public List<List<AnQingRecord>> getInfoList()
    {
        return infoList;
    }

    public void setInfoList(List<List<AnQingRecord>> infoList)
    {
        this.infoList = infoList;
    }

}
