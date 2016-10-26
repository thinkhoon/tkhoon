package com.tkhoon.framework;

import com.tkhoon.framework.helper.AOPHelper;
import com.tkhoon.framework.helper.ActionHelper;
import com.tkhoon.framework.helper.BeanHelper;
import com.tkhoon.framework.helper.DBHelper;
import com.tkhoon.framework.helper.EntityHelper;
import com.tkhoon.framework.helper.IOCHelper;
import com.tkhoon.framework.helper.PluginHelper;
import com.tkhoon.framework.util.ClassUtil;

public final class HelperLoader {

    public static void init() {
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
            ClassUtil.loadClass(cls.getName(), true);
        }
    }
}
