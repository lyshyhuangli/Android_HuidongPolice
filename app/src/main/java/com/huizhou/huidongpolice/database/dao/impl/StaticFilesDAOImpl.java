package com.huizhou.huidongpolice.database.dao.impl;

import com.huizhou.huidongpolice.database.DbConnect;
import com.huizhou.huidongpolice.database.dao.StaticFilesDAO;
import com.huizhou.huidongpolice.database.vo.JingQingInfoRecord;
import com.huizhou.huidongpolice.database.vo.StaticFilesRecord;
import com.huizhou.huidongpolice.utils.Log4J;
import com.huizhou.huidongpolice.utils.StaticNumber;

import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */

public class StaticFilesDAOImpl implements StaticFilesDAO
{
    /**
     * 滑动获取数据
     *
     * @param count
     * @return
     */
    public List<StaticFilesRecord> getStaticFilsInfoByCount(int count, String fileType, String fileName)
    {
        Connection conn = DbConnect.getConnection();
        StringBuilder strsql = new StringBuilder("select * from tb_staticFiles where 1=1 ");
        List<String> params = new ArrayList<String>();

        if (StringUtils.isNotBlank(fileName))
        {
            strsql.append(" and fileName like  ? ");
            params.add("%" + fileName.trim() + "%");
        }


        if (StringUtils.isNotBlank(fileType))
        {
            strsql.append(" and fileType =  ? ");
            params.add(fileType.trim());
        }

        strsql.append(" order by modifyTime DESC LIMIT " + count * StaticNumber.THREE_FIVE);

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<StaticFilesRecord> infoList = new LinkedList<StaticFilesRecord>();
        try
        {
            pstmt = conn.prepareStatement(strsql.toString());

            for (int i = 0; i < params.size(); i++)
            {
                pstmt.setString(i + 1, params.get(i));
            }

            rs = pstmt.executeQuery();

            while (rs.next())
            {
                String fileNameDb = rs.getString("fileName").trim();
                int id = rs.getInt("id");
                StaticFilesRecord info = new StaticFilesRecord();
                info.setId(id);
                info.setFileName(fileNameDb);
                info.setFiletype(rs.getString("fileType"));
                info.setModifyTime(rs.getString("modifyTime"));
                info.setUrl(rs.getString("url"));
                infoList.add(info);
            }
        }
        catch (Exception e)
        {
            Log4J.getLogger(this.getClass()).error("获取更多统计文件信息失败：" + e.getMessage());
        }
        finally
        {
            try
            {
                if (null != rs)
                {
                    rs.close();
                }
                if (null != pstmt)
                {
                    pstmt.close();
                }
            }
            catch (Exception e)
            {
                Log4J.getLogger(this.getClass()).error("获取更多统计文件信息失败：" + e.getMessage());
            }

        }
        return infoList;

    }
}
