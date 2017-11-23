package com.huizhou.huidongpolice.Request;

public class GetJingQingMoreInfoByCountReq extends CommonRequest
{
    private int count;
    private String startTime;
    private String endTime;
    private String briefly;
    private String conformJingqingXingzhi;
    private String alarmCompany;
    private String chooseIdNo;
    private String informantPhoneChoose;

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public String getBriefly()
    {
        return briefly;
    }

    public void setBriefly(String briefly)
    {
        this.briefly = briefly;
    }

    public String getConformJingqingXingzhi()
    {
        return conformJingqingXingzhi;
    }

    public void setConformJingqingXingzhi(String conformJingqingXingzhi)
    {
        this.conformJingqingXingzhi = conformJingqingXingzhi;
    }

    public String getAlarmCompany()
    {
        return alarmCompany;
    }

    public void setAlarmCompany(String alarmCompany)
    {
        this.alarmCompany = alarmCompany;
    }

    public String getChooseIdNo()
    {
        return chooseIdNo;
    }

    public void setChooseIdNo(String chooseIdNo)
    {
        this.chooseIdNo = chooseIdNo;
    }

    public String getInformantPhoneChoose()
    {
        return informantPhoneChoose;
    }

    public void setInformantPhoneChoose(String informantPhoneChoose)
    {
        this.informantPhoneChoose = informantPhoneChoose;
    }


}
