package com.tkhoon.framework.util;

import java.lang.reflect.Field;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

public class ObjectUtil {

    private static final Logger logger = Logger.getLogger(ObjectUtil.class);

    // 设置字段值
    public static void setField(Object obj, String fieldName, Object fieldValue) {
        try {
            if (PropertyUtils.isWriteable(obj, fieldName)) {
                PropertyUtils.setProperty(obj, fieldName, fieldValue);
            }
        } catch (Exception e) {
            logger.error("设置字段出错！", e);
            throw new RuntimeException(e);
        }
    }

    // 获取字段值
    public static Object getFieldValue(Object obj, String fieldName) {
        Object propertyValue = null;
        try {
            if (PropertyUtils.isReadable(obj, fieldName)) {
                propertyValue = PropertyUtils.getProperty(obj, fieldName);
            }
        } catch (Exception e) {
            logger.error("获取字段出错！", e);
            throw new RuntimeException(e);
        }
        return propertyValue;
    }

    // 复制所有字段
    public static void copyFields(Object target, Object source) {
        try {
            for (Field field : target.getClass().getDeclaredFields()) {
                field.setAccessible(true); // 可操作私有字段
                field.set(source, field.get(target));
            }
        } catch (Exception e) {
            logger.error("复制字段出错！", e);
            throw new RuntimeException(e);
        }
    }
}
