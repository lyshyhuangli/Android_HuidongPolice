package com.huizhou.huidongpolice.response;


import com.huizhou.huidongpolice.database.vo.JingQingInfoRecord;

import java.util.LinkedList;
import java.util.List;

public class GetJingQingMoreInfoByCountResp extends CommonResult
{
    List<JingQingInfoRecord> infList = new LinkedList<JingQingInfoRecord>();

    public List<JingQingInfoRecord> getInfList()
    {
        return infList;
    }

    public void setInfList(List<JingQingInfoRecord> infList)
    {
        this.infList = infList;
    }



}
