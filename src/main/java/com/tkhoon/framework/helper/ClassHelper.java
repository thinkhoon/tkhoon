package com.tkhoon.framework.helper;

import com.tkhoon.framework.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.List;

public class ClassHelper {

    private static final String packageName = ConfigHelper.getInstance().getStringProperty("package");

    private static final ClassHelper instance = new ClassHelper();

    private ClassHelper() {
    }

    public static ClassHelper getInstance() {
        return instance;
    }

    public List<Class<?>> getClassListByPackage(String pkg) {
        return ClassUtil.getClassList(pkg, true);
    }

    public List<Class<?>> getClassListBySuper(Class<?> superClass) {
        return ClassUtil.getClassListBySuper(packageName, superClass);
    }

    public List<Class<?>> getClassListByInterface(Class<?> interfaceClass) {
        return ClassUtil.getClassListByInterface(packageName, interfaceClass);
    }

    public List<Class<?>> getClassListByAnnotation(Class<? extends Annotation> annotationClass) {
        return ClassUtil.getClassListByAnnotation(packageName, annotationClass);
    }
}
