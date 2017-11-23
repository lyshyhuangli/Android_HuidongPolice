package com.huizhou.huidongpolice.utils;

import com.google.gson.Gson;
import com.huizhou.huidongpolice.database.DbConnect;
import com.huizhou.huidongpolice.database.dao.JingQingInfoDAO;
import com.huizhou.huidongpolice.database.dao.StaticFilesDAO;
import com.huizhou.huidongpolice.database.dao.impl.JingQingInfoDAOImpl;
import com.huizhou.huidongpolice.database.dao.impl.StaticFilesDAOImpl;
import com.huizhou.huidongpolice.database.dao.impl.UserInfoDAOImpl;
import com.huizhou.huidongpolice.database.vo.JingQingInfoRecord;
import com.huizhou.huidongpolice.database.vo.StaticFilesRecord;
import com.huizhou.huidongpolice.response.CheckDbConnectResp;
import com.huizhou.huidongpolice.response.CheckUserByUserAndPwdResp;
import com.huizhou.huidongpolice.response.GetAllAlarmCompanyResp;
import com.huizhou.huidongpolice.response.GetAllConformJingqingXingzhiResp;
import com.huizhou.huidongpolice.response.GetJingQingInfoByIdResp;
import com.huizhou.huidongpolice.response.GetJingQingMoreInfoByCountResp;
import com.huizhou.huidongpolice.response.GetLastSevenNumberJingQingResp;
import com.huizhou.huidongpolice.response.GetStaticFilsInfoByCountResp;
import com.huizhou.huidongpolice.response.GetWeekAlarmStaticResp;
import com.huizhou.huidongpolice.response.ModifyPwdByUserNameResp;

import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */

public class ProcessDataBaseThread implements Runnable
{
    private String userName;
    private String password;
    private int flag;
    private String id;

    private int getDataNo;
    private String startTime;
    private String endTime;
    private String briefly;
    private String conformJingqingXingzhi;
    private String alarmCompany;
    private String chooseIdNo;
    private String informantPhoneChoose;

    private String fileName;
    private String fileType;

    private Object object;

    public void setObject(Object o)
    {
        this.object = o;
    }

    public void setUserAndPwd(String userNameP, String passwordP)
    {
        this.userName = userNameP;
        this.password = passwordP;
    }

    public void setJingQingId(String id)
    {
        this.id = id;
    }

    public void setFlag(int flags)
    {
        this.flag = flags;
    }

    public void setGetDataNo(int getDataNo)
    {
        this.getDataNo = getDataNo;
    }

    public void setGetMoreData(
            String startTime, String endTime, String briefly,
            String conformJingqingXingzhi, String alarmCompany,
            String chooseIdNo, String informantPhoneChoose
    )
    {
        this.startTime = startTime;
        this.endTime = endTime;
        this.briefly = briefly;
        this.conformJingqingXingzhi = conformJingqingXingzhi;
        this.alarmCompany = alarmCompany;
        this.chooseIdNo = chooseIdNo;
        this.informantPhoneChoose = informantPhoneChoose;
    }

    public void setGetStaticFiles(String fileType, String fileName)
    {
        this.fileName = fileName;
        this.fileType = fileType;
    }


