package com.tkhoon.framework.helper;

import com.tkhoon.framework.annotation.Action;
import com.tkhoon.framework.annotation.Request;
import com.tkhoon.framework.bean.ActionBean;
import com.tkhoon.framework.bean.RequestBean;
import com.tkhoon.framework.util.ArrayUtil;
import com.tkhoon.framework.util.StringUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionHelper {

    private static final Map<RequestBean, ActionBean> actionMap = new HashMap<RequestBean, ActionBean>();

    static {
        // 获取并遍历所有 Action 类
        List<Class<?>> actionClassList = ClassHelper.getClassListByAnnotation(Action.class);
        for (Class<?> actionClass : actionClassList) {
            // 获取并遍历该 Action 类中所有的方法（不包括父类中的方法）
            Method[] actionMethods = actionClass.getDeclaredMethods();
            if (ArrayUtil.isNotEmpty(actionMethods)) {
                for (Method actionMethod : actionMethods) {
                    // 判断当前 Action 方法是否带有 @Request 注解
                    if (actionMethod.isAnnotationPresent(Request.class)) {
                        // 获取 @Requet 注解中的 URL 字符串
                        String[] urlArray = StringUtil.splitString(actionMethod.getAnnotation(Request.class).value(), ":");
                        if (ArrayUtil.isNotEmpty(urlArray)) {
                            // 获取请求方法与请求路径
                            String requestMethod = urlArray[0];
                            String requestPath = urlArray[1]; // 带有占位符
                            // 将请求路径中的占位符 {\w+} 转换为正则表达式 (\\w+)
                            requestPath = StringUtil.replaceAll(requestPath, "\\{\\w+\\}", "(\\\\w+)");
                            // 将 RequestBean 与 ActionBean 放入 Action Map 中
                            actionMap.put(new RequestBean(requestMethod, requestPath), new ActionBean(actionClass, actionMethod));
                        }
                    }
                }
            }
        }
    }

    public static Map<RequestBean, ActionBean> getActionMap() {
        return actionMap;
    }
}
