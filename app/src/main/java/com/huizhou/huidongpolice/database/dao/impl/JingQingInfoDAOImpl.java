package com.huizhou.huidongpolice.database.dao.impl;


import com.huizhou.huidongpolice.database.DbConnect;
import com.huizhou.huidongpolice.database.dao.JingQingInfoDAO;
import com.huizhou.huidongpolice.database.vo.JingQingInfoRecord;
import com.huizhou.huidongpolice.utils.AnQingRecord;
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
 * Created by Administrator on 2017/8/8.
 */

public class JingQingInfoDAOImpl implements JingQingInfoDAO
{
    /**
     * 查询最新的5条警情
     *
     * @return
     */
    public List<JingQingInfoRecord> getLastFiveNumberJingQing()
    {
        Connection conn = DbConnect.getConnection();
        String strsql = "select * from tb_jingqinginfo order by alarm_date DESC LIMIT 7";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<JingQingInfoRecord> infoList = new LinkedList<JingQingInfoRecord>();
        try
        {
            pstmt = conn.prepareStatement(strsql);
            rs = pstmt.executeQuery();

            while (rs.next())
            {
                String briefly = rs.getString("briefly").trim();
                int id = rs.getInt("id");
                JingQingInfoRecord info = new JingQingInfoRecord();
                info.setId(id);
                info.setBriefly(briefly.replaceAll("\r\n", ""));
                info.setComformXingZhi(rs.getString("comformXingZhi"));
                info.setAlarmDate(rs.getString("alarm_date"));
                info.setAlarmCompany(rs.getString("alarm_Company").replaceAll("惠东县公安局", ""));
                infoList.add(info);
            }
        }
        catch (Exception e)
        {
            Log4J.getLogger(this.getClass()).error("获取最新5条警情信息失败：" + e.getMessage());
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
                Log4J.getLogger(this.getClass()).error("获取最新5条警情信息失败：" + e.getMessage());
            }

        }
        return infoList;
    }

    /**
     * 获取所有的  派出所
     *
     * @return
     */
    public List<String> getAllAlarmCompany()
    {
        String sql = "select alarm_company FROM tb_jingqinginfo GROUP BY alarm_company";
        Connection conn = DbConnect.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<String> infoList = new LinkedList<String>();
        try
        {
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next())
            {
                String alarmCompany = rs.getString("alarm_company").trim();
                if (alarmCompany.contains("已撤销"))
                {
                    continue;
                }
                infoList.add(alarmCompany);
            }
        }
        catch (Exception e)
        {
            Log4J.getLogger(this.getClass()).error("获取所有的派出所失败：" + e.getMessage());
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
                Log4J.getLogger(this.getClass()).error("获取所有的派出所失败：" + e.getMessage());
            }

        }
        return infoList;
    }

    /**
     * 一周警情统计
     *
     * @return
     */
    public List<List<AnQingRecord>> getWeekAlarmStatic()
    {
        String sql = "select count(*) as number, alarm_Company from tb_jingqinginfo" +
                " where alarm_date BETWEEN CONCAT(date_format(date_sub(date(NOW()), interval 7770 day),'%Y-%m-%d') ,' 00:00:00' ) " +
                " and CONCAT(date_format(date(NOW()),'%Y-%m-%d') ,' 23:59:59')  " +
                " GROUP BY alarm_Company ORDER BY number DESC;";

        Connection conn = DbConnect.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<List<AnQingRecord>> infoList = new ArrayList<List<AnQingRecord>>();
        List<AnQingRecord> companyList1 = new ArrayList<AnQingRecord>();
        List<AnQingRecord> companyList2 = new ArrayList<AnQingRecord>();
        List<AnQingRecord> companyList3 = new ArrayList<AnQingRecord>();
        String type1 = "城南派出所，城西派出所，南湖派出所，蕉田派出所，大岭派出所，吉隆派出所，黄埠派出所";
        String type2 = "白花派出所，太阳坳派出所，稔山派出所,亚婆角派出所，平海派出所，港口派出所，巽寮派出所，梁化派出所，多祝派出所，铁涌派出所";
        String type3 = "增光派出所，白盆朱派出所，新庵派出所，安墩派出所，宝口派出所，高潭派出所，港口边防派出所，平海边防派出所，范和边防派出所，盐洲边防派出所，黄埠边防派出所，巽寮边防派出所，三角洲边防派出所，亚婆边防派出所";

        try
        {
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next())
            {
                String alarmCompany = rs.getString("alarm_company").trim();
                if (alarmCompany.contains("已撤销"))
                {
                    continue;
                }

                String temp = alarmCompany.replace("惠东县公安局", "");
                int number = rs.getInt("number");
                AnQingRecord r = new AnQingRecord();
                r.setAlarm_Company(temp);
                r.setNumber(number);

                if (type1.contains(temp))
                {
                    companyList1.add(r);
                }
                else if (type2.contains(temp))
                {
                    companyList2.add(r);
                }
                else
                {
                    companyList3.add(r);
                }
            }

            infoList.add(companyList1);
            infoList.add(companyList2);
            infoList.add(companyList3);
        }
        catch (Exception e)
        {
            Log4J.getLogger(this.getClass()).error("获取一周警情情况失败：" + e.getMessage());
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
                Log4J.getLogger(this.getClass()).error("获取一周警情情况失败：" + e.getMessage());
            }

        }
        return infoList;
    }

    /**
     * 获取所有的确认警情性质
     *
     * @return
     */
    public List<String> getAllConformJingqingXingzhi()
    {
        String sql = "select comformxingzhi FROM tb_jingqinginfo GROUP BY comformxingzhi";
        Connection conn = DbConnect.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<String> infoList = new LinkedList<String>();
        try
        {
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next())
            {
                String comformxingzhi = rs.getString("comformxingzhi").trim();
                if (comformxingzhi.contains("删除"))
                {
                    continue;
                }
                infoList.add(comformxingzhi);
            }
        }
        catch (Exception e)
        {
            Log4J.getLogger(this.getClass()).error("获取所有的确认警情性质失败：" + e.getMessage());
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
                Log4J.getLogger(this.getClass()).error("获取所有的确认警情性质失败：" + e.getMessage());
            }

        }
        return infoList;
    }

    /**
     * 滑动获取数据,每次获取20条数据
     *
     * @param count
     * @return
     */
    public List<JingQingInfoRecord> getJingQingMoreInfoByCount(
            int count, String startTime, String endTime,
            String briefly, String conformJingqingXingzhi,
            String alarmCompany, String chooseIdNo,
            String informantPhoneChoose
    )
    {
        Connection conn = DbConnect.getConnection();
        StringBuilder strsql = new StringBuilder("select * from tb_jingqinginfo where 1=1 ");
        List<String> params = new ArrayList<String>();

        if (StringUtils.isNotBlank(startTime)
                && StringUtils.isNotBlank(endTime))
        {
            strsql.append(" and alarm_date between ? and ?");
            params.add(startTime + " 00:00");
            params.add(endTime + " 23:59");
        }

        if (StringUtils.isNotBlank(briefly))
        {
            strsql.append(" and briefly like  ? ");
            params.add("%" + briefly.trim() + "%");

            strsql.append(" or placeDetail like  ? ");
            params.add("%" + briefly.trim() + "%");
        }

        if (StringUtils.isNotBlank(conformJingqingXingzhi))
        {
            //String temp = conformJingqingXingzhi.substring(0, conformJingqingXingzhi.lastIndexOf(","));
            StringBuilder sb = new StringBuilder();
            sb.append(conformJingqingXingzhi.replace(",", "','"));

            strsql.append(" and comformxingzhi in (?)");
            params.add(sb.toString());
        }

        if (StringUtils.isNotBlank(alarmCompany))
        {
            //String temp = alarmCompany.substring(0, alarmCompany.lastIndexOf(","));
            StringBuilder sb = new StringBuilder();
            sb.append(alarmCompany.replace(",", "','"));

            strsql.append(" and alarm_Company in (?)");
            params.add(sb.toString());
        }

        if (StringUtils.isNotBlank(chooseIdNo))
        {
            strsql.append(" and idNo like  ? ");
            params.add("%" + chooseIdNo.trim() + "%");
        }

        if (StringUtils.isNotBlank(informantPhoneChoose))
        {
            strsql.append(" and Informant_phone like  ? ");
            params.add("%" + informantPhoneChoose.trim() + "%");
        }

        strsql.append(" order by alarm_date DESC LIMIT " + count * StaticNumber.TWO_ZERO);

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<JingQingInfoRecord> infoList = new LinkedList<JingQingInfoRecord>();
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
                String brieflyDb = rs.getString("briefly").trim();
                int id = rs.getInt("id");
                JingQingInfoRecord info = new JingQingInfoRecord();
                info.setId(id);
                info.setBriefly(brieflyDb.replaceAll("\r\n", ""));
                info.setComformXingZhi(rs.getString("comformXingZhi"));
                info.setAlarmDate(rs.getString("alarm_date"));
                info.setAlarmCompany(rs.getString("alarm_Company").replaceAll("惠东县公安局", ""));
                infoList.add(info);
            }
        }
        catch (Exception e)
        {
            Log4J.getLogger(this.getClass()).error("获取更多警情信息失败：" + e.getMessage());
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
                Log4J.getLogger(this.getClass()).error("获取更多警情信息失败：" + e.getMessage());
            }

        }
        return infoList;

    }


    /**
     * 根据ID查询警情
     *
     * @param id
     * @return
     */
    public JingQingInfoRecord getJingQingInfoById(String id)
    {
        {
            Connection conn = DbConnect.getConnection();
            String strsql = "select * from tb_jingqinginfo where id= " + Integer.valueOf(id);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            JingQingInfoRecord info = new JingQingInfoRecord();
            try
            {
                pstmt = conn.prepareStatement(strsql);
                rs = pstmt.executeQuery();

                while (rs.next())
                {
                    int ids = rs.getInt("id");
                    String comformXingZhi = rs.getString("comformXingZhi").trim();
                    String idNo = rs.getString("idNo").trim();
                    info.setId(ids);
                    info.setIdNo(idNo);
                    info.setComformXingZhi(comformXingZhi);
                    info.setPlace(rs.getString("place").trim());
                    info.setXingZhi(rs.getString("xingZhi").trim());
                    info.setBriefly(rs.getString("briefly").trim());
                    info.setDate1(rs.getString("date1").trim());
                    info.setDate2(rs.getString("date2").trim());
                    info.setPlaceDetail(rs.getString("placeDetail").trim());
                    info.setAlarmMode(rs.getString("alarm_Mode").trim());
                    info.setAlarmCompany(rs.getString("alarm_Company").trim());
                    info.setProcess(rs.getString("process").trim());
                    info.setNumberCaptured(rs.getString("number_Captured").trim());
                    info.setRescuedPopulation(rs.getString("rescued_Population").trim());
                    info.setRescuedChildPopulation(rs.getString("rescued_Child_Population").trim());
                    info.setChuJingPerson(rs.getString("chu_Jing_Number").trim());
                    info.setJingLi(rs.getString("jing_Li").trim());
                    info.setChuanCi(rs.getString("chuan_Ci").trim());
                    info.setHangkongNumber(rs.getString("hangkong_Number").trim());
                    info.setProcessResult(rs.getString("process_Result").trim());
                    info.setLeaderOpinion(rs.getString("leader_Opinion").trim());
                    info.setFillFormDate(rs.getString("fill_Form_Date").trim());
                    info.setJieJingPerson(rs.getString("jieJing_Person").trim());
                    info.setAlarmDate(rs.getString("alarm_Date").trim());
                    info.setPlaceStreet(rs.getString("place_Street").trim());
                    info.setJingQingType(rs.getString("jingqing_type").trim());
                    info.setIsValid(rs.getString("isValid").trim());
                    info.setDiscoverTime(rs.getString("discover_Time").trim());
                    info.setSubordinateCommunity(rs.getString("subordinate_Community").trim());
                    info.setBusNumber(rs.getString("bus_Number").trim());
                    info.setCoordinateX(rs.getString("coordinateX").trim());
                    info.setCoordinateY(rs.getString("coordinateY").trim());
                    info.setSunshiZhekuan(rs.getString("sunshi_Zhekuan").trim());
                    info.setInjuredNumber(rs.getString("injured_Number").trim());
                    info.setIsDelete(rs.getString("is_Delete").trim());
                    info.setRemark(rs.getString("remark").trim());
                    info.setShizhuPhone(rs.getString("shizhu_Phone").trim());
                    info.setInformantPhone(rs.getString("informant_Phone").trim());
                    info.setDistrict(rs.getString("district").trim());
                    info.setProcessTime(rs.getString("process_Time").trim());
                    info.setDeadNumber(rs.getString("dead_Number").trim());
                    info.setPoliceArea(rs.getString("police_Area").trim());
                    info.setFillFormPerson(rs.getString("fill_Form_Person").trim());
                }
            }
            catch (Exception e)
            {
                Log4J.getLogger(this.getClass()).error("获取警情信息失败：" + e.getMessage());
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
                    Log4J.getLogger(this.getClass()).error("获取警情信息失败：" + e.getMessage());
                }

            }
            return info;
        }
    }

}
