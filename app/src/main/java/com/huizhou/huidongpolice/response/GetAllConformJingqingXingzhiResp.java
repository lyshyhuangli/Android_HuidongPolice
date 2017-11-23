package com.huizhou.huidongpolice.response;

import java.util.LinkedList;
import java.util.List;

public class GetAllConformJingqingXingzhiResp extends CommonResult
{
    private List<String> infoLit = new LinkedList<String>();

    public List<String> getInfoLit()
    {
        return infoLit;
    }

    public void setInfoLit(List<String> infoLit)
    {
        this.infoLit = infoLit;
    }


}
