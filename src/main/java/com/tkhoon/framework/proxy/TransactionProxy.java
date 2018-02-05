package com.tkhoon.framework.proxy;

import com.tkhoon.framework.annotation.Transaction;
import com.tkhoon.framework.helper.DBHelper;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionProxy implements Proxy {

    private static final Logger logger = LoggerFactory.getLogger(TransactionProxy.class);

    private boolean isTransactional = false; // 默认不具有事务

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;
        try {
            // 获取目标方法
            Method method = proxyChain.getTargetMethod();
            // 若在目标方法上定义了 @Transaction 注解，则说明该方法具有事务
            if (method.isAnnotationPresent(Transaction.class)) {
                // 设置为具有事务
                isTransactional = true;
                // 开启事务
                DBHelper.beginTransaction();
                if (logger.isDebugEnabled()) {
                    logger.debug("[Smart] begin transaction");
                }
                // 执行操作
                result = proxyChain.doProxyChain();
                // 提交事务
                DBHelper.commitTransaction();
                if (logger.isDebugEnabled()) {
                    logger.debug("[Smart] commit transaction");
                }
            } else {
                // 执行操作
                result = proxyChain.doProxyChain();
            }
        } catch (Exception e) {
            // 判断是否具有事务
            if (isTransactional) {
                // 回滚事务
                DBHelper.rollbackTransaction();
                if (logger.isDebugEnabled()) {
                    logger.debug("[Smart] rollback transaction");
                }
            }
            logger.error("服务端运行出错！", e);
        }
        return result;
    }
}