    @Override
    public void run()
    {
        switch (flag)
        {
            case Flags.DB_CONNECT:
            {
                String result = HttpClientClass.httpPost(object, "checkDbConnect");
                if (StringUtils.isBlank(result))
                {
                    CommonCache.dataCache.put(flag, false);
                    break;
                }

                Gson gson = new Gson();
                CheckDbConnectResp info = gson.fromJson(result, CheckDbConnectResp.class);
                if (null != info)
                {
                    if (0 == info.getResultCode())
                    {
                        CommonCache.dataCache.put(flag, info.getIsConnect());
                        break;
                    }
                }

                CommonCache.dataCache.put(flag, false);
                break;
            }
            //登录校验
            case Flags.ONE_ZERO_ZERO_ONE:
            {
                try
                {
                    String result = HttpClientClass.httpPost(object, "checkUserByUserAndPwd");
                    if (StringUtils.isBlank(result))
                    {
                        CommonCache.dataCache.put(flag, false);
                        break;
                    }

                    Gson gson = new Gson();
                    CheckUserByUserAndPwdResp info = gson.fromJson(result, CheckUserByUserAndPwdResp.class);
                    if (null != info)
                    {
                        if (0 == info.getResultCode())
                        {
                            CommonCache.dataCache.put(flag, info.getIsExist());
                            break;
                        }
                    }

                    CommonCache.dataCache.put(flag, false);
                    break;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            //查询最新5条警讯
            case Flags.ONE_ZERO_ZERO_TWO:
            {
                String result = HttpClientClass.httpPost(object, "getLastSevenNumberJingQing");
                if (StringUtils.isBlank(result))
                {
                    CommonCache.dataCache.put(flag, new LinkedList<JingQingInfoRecord>());
                    break;
                }

                Gson gson = new Gson();
                GetLastSevenNumberJingQingResp info = gson.fromJson(result, GetLastSevenNumberJingQingResp.class);
                if (null != info)
                {
                    if (0 == info.getResultCode())
                    {
                        CommonCache.dataCache.put(flag, info.getInfoList());
                        break;
                    }
                }

                CommonCache.dataCache.put(flag, new LinkedList<JingQingInfoRecord>());
                break;
            }
            //根据Id查询信息
            case Flags.ONE_ZERO_ZERO_THREE:
            {
                String result = HttpClientClass.httpPost(object, "getJingQingInfoById");
                if (StringUtils.isBlank(result))
                {
                    CommonCache.dataCache.put(flag, new JingQingInfoRecord());
                    break;
                }
                Gson gson = new Gson();
                GetJingQingInfoByIdResp info = gson.fromJson(result, GetJingQingInfoByIdResp.class);
                if (null != info)
                {
                    if (0 == info.getResultCode())
                    {
                        CommonCache.dataCache.put(flag, info.getInfo());
                        break;
                    }
                }

                CommonCache.dataCache.put(flag, new JingQingInfoRecord());
                break;
            }

            //滑动获取数据
            case Flags.ONE_ZERO_ZERO_FOUR:
            {
                try
                {
                    String result = HttpClientClass.httpPost(object, "getJingQingMoreInfoByCount");
                    if (StringUtils.isBlank(result))
                    {
                        CommonCache.dataCache.put(flag, new LinkedList<JingQingInfoRecord>());
                        break;
                    }

                    Gson gson = new Gson();
                    GetJingQingMoreInfoByCountResp info = gson.fromJson(result, GetJingQingMoreInfoByCountResp.class);
                    if (null != info)
                    {
                        if (0 == info.getResultCode())
                        {
                            CommonCache.dataCache.put(flag, info.getInfList());
                            break;
                        }
                    }

                    CommonCache.dataCache.put(flag, new LinkedList<JingQingInfoRecord>());
                    break;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            //修改密码
            case Flags.ONE_ZERO_ZERO_FIVE:
            {
                String result = HttpClientClass.httpPost(object, "modifyPwdByUserName");
                if (StringUtils.isBlank(result))
                {
                    CommonCache.dataCache.put(flag, false);
                    break;
                }

                Gson gson = new Gson();
                ModifyPwdByUserNameResp info = gson.fromJson(result, ModifyPwdByUserNameResp.class);
                if (null != info)
                {
                    if (0 == info.getResultCode())
                    {
                        CommonCache.dataCache.put(flag, info.getIsOK());
                        break;
                    }
                }

                CommonCache.dataCache.put(flag, false);
                break;
            }

            //查询统计文件
            case Flags.SEARCH_STATIC_FILES:
            {
                String result = HttpClientClass.httpPost(object, "getStaticFilsInfoByCount");
                if (StringUtils.isBlank(result))
                {
                    CommonCache.dataCache.put(flag, new LinkedList<StaticFilesRecord>());
                    break;
                }

                Gson gson = new Gson();
                GetStaticFilsInfoByCountResp info = gson.fromJson(result, GetStaticFilsInfoByCountResp.class);
                if (null != info)
                {
                    if (0 == info.getResultCode())
                    {
                        CommonCache.dataCache.put(flag, info.getFileList());
                        break;
                    }
                }

                CommonCache.dataCache.put(flag, new LinkedList<StaticFilesRecord>());
                break;
            }

            //查询所有确认警情性质
            case Flags.SEARCH_ALL_CONFORM_JINGQING_XINGZHI:
            {
                String result = HttpClientClass.httpPost(object, "getAllConformJingqingXingzhi");
                if (StringUtils.isBlank(result))
                {
                    CommonCache.dataCache.put(flag, new LinkedList<String>());
                    break;
                }
                Gson gson = new Gson();
                GetAllConformJingqingXingzhiResp info = gson.fromJson(result, GetAllConformJingqingXingzhiResp.class);
                if (null != info)
                {
                    if (0 == info.getResultCode())
                    {
                        CommonCache.dataCache.put(flag, info.getInfoLit());
                        break;
                    }
                }

                CommonCache.dataCache.put(flag, new LinkedList<String>());
                break;
            }

            //查询所有派出所
            case Flags.SEARCH_ALL_ALARM_COMPANY:
            {
                String result = HttpClientClass.httpPost(object, "getAllAlarmCompany");
                if (StringUtils.isBlank(result))
                {
                    CommonCache.dataCache.put(flag, new LinkedList<String>());
                    break;
                }

                Gson gson = new Gson();
                GetAllAlarmCompanyResp info = gson.fromJson(result, GetAllAlarmCompanyResp.class);
                if (null != info)
                {
                    if (0 == info.getResultCode())
                    {
                        CommonCache.dataCache.put(flag, info.getInfoList());
                        break;
                    }
                }

                CommonCache.dataCache.put(flag, new LinkedList<String>());
                break;
            }

            //统计一周案情
            case Flags.STATIC_WEEK_ALARM_COUNT:
            {
                String result = HttpClientClass.httpPost(object, "getWeekAlarmStatic");
                if (StringUtils.isBlank(result))
                {
                    CommonCache.dataCache.put(flag, new LinkedList<List<AnQingRecord>>());
                    break;
                }

                Gson gson = new Gson();
                GetWeekAlarmStaticResp info = gson.fromJson(result, GetWeekAlarmStaticResp.class);
                if (null != info)
                {
                    if (0 == info.getResultCode())
                    {
                        CommonCache.dataCache.put(flag, info.getInfoList());
                        break;
                    }
                }

                CommonCache.dataCache.put(flag, new LinkedList<List<AnQingRecord>>());
                break;
            }

            //上传日志到服务器
            case Flags.SEND_LOGS_TO_SERVER:
            {
                HttpClientClass.httpPost(object, "sendLogsToServer");
                break;
            }

            default:
                break;
        }
    }
}
