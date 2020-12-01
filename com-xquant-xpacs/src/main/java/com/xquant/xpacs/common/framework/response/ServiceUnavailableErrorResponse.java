package com.xquant.xpacs.common.framework.response;

import com.xquant.xpacs.common.framework.error.ErrorMessageSourceHandler;
import com.xquant.xpacs.common.framework.error.MainError;
import com.xquant.xpacs.common.framework.error.SubError;
import com.xquant.xpacs.common.framework.error.SubErrorType;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Locale;

public class ServiceUnavailableErrorResponse extends ErrorResponse {
	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 6909228677122198149L;

	private static final String ISP = "isp.";

    private static final String SERVICE_UNAVAILABLE = "-service-unavailable";

    public ServiceUnavailableErrorResponse() {
    }

    public ServiceUnavailableErrorResponse(String method, Locale locale) {
        MainError mainError = ErrorMessageSourceHandler.getMainError(SubErrorType.ISP_SERVICE_UNAVAILABLE, locale);
        String errorCodeKey = ISP + transform(method) + SERVICE_UNAVAILABLE;
        SubError subError = ErrorMessageSourceHandler.getSubError(errorCodeKey,
                SubErrorType.ISP_SERVICE_UNAVAILABLE.value(),
                locale, method,"NONE","NONE");
        ArrayList<SubError> subErrors = new ArrayList<SubError>();
        subErrors.add(subError);

        setMainError(mainError);
        setSubErrors(subErrors);
    }

    public ServiceUnavailableErrorResponse(String method, Locale locale, Throwable throwable) {
        MainError mainError = ErrorMessageSourceHandler.getMainError(SubErrorType.ISP_SERVICE_UNAVAILABLE, locale);

        ArrayList<SubError> subErrors = new ArrayList<SubError>();

        String errorCodeKey = ISP + transform(method) + SERVICE_UNAVAILABLE;
        Throwable srcThrowable = throwable;
        if(throwable.getCause() != null){
            srcThrowable = throwable.getCause();
        }
        SubError subError = ErrorMessageSourceHandler.getSubError(errorCodeKey,
                SubErrorType.ISP_SERVICE_UNAVAILABLE.value(),
                locale,
                method, srcThrowable.getClass().getName(),getThrowableInfo(throwable));
        subErrors.add(subError);

        setSubErrors(subErrors);
        setMainError(mainError);
    }

    private String getThrowableInfo(Throwable throwable) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        PrintStream printStream = new PrintStream(outputStream);
        throwable.printStackTrace(printStream);
        return outputStream.toString();
    }
}
