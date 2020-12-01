package com.xquant.xpacs.common.framework.error;

import java.util.EnumMap;

public enum MainErrorType {

	SERVICE_CURRENTLY_UNAVAILABLE,
	FORBIDDEN_REQUEST,

    MISSING_APP_KEY,
    MISSING_MODULE_NO,
    MISSING_REQUIRED_ARGUMENTS,

    INVALID_APP_KEY,
    INVALID_MODULE_NO,
    INVALID_FORMAT,
    INVALID_ARGUMENTS,

    BUSINESS_LOGIC_ERROR;

	private static EnumMap<MainErrorType, String> errorCodeMap = new EnumMap<MainErrorType, String>(MainErrorType.class);

	static {
		errorCodeMap.put(MainErrorType.SERVICE_CURRENTLY_UNAVAILABLE, "101001");
        errorCodeMap.put(MainErrorType.FORBIDDEN_REQUEST, "101002");

        errorCodeMap.put(MainErrorType.MISSING_APP_KEY, "102001");
        errorCodeMap.put(MainErrorType.MISSING_MODULE_NO, "102002");
        errorCodeMap.put(MainErrorType.MISSING_REQUIRED_ARGUMENTS, "102020");

        errorCodeMap.put(MainErrorType.INVALID_APP_KEY, "103001");
        errorCodeMap.put(MainErrorType.INVALID_MODULE_NO, "103002");
        errorCodeMap.put(MainErrorType.INVALID_FORMAT, "103003");
        errorCodeMap.put(MainErrorType.INVALID_ARGUMENTS, "103020");

        errorCodeMap.put(MainErrorType.BUSINESS_LOGIC_ERROR, "105001");
	}

	public String value() {
        return errorCodeMap.get(this);
    }

}
