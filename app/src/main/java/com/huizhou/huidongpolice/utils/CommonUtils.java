package com.huizhou.huidongpolice.utils;

import com.huizhou.huidongpolice.Request.CheckUserByUserAndPwdReq;
import com.huizhou.huidongpolice.Request.SendLogsToServerReq;

/**
 * Created by Administrator on 2017/9/1.
 */

public class CommonUtils
{
    /**
     * 上传日志到服务器
     *
     * @param classInfo
     * @param message
     * @param userName
     */
    public static void sendLogsToServer(String classInfo, String message, String userName)
    {
        ProcessDataBaseThread pd = new ProcessDataBaseThread();
        pd.setFlag(Flags.SEND_LOGS_TO_SERVER);
        SendLogsToServerReq req = new SendLogsToServerReq();
        String messages = "[" + userName + "]" + " -- " + classInfo + " -- " + message;
        req.setOperatorId(userName);
        req.setLogMessages(messages);
        pd.setObject(req);

        Thread t = new Thread(pd);
        t.start();
    }

    public static String getFileLineMethod(Exception e)
    {
        StackTraceElement[] trace = e.getStackTrace();
        if (trace == null || trace.length == 0)
            return "";

        StringBuffer toStringBuffer = new StringBuffer("[").append(
                trace[0].getFileName()).append(" -- ").append(
                trace[0].getMethodName()).append(" -- ").append(
                trace[0].getLineNumber()).append("]");
        return toStringBuffer.toString();
    }
}
