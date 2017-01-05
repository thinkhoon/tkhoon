package com.tkhoon.framework.helper;

import com.tkhoon.framework.annotation.Bean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class BeanHelper {

    private static final Logger logger = Logger.getLogger(BeanHelper.class);
    private static final BeanHelper instance = new BeanHelper();

    private final Map<Class<?>, Object> beanMap = new HashMap<Class<?>, Object>(); // Bean 类 => Bean 实例

    private BeanHelper() {
        if (logger.isDebugEnabled()) {
            logger.debug("[Init BeanHelper]");
        }
        try {
            // 获取并遍历所有的 Bean（带有 @Bean 注解的类）
            List<Class<?>> beanClassList = ClassHelper.getInstance().getClassListByAnnotation(Bean.class);
            for (Class<?> beanClass : beanClassList) {
                // 创建 Bean 实例
                Object beanInstance = beanClass.newInstance();
                // 将 Bean 实例放入 Bean Map 中（键为 Bean 类，值为 Bean 实例）
                beanMap.put(beanClass, beanInstance);
            }
        } catch (Exception e) {
            logger.error("初始化 BeanHelper 出错！", e);
        }
    }

    public static BeanHelper getInstance() {
        return instance;
    }

    public Map<Class<?>, Object> getBeanMap() {
        return beanMap;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> cls) {
        if (!beanMap.containsKey(cls)) {
            throw new RuntimeException("无法根据类名获取实例！" + cls);
        }
        return (T) beanMap.get(cls);
    }
}
