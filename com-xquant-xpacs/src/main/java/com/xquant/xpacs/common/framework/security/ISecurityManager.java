package com.xquant.xpacs.common.framework.security;

import com.xquant.xpacs.common.framework.error.MainError;
import com.xquant.xpacs.common.framework.request.IRequestContext;

public interface ISecurityManager {

	public MainError validateSystemParameters(IRequestContext requestContext);

	public MainError validateOther(IRequestContext requestContext);
}
