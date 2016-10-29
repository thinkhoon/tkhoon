package com.tkhoon.framework.proxy;

public interface Proxy {

    void doProxy(ProxyChain proxyChain) throws Exception;
}
