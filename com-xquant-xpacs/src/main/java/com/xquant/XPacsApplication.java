/**
 * ******************************************
 * 文件名称: XPacsApplication.java
 * 系统名称: xAsset量化管理分析系统
 * 模块名称: 指标计算
 * 软件版权: 杭州衡泰软件有限公司
 *
 * @Description: TODO(用一句话描述该文件做什么)
 * @version: V1.0.0.0100
 * @author: yt.zhou
 * 开发时间: 2020年11月02日 10:59:22
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 ********************************************/
package com.xquant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @ClassName: XPacsApplication
 * @Description: SpringBoot启动
 * @author: yt.zhou
 * @date: 2020年11月02日 10:59:22
 */
@SpringBootApplication
@EnableCaching
@EnableWebMvc
public class XPacsApplication  {

    public static void main(String[] args) {
        SpringApplication.run(XPacsApplication.class, args);
    }
}
