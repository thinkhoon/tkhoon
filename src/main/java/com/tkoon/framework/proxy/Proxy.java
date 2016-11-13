package com.tkoon.framework.proxy;

public interface Proxy {

    void doProxy(ProxyChain proxyChain) throws Exception;
}
