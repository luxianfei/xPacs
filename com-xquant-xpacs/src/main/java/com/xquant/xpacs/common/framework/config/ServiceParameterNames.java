package com.xquant.xpacs.common.framework.config;


 /**
 * @ClassName: ServiceParameterNames
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: deming.ye
 * @date: 2018年7月25日 下午3:14:54
 *
 */
public class ServiceParameterNames {

	/** 请求ID的默认参数名 */
    private static final String REQUEST_ID = "requestId";

    /** 应用标识的默认参数名 */
    private static final String APP_KEY = "appKey";

    /** 方法的默认参数名 */
    private static final String METHOD = "method";

    /** 服务版本号的默认参数名 */
    private static final String VERSION = "version";

    /** 签名算法的默认参数名 */
    private static final String SIGN_TYPE = "signType";

    /** 签名的默认参数名 */
    private static final String SIGN = "sign";

    /** 格式化默认参数名 */
    private static final String FORMAT = "format";

    /** 本地化默认参数名 */
    private static final String LOCALE = "locale";

    /** 会话id默认参数名 */
    private static final String SESSION_ID = "sessionId";

    /** 用户ID参数名 */
    private static final String USER_ID = "userId";

    private static String requestId = REQUEST_ID;

    private static String method = METHOD;

    private static String format = FORMAT;

    private static String version = VERSION;

    private static String signType = SIGN_TYPE;

    private static String sign = SIGN;

    private static String locale = LOCALE;

    private static String sessionId = SESSION_ID;

    private static String appKey = APP_KEY;

    private static String userId = USER_ID;

    public static String getRequestId() {
        return requestId;
    }

    public static void setRequestId(String requestId) {
        ServiceParameterNames.requestId = requestId;
    }

    public static String getMethod() {
        return method;
    }

    public static void setMethod(String method) {
        ServiceParameterNames.method = method;
    }

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        ServiceParameterNames.version = version;
    }

    public static String getSignType() {
        return signType;
    }

    public static void setSignType(String signType) {
        ServiceParameterNames.signType = signType;
    }

    public static String getSign() {
        return sign;
    }

    public static void setSign(String sign) {
        ServiceParameterNames.sign = sign;
    }

    public static String getFormat() {
        return format;
    }

    public static void setFormat(String format) {
        ServiceParameterNames.format = format;
    }

    public static String getLocale() {
        return locale;
    }

    public static void setLocale(String locale) {
        ServiceParameterNames.locale = locale;
    }

    public static String getSessionId() {
        return sessionId;
    }

    public static void setSessionId(String sessionId) {
        ServiceParameterNames.sessionId = sessionId;
    }

    public static String getAppKey() {
        return appKey;
    }

    public static void setAppKey(String appKey) {
        ServiceParameterNames.appKey = appKey;
    }

	public static String getUserId() {
		return userId;
	}

	public static void setUserId(String userId) {
		ServiceParameterNames.userId = userId;
	}

}
