package com.huizhou.huidongpolice.response;


import com.huizhou.huidongpolice.database.vo.JingQingInfoRecord;


import java.util.LinkedList;
import java.util.List;

public class GetLastSevenNumberJingQingResp extends CommonResult
{
    private List<JingQingInfoRecord> infoList = new LinkedList<JingQingInfoRecord>();

    public List<JingQingInfoRecord> getInfoList()
    {
        return infoList;
    }

    public void setInfoList(List<JingQingInfoRecord> infoList)
    {
        this.infoList = infoList;
    }


}
