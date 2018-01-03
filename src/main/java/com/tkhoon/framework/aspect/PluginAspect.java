package com.tkhoon.framework.aspect;

import com.tkhoon.framework.proxy.Proxy;

import java.util.List;

public abstract class PluginAspect implements Proxy {

    public abstract List<Class<?>> getTargetClassList();
}
