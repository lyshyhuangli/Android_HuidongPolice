package com.huizhou.huidongpolice.Request;

public class GetStaticFilsInfoByCountReq extends CommonRequest
{
    private int count;
    private String fileType;
    private String fileName;

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public String getFileType()
    {
        return fileType;
    }

    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

}
