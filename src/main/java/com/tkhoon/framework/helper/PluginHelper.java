package com.tkhoon.framework.helper;

import com.tkhoon.framework.FrameworkConstant;
import com.tkhoon.framework.Plugin;
import com.tkhoon.framework.util.ClassUtil;
import java.util.List;
import org.apache.log4j.Logger;

public class PluginHelper {

    private static final Logger logger = Logger.getLogger(PluginHelper.class);

    static {
        try {
            // 获取并遍历所有的 Plugin 类（实现了 Plugin 接口的类）
            List<Class<?>> pluginClassList = ClassUtil.getClassListBySuper(FrameworkConstant.PLUGIN_PACKAGE, Plugin.class);
            for (Class<?> pluginClass : pluginClassList) {
                // 创建 Plugin 实例
                Plugin plugin = (Plugin) pluginClass.newInstance();
                // 调用初始化方法
                plugin.init();
            }
        } catch (Exception e) {
            logger.error("初始化 BeanHelper 出错！", e);
        }
    }
}
