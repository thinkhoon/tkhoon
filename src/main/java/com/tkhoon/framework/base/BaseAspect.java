package com.tkhoon.framework.base;

import com.tkhoon.framework.proxy.Proxy;
import com.tkhoon.framework.proxy.ProxyChain;

import java.lang.reflect.Method;

public abstract class BaseAspect implements Proxy {

    @Override
    public final Object doProxy(ProxyChain proxyChain) throws Exception {
        Object result = null;

        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();

        begin();
        try {
            if (filter(cls, method, params)) {
                before(cls, method, params);
                result = proxyChain.doProxyChain();
                after(cls, method, params, result);
            } else {
                result = proxyChain.doProxyChain();
            }
        } catch (Exception e) {
            error(cls, method, params, e);
            throw e; // 将异常继续往上抛，抛给它的调用者
        } finally {
            end();
        }

        return result;
    }

    public void begin() {
    }

    public boolean filter(Class<?> cls, Method method, Object[] params) throws Exception {
        return true;
    }

    public void before(Class<?> cls, Method method, Object[] params) throws Exception {
    }

    public void after(Class<?> cls, Method method, Object[] params, Object result) throws Exception {
    }

    public void error(Class<?> cls, Method method, Object[] params, Exception e) {
    }

    public void end() {
    }
}
