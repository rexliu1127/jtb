package io.grx.pay.util;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AllinPayTranxResult {
    private String returnCode;
    private String returnMessage;
    private String resultCode;
    private String resultMessage;

    private static final Set<String> PENDING_CODES = new HashSet<String>(Arrays.asList(
            "2000", "2001","2003","2005","2007","2008","0003","0014"));

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public boolean isSuccess() {
        return StringUtils.equalsIgnoreCase(returnCode, "0000")
                || StringUtils.equalsIgnoreCase(returnCode, "4000");
    }

    public boolean isProcessing() {
        return PENDING_CODES.contains(returnCode);
    }

    public boolean isFailed() {
        return !isSuccess() && !isProcessing();
    }
}
