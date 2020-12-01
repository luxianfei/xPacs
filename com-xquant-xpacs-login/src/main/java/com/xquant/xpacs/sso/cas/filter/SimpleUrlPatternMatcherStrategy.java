package com.xquant.xpacs.sso.cas.filter;

import org.jasig.cas.client.authentication.UrlPatternMatcherStrategy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 过滤掉一些不需要授权，登录的界面
 */
public class SimpleUrlPatternMatcherStrategy implements UrlPatternMatcherStrategy {
    private Pattern pattern;
    /**
     * 判断是否匹配这个字符串
     * @param url 用户请求的连接
     * @return true : 我就不拦截你了
     *         false :必须得登录了
     */
    @Override
    public boolean matches(String url) {
        //当含有loginout的字段，就可以不用登录了
    	Matcher matcher = pattern.matcher(url);
    	return matcher.find();
    }

    /**
     * 正则表达式的规则，这个地方可以是web传递过来的
     */
    @Override
    public void setPattern(String pattern) {
    	this.pattern=Pattern.compile(pattern);
    }
}
