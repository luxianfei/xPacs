package com.xquant.xpacs.common.framework.error;

import java.util.EnumMap;

public enum SubErrorType {
    ISP_SERVICE_UNAVAILABLE,

    ISV_NOT_EXIST,
    ISV_MISSING_PARAMETER,
    ISV_INVALID_PARAMETE,
    ISV_PARAMETERS_MISMATCH;

    private static EnumMap<SubErrorType, String> errorKeyMap = new EnumMap<SubErrorType, String>(SubErrorType.class);

    static {
        errorKeyMap.put(SubErrorType.ISP_SERVICE_UNAVAILABLE, "isp.xxx-service-unavailable");

        errorKeyMap.put(SubErrorType.ISV_NOT_EXIST, "isv.xxx-not-exist:invalid-yyy");
        errorKeyMap.put(SubErrorType.ISV_MISSING_PARAMETER, "isv.missing-parameter:xxx");
        errorKeyMap.put(SubErrorType.ISV_INVALID_PARAMETE, "isv.invalid-paramete:xxx");
        errorKeyMap.put(SubErrorType.ISV_PARAMETERS_MISMATCH, "isv.parameters-mismatch:xxx-and-yyy");
    }

    public String value() {
        return errorKeyMap.get(this);
    }

}
