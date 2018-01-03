package com.tkhoon.framework.helper;

import com.tkhoon.framework.util.ClassUtil;
import java.lang.annotation.Annotation;
import java.util.List;

public class ClassHelper {

    private static final String packageName = ConfigHelper.getStringProperty("app.package");

    public static List<Class<?>> getClassListByPackage(String pkg) {
        return ClassUtil.getClassList(pkg, true);
    }

    public static List<Class<?>> getClassListBySuper(Class<?> superClass) {
        return ClassUtil.getClassListBySuper(packageName, superClass);
    }

    public static List<Class<?>> getClassListByAnnotation(Class<? extends Annotation> annotationClass) {
        return ClassUtil.getClassListByAnnotation(packageName, annotationClass);
    }
}
