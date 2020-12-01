package com.xquant.xpacs.common.framework.response;

public class SuccessResponse extends BaseResponse {

	private static final long serialVersionUID = 5501035061449807785L;

	public static final String SUCCESS_CODE = "000000";
	public static final String SUCCESS_MESSAGE = "服务执行成功";

	public static final String FAIL_CODE = "999999";
	public static final String FAIL_MESSAGE = "服务执行失败";

	public static final SuccessResponse SUCCESSFUL_RESPONSE = new SuccessResponse();

	public SuccessResponse() {
		super.setCode(SUCCESS_CODE);
		super.setMessage(SUCCESS_MESSAGE);
    }
}
