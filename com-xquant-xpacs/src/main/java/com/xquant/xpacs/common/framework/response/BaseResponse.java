package com.xquant.xpacs.common.framework.response;

import java.io.Serializable;

public class BaseResponse implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 229839101827279446L;

	protected String code;

    protected String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccessful() {
    	return SuccessResponse.SUCCESS_CODE.equals(this.code);
    }

}
