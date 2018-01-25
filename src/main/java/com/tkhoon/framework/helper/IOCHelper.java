package com.tkhoon.framework.helper;

import com.tkhoon.framework.annotation.Impl;
import com.tkhoon.framework.annotation.Inject;
import com.tkhoon.framework.util.ArrayUtil;
import com.tkhoon.framework.util.CollectionUtil;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOCHelper {

    private static final Logger logger = LoggerFactory.getLogger(IOCHelper.class);

    static {
        try {
            // 获取并遍历所有的 Bean 类
            Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                // 获取 Bean 类与 Bean 实例
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                // 获取 Bean 类中所有的字段（不包括父类中的方法）
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(beanFields)) {
                    // 遍历所有的 Bean 字段
                    for (Field beanField : beanFields) {
                        // 判断当前 Bean 字段是否带有 @Inject 注解
                        if (beanField.isAnnotationPresent(Inject.class)) {
                            // 获取 Bean 字段对应的实现类
                            Class<?> implementClass = getImplementClass(beanField);
                            // 若存在实现类，则执行以下代码
                            if (implementClass != null) {
                                // 从 Bean Map 中获取该实现类对应的实现类实例
                                Object implementInstance = beanMap.get(implementClass);
                                // 设置该 Bean 字段的值
                                if (implementInstance != null) {
                                    beanField.setAccessible(true); // 取消类型安全检测（可提高反射性能）
                                    beanField.set(beanInstance, implementInstance); // beanInstance 是普通实例，或 CGLib 动态代理实例（不能使 JDK 动态代理实例）
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("初始化 IOCHelper 出错！", e);
        }
    }

    private static Class<?> getImplementClass(Field beanField) {
        // 定义实现类对象
        Class<?> implementClass = null;
        // 获取 Bean 字段对应的接口
        Class<?> interfaceClass = beanField.getType();
        // 通过接口查找唯一的实现类
        return findImplementClass(interfaceClass);
    }

    public static Class<?> findImplementClass(Class<?> interfaceClass) {
        Class<?> implementClass = interfaceClass;
        // 判断接口上是否标注了 @Impl 注解
        if (interfaceClass.isAnnotationPresent(Impl.class)) {
            // 获取强制指定的实现类
            implementClass = interfaceClass.getAnnotation(Impl.class).value();
        } else {
            // 获取该接口所有的实现类
            List<Class<?>> implementClassList = ClassHelper.getClassListBySuper(interfaceClass);
            if (CollectionUtil.isNotEmpty(implementClassList)) {
                // 获取第一个实现类
                implementClass = implementClassList.get(0);
            }
        }
        // 返回实现类对象
        return implementClass;
    }
}
