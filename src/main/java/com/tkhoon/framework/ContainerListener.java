package com.tkhoon.framework;

import com.tkhoon.framework.helper.ConfigHelper;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContainerListener implements ServletContextListener {

    private final String wwwPath = ConfigHelper.getInstance().getStringProperty("app.www_path");
    private final String jspPath = ConfigHelper.getInstance().getStringProperty("app.jsp_path");

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
        ServletRegistration defaultServletRegistration = context.getServletRegistration("default");
        defaultServletRegistration.addMapping("/favicon.ico", wwwPath + "*");
        // 用 JspServlet 映射所有 JSP 请求
        ServletRegistration jspServletRegistration = context.getServletRegistration("jsp");
        jspServletRegistration.addMapping(jspPath + "*");
        // 用 UploadServlet 映射 /upload.do 请求
        ServletRegistration uploadServletRegistration = context.getServletRegistration("upload");
        uploadServletRegistration.addMapping("/upload.do");
    }
}
