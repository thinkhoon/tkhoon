package com.tkhoon.framework;

import com.tkhoon.framework.helper.AOPHelper;
import com.tkhoon.framework.helper.ActionHelper;
import com.tkhoon.framework.helper.BeanHelper;
import com.tkhoon.framework.helper.DBHelper;
import com.tkhoon.framework.helper.EntityHelper;
import com.tkhoon.framework.helper.IOCHelper;
import com.tkhoon.framework.helper.PluginHelper;
import org.apache.log4j.Logger;

public final class Smart {

    private static final Logger logger = Logger.getLogger(Smart.class);

    public static void init() {
        try {
            Class<?>[] classList = {
                DBHelper.class,
                EntityHelper.class,
                ActionHelper.class,
                BeanHelper.class,
                AOPHelper.class,
                IOCHelper.class,
                PluginHelper.class,
            };
            for (Class<?> cls : classList) {
                Class.forName(cls.getName());
            }
        } catch (Exception e) {
            logger.error("加载 Helper 出错！", e);
        }
    }
}
