package com.tkhoon.framework.aspect;

import com.tkhoon.framework.annotation.Transaction;
import com.tkhoon.framework.base.BaseAspect;
import com.tkhoon.framework.helper.DBHelper;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionAspect extends BaseAspect {

    private static final Logger logger = LoggerFactory.getLogger(TransactionAspect.class);

    @Override
    public boolean intercept(Class<?> cls, Method method, Object[] params) {
        return method.isAnnotationPresent(Transaction.class);
    }

    @Override
    public void before(Class<?> cls, Method method, Object[] params) throws Exception {
        // 开启事务
        DBHelper.beginTransaction();
        if (logger.isDebugEnabled()) {
            logger.debug("[Begin Transaction]");
        }

        // 设置事务隔离级别
        setTransactionIsolation(method);
    }

    @Override
    public void after(Class<?> cls, Method method, Object[] params, Object result) throws Exception {
        // 提交事务
        DBHelper.commitTransaction();
        if (logger.isDebugEnabled()) {
            logger.debug("[Commit Transaction]");
        }
    }

    @Override
    public void error(Class<?> cls, Method method, Object[] params, Exception e) {
        // 回滚事务
        DBHelper.rollbackTransaction();
        if (logger.isDebugEnabled()) {
            logger.debug("[Rollback Transaction]");
        }
    }

    private void setTransactionIsolation(Method method) throws SQLException {
        // 缺省使用数据库默认隔离级别，可在 @Transaction 注解上设置特定的隔离级别
        Transaction transaction = method.getAnnotation(Transaction.class);
        int currentIsolation = transaction.isolation();
        int defaultIsolation = DBHelper.getDefaultIsolationLevel();
        if (currentIsolation != defaultIsolation) {
            Connection conn = DBHelper.getConnectionFromThreadLocal();
            conn.setTransactionIsolation(currentIsolation);
            if (logger.isDebugEnabled()) {
                logger.debug("[Set Transaction Isolation] Isolation: {}", currentIsolation);
            }
        }
    }
}
