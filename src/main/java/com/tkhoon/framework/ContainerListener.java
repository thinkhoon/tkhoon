package com.tkhoon.framework;

import com.tkhoon.framework.helper.ConfigHelper;
import com.tkhoon.framework.util.StringUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContainerListener implements ServletContextListener {

    private static final String wwwPath = ConfigHelper.getStringProperty(FrameworkConstant.APP_WWW_PATH);
    private static final String jspPath = ConfigHelper.getStringProperty(FrameworkConstant.APP_JSP_PATH);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 初始化 Helper 类
        tkhoon.init();
        // 添加 Servlet 映射
        addServletMapping(sce.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    private void addServletMapping(ServletContext context) {
        // 用 DefaultServlet 映射所有静态资源
        registerDefaultServlet(context);
        // 用 JspServlet 映射所有 JSP 请求
        registerJspServlet(context);
    }

    private void registerDefaultServlet(ServletContext context) {
        ServletRegistration defaultServletReg = context.getServletRegistration(FrameworkConstant.DEFAULT_SERVLET_NAME);
        defaultServletReg.addMapping("/favicon.ico");
        if (StringUtil.isNotEmpty(wwwPath)) {
            defaultServletReg.addMapping(wwwPath + "*");
        }
    }

    private void registerJspServlet(ServletContext context) {
        if (StringUtil.isNotEmpty(jspPath)) {
            ServletRegistration jspServletReg = context.getServletRegistration(FrameworkConstant.JSP_SERVLET_NAME);
            jspServletReg.addMapping(jspPath + "*");
        }
    }
}
