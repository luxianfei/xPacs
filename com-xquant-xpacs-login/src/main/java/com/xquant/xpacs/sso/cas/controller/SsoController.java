package com.xquant.xpacs.sso.cas.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xquant.auth.userManager.constant.UserConstant;
import com.xquant.auth.userManager.entity.bo.LoginUserBO;
import com.xquant.auth.userManager.entity.po.AuthUser;
import com.xquant.common.bean.CommonResp;
import com.xquant.login.service.api.ILoginService;
import com.xquant.xpacs.sso.cas.filter.PlatformAuthenticationFilter;

@RestController
//@RequestMapping("/sso")
public class SsoController {


    @Value("${sso.cas.casServerLoginUrl:http://localhost:8080/cas}")
    private String casServerLoginUrl;
    @Autowired
    private ILoginService loginService;

    /**
     * 功能描述: 一站通单点登录受托系统入口<br>
     *
     * @Param: [request, response]
     * @Return: com.xquant.common.bean.CommonResp
     * @Author: zhicheng.yu
     * @Date: 2020/11/17 19:51
     */
    @RequestMapping("/ssoLogin")
    public CommonResp doPlatformSso(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        LoginUserBO userBO = (LoginUserBO) request.getSession().getAttribute(UserConstant.SESSION_USER);
        //casServer第一次登陆的情况
        if (userBO == null || userBO.getUserId() == null) {
        	String userName = getUserNameFromSSO(request);
        	LoginUserBO loginUser = new LoginUserBO();
        	if (StringUtils.isNoneBlank(userName)){
        		AuthUser user = loginService.getUserByAccount(userName);
        		if (user != null){
        			loginUser.setAccount(user.getAccount());
        			loginUser.setUserId(user.getUserId());
        			loginUser.setUserName(user.getUserName());
        		}
        		if (loginUser.getUserId() == null) {
        			return new CommonResp("403", "不存在的用户信息!");
        		}
        		session.setAttribute(UserConstant.SESSION_USER, loginUser);
            }
            return new CommonResp(loginUser);
        } else {
            //casServer登陆完以后的情况
            return new CommonResp(request.getSession().getAttribute(UserConstant.SESSION_USER));
        }
    }
    
    @RequestMapping("/ssoLogout")
    public CommonResp logout(HttpSession session) {
        session.invalidate();
        //使用cas退出成功后,跳转到http://cas.client1.com:9001/logout/success
        return new CommonResp("402", "ssoLogout", casServerLoginUrl + "/logout");
    }
    
    private String getUserNameFromSSO(HttpServletRequest request) {
    	String userName = null;
    	final HttpSession session = request.getSession();
        final Assertion assertion = (Assertion) session.getAttribute(PlatformAuthenticationFilter.CONST_CAS_ASSERTION);
        AttributePrincipal principal = assertion.getPrincipal();
        if (principal != null){
            userName = principal.getName();
        }
        return userName;
    }
    
}
