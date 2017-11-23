package com.huizhou.huidongpolice.response;


import com.huizhou.huidongpolice.database.vo.JingQingInfoRecord;

public class GetJingQingInfoByIdResp extends CommonResult
{
    private JingQingInfoRecord info;

    public JingQingInfoRecord getInfo()
    {
        return info;
    }

    public void setInfo(JingQingInfoRecord info)
    {
        this.info = info;
    }


}
