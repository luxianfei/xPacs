package com.xquant.xpacs.common.framework.impl;

/**
 * @ClassName: ServiceMethodDefinition
 * @Description: 指标计算服务方法定义类
 * @author: deming.ye
 * @date: 2018年7月25日 上午11:03:36
 *
 */
public class ServiceMethodDefinition {

	/**
     * 默认的组
     */
    public static final String DEFAULT_GROUP = "DEFAULT";

    /**
     * 默认分组标识
     */
    public static final String DEFAULT_GROUP_TITLE = "DEFAULT GROUP";

    /**
     * API的方法
     */
    private String method;

    /**
     * API的方法的标识
     */
    private String methodTitle;

    /**
     * API方法所属组名
     */
    private String methodGroup = DEFAULT_GROUP;

    /**
     * API方法组名的标识
     */
    private String methodGroupTitle;

    /**
     * API所属的标签
     */
    private String[] tags = {};

    /**
     * 过期时间，单位为秒，0或负数表示不过期
     */
    private int timeout = -9999;

    /**
     * 对应的版本号，如果为null或""表示不区分版本
     */
    private String version = null;

    /**
     * 是否忽略服务请求签名的校验，默认为false
     */
    private boolean ignoreSign = false;

    /**
     * 服务方法是否已经过期
     */
    private boolean obsoleted = false;

    /**
     * 服务方法通配规则
     */
    private String methodMatchs;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethodTitle() {
        return methodTitle;
    }

    public void setMethodTitle(String methodTitle) {
        this.methodTitle = methodTitle;
    }

    public String getMethodGroup() {
        return methodGroup;
    }

    public void setMethodGroup(String methodGroup) {
        this.methodGroup = methodGroup;
    }

    public String getMethodGroupTitle() {
        return methodGroupTitle;
    }

    public void setMethodGroupTitle(String methodGroupTitle) {
        this.methodGroupTitle = methodGroupTitle;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isIgnoreSign() {
        return ignoreSign;
    }

    public void setIgnoreSign(boolean ignoreSign) {
        this.ignoreSign = ignoreSign;
    }

    public boolean isObsoleted() {
        return obsoleted;
    }

    public void setObsoleted(boolean obsoleted) {
        this.obsoleted = obsoleted;
    }

	public String getMethodMatchs() {
		return methodMatchs;
	}

	public void setMethodMatchs(String methodMatchs) {
		this.methodMatchs = methodMatchs;
	}
}
