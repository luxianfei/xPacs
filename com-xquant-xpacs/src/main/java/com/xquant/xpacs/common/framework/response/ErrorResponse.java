package com.xquant.xpacs.common.framework.response;

import com.xquant.xpacs.common.framework.error.MainError;
import com.xquant.xpacs.common.framework.error.SubError;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse extends BaseResponse {

	private static final long serialVersionUID = 3621474005245274081L;

    protected String solution;

    protected List<SubError> subErrors;

    public ErrorResponse() {
    }

    public ErrorResponse(MainError mainError) {
        super.code = mainError.getCode();
        super.message = mainError.getMessage();
        this.solution = mainError.getSolution();
        if (mainError.getSubErrors() != null && mainError.getSubErrors().size() > 0) {
            this.subErrors = mainError.getSubErrors();
        }
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public List<SubError> getSubErrors() {
        return subErrors;
    }

    public void setSubErrors(List<SubError> subErrors) {
        this.subErrors = subErrors;
    }

    public void addSubError(SubError subError){
        if (subErrors == null) {
            subErrors = new ArrayList<SubError>();
        }
        subErrors.add(subError);
    }

    protected void setMainError(MainError mainError) {
    	super.setCode(mainError.getCode());
    	super.setMessage(mainError.getMessage());
        setSolution(mainError.getSolution());
    }

    /**
     * 对服务名进行标准化处理
     * @Method: transform
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param method
     * @return
     */
    protected String transform(String method) {
        if(method != null){
            method = method.replace(".", "-");
            return method;
        }else{
            return "LACK_METHOD";
        }
    }
}
