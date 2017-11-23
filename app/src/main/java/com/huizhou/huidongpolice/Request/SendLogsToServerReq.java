package com.huizhou.huidongpolice.Request;

public class SendLogsToServerReq extends CommonRequest
{
    private String logMessages;

    public String getLogMessages()
    {
        return logMessages;
    }

    public void setLogMessages(String logMessages)
    {
        this.logMessages = logMessages;
    }


}
