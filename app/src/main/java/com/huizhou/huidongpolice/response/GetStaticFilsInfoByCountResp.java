package com.huizhou.huidongpolice.response;


import com.huizhou.huidongpolice.database.vo.StaticFilesRecord;


import java.util.LinkedList;
import java.util.List;

public class GetStaticFilsInfoByCountResp extends CommonResult
{
    List<StaticFilesRecord> fileList = new LinkedList<StaticFilesRecord>();

    public List<StaticFilesRecord> getFileList()
    {
        return fileList;
    }

    public void setFileList(List<StaticFilesRecord> fileList)
    {
        this.fileList = fileList;
    }


}
