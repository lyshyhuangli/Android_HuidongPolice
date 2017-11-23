package com.huizhou.huidongpolice.response;

import java.util.LinkedList;
import java.util.List;

public class GetAllAlarmCompanyResp extends CommonResult
{
    private List<String> infoList = new LinkedList<String>();

    public List<String> getInfoList()
    {
        return infoList;
    }

    public void setInfoList(List<String> infoList)
    {
        this.infoList = infoList;
    }

}
