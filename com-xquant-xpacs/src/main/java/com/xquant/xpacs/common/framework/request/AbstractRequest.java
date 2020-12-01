package com.xquant.xpacs.common.framework.request;

/**
 * @ClassName: AbstractIndexRequest
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: deming.ye
 * @date: 2018年7月26日 下午5:59:19
 *
 */
public abstract class AbstractRequest implements IRequest {

    private IRequestContext requestContext;
    @Override
    public IRequestContext getRequestContext() {
        return requestContext;
    }

    public final void setRequestContext(IRequestContext requestContext) {
        this.requestContext = requestContext;
    }

}
