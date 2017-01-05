package com.tkhoon.framework.aspect;

import com.tkhoon.framework.proxy.Proxy;
import java.util.List;

public abstract class PluginAspect implements Proxy {

    private List<Class<?>> targetClassList;

    public final List<Class<?>> getTargetClassList() {
        return targetClassList;
    }

    public final void setTargetClassList(List<Class<?>> targetClassList) {
        this.targetClassList = targetClassList;
    }

    public abstract void initPlugin();
}
