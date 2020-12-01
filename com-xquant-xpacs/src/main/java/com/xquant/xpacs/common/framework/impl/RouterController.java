package com.xquant.xpacs.common.framework.impl;

import com.alibaba.fastjson.JSONObject;
import com.xquant.xpacs.common.framework.api.IServiceRouter;
import com.xquant.xpacs.common.remoting.IndexClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@ConditionalOnProperty(name = "engine.index.remoting.enabled", matchIfMissing = true)
@RequestMapping("module")
public class RouterController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RouterController.class);

	@Autowired
	private IServiceRouter serviceRouter;
	@Autowired
	private IndexClient engineClient;
	/**
	 * 指标方案路由
	 * @param req  HTTP请求
	 * @param resp HTTP响应
	 * @return
	 */
	@RequestMapping("/router")
	public ResponseEntity<JSONObject> service(@RequestBody(required = false) String body, HttpMethod method, HttpServletRequest request, HttpServletResponse response) {
		// 调用数据集服务
		String responseContext = engineClient.calcModule(request);
		JSONObject jsonObject = JSONObject.parseObject(responseContext);
 		return ResponseEntity.ok(jsonObject);
	}

	/**
	 * 数据源服务路由
	 * @param req  HTTP请求
	 * @param resp HTTP响应
	 * @return
	 */
	@GetMapping("/serviceRouter")
	public ResponseEntity<Object> annotationService(HttpServletRequest req, HttpServletResponse resp) {
		Object response = serviceRouter.annotationService(req, resp);
 		return ResponseEntity.ok(response);
	}

}
