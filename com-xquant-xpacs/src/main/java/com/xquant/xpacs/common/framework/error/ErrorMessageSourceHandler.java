package com.xquant.xpacs.common.framework.error;

import java.util.EnumMap;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.Assert;

public class ErrorMessageSourceHandler {

	private static Logger logger = LoggerFactory.getLogger(ErrorMessageSourceHandler.class);

    private static final String ERROR_CODE_PREFIX = "ERROR_";
    private static final String ERROR_SOLUTION_SUBFIX = "_SOLUTION";

    //子错误和主错误对应Map,key为子错误代码，值为主错误代码
    private static final EnumMap<SubErrorType, MainErrorType> SUBERROR_MAINERROR_MAPPINGS =
            new EnumMap<SubErrorType, MainErrorType>(SubErrorType.class);
    private static final String PARAM_1 = "xxx";
    private static final String PARAM_2 = "yyy";

    // 错误信息的国际化信息
//    private static MessageSourceAccessor messageSource;
    private static ResourceBundleMessageSource messageSource;

    static {
        SUBERROR_MAINERROR_MAPPINGS.put(
                SubErrorType.ISP_SERVICE_UNAVAILABLE, MainErrorType.SERVICE_CURRENTLY_UNAVAILABLE);
        SUBERROR_MAINERROR_MAPPINGS.put(
                SubErrorType.ISV_MISSING_PARAMETER, MainErrorType.MISSING_REQUIRED_ARGUMENTS);
        SUBERROR_MAINERROR_MAPPINGS.put(
                SubErrorType.ISV_PARAMETERS_MISMATCH, MainErrorType.INVALID_ARGUMENTS);
        SUBERROR_MAINERROR_MAPPINGS.put(
                SubErrorType.ISV_INVALID_PARAMETE, MainErrorType.INVALID_ARGUMENTS);
    }

    public static MainError getMainError(MainErrorType mainErrorType,Locale locale,Object... params) {
        String errorMessage = getErrorMessage(ERROR_CODE_PREFIX + mainErrorType.value(),locale,params);
        String errorSolution = getErrorSolution(ERROR_CODE_PREFIX + mainErrorType.value() + ERROR_SOLUTION_SUBFIX, locale);
        return new MainError(mainErrorType.value(), errorMessage, errorSolution);
    }

    public static void setErrorMessageSource(ResourceBundleMessageSource messageSource) {
    	ErrorMessageSourceHandler.messageSource = messageSource;
    }

    public static String getErrorMessage(String code, Locale locale,Object... params) {
        try {
            Assert.notNull(messageSource, "请先设置错误消息的国际化资源");
            return messageSource.getMessage(code, params, locale);
        } catch (NoSuchMessageException e) {
            logger.error("不存在对应的错误键：{}，请检查是否在i18n/errorMessage的错误资源", code);
            throw e;
        }
    }

    private static String getErrorSolution(String code, Locale locale) {
        try {
            Assert.notNull(messageSource, "请先设置错误解决方案的国际化资源");
            return messageSource.getMessage(code, new Object[]{}, locale);
        } catch (NoSuchMessageException e) {
            logger.error("不存在对应的错误键：{}，请检查是否在i18n/errorMessage的错误资源", code);
            throw e;
        }
    }

    /**
     * 获取对应子错误的主错误
     * @Method: getMainError
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param subErrorType
     * @param locale
     * @return
     */
    public static MainError getMainError(SubErrorType subErrorType, Locale locale, Object... params) {
        return getMainError(SUBERROR_MAINERROR_MAPPINGS.get(subErrorType), locale,params);
    }

    /**
     * @Method: getSubError
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param subErrorCode 子错误代码
     * @param subErrorKey  子错误信息键
     * @param locale       本地化
     * @param params       本地化消息参数
     * @return
     */
    public static SubError getSubError(String subErrorCode, String subErrorKey, Locale locale, Object... params) {
        try {
            String parsedSubErrorMessage = messageSource.getMessage(subErrorKey, params, locale);
            return new SubError(subErrorCode, parsedSubErrorMessage);
        } catch (NoSuchMessageException e) {
            logger.error("不存在对应的错误键：{}，请检查是否正确配置了应用的错误资源", subErrorCode);
            throw e;
        }
    }

    public static String getSubErrorCode(SubErrorType subErrorType, Object... params) {
        String subErrorCode = subErrorType.value();
        if (params.length > 0) {
            if (params.length == 1) {
                subErrorCode = subErrorCode.replace(PARAM_1, String.valueOf(params[0]));
            } else {
                subErrorCode = subErrorCode.replace(PARAM_1, String.valueOf(params[0]));
                if (params[1] != null) {
                    subErrorCode = subErrorCode.replace(PARAM_2, String.valueOf(params[1]));
                }
            }
        }
        return subErrorCode;
    }
}
