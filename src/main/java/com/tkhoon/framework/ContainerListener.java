package com.tkhoon.framework;

import com.tkhoon.framework.helper.AOPHelper;
import com.tkhoon.framework.helper.ActionHelper;
import com.tkhoon.framework.helper.BeanHelper;
import com.tkhoon.framework.helper.DBHelper;
import com.tkhoon.framework.helper.EntityHelper;
import com.tkhoon.framework.helper.IOCHelper;
import com.tkhoon.framework.helper.ServiceHelper;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContainerListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 初始化 Helper 类
        initHelperClass();
        // 添加 Servlet 映射
        addServletMapping(sce.getServletContext());
    }

    private void initHelperClass() {
        DBHelper.getInstance().init();
        EntityHelper.getInstance().init();
        ActionHelper.getInstance().init();
        BeanHelper.getInstance().init();
        ServiceHelper.getInstance().init();
        IOCHelper.getInstance().init();
        AOPHelper.getInstance().init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    private void addServletMapping(ServletContext context) {
        // 用 DefaultServlet 映射所有静态资源
        ServletRegistration defaultServletRegistration = context.getServletRegistration("default");
        defaultServletRegistration.addMapping("/favicon.ico", "/static/*", "/index.html");
        // 用 JspServlet 映射所有 JSP 请求
        ServletRegistration jspServletRegistration = context.getServletRegistration("jsp");
        jspServletRegistration.addMapping("/dynamic/jsp/*");
        // 用 UploadServlet 映射 /upload.do 请求
        ServletRegistration uploadServletRegistration = context.getServletRegistration("upload");
        uploadServletRegistration.addMapping("/upload.do");
    }
}
