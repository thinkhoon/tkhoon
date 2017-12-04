package com.tkhoon.framework.helper;

import com.tkhoon.framework.annotation.Table;
import com.tkhoon.framework.util.CollectionUtil;
import com.tkhoon.framework.util.MapUtil;
import com.tkhoon.framework.util.PropsUtil;
import com.tkhoon.framework.util.StringUtil;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLHelper {

    private static final Logger logger = LoggerFactory.getLogger(SQLHelper.class);

    private static final Properties sqlProps = PropsUtil.loadProps("sql.properties");

    public static String getSQL(String key) {
        String value = "";
        if (sqlProps.containsKey(key)) {
            value = sqlProps.getProperty(key);
        } else {
            logger.error("无法在 sql.properties 文件中获取属性：" + key);
        }
        return value;
    }

    public static String generateSelectSQL(Class<?> cls, String condition, String sort) {
        StringBuilder sql = new StringBuilder("select * from ").append(getTable(cls));
        sql.append(generateWhere(condition));
        sql.append(generateOrder(sort));
        return sql.toString();
    }

    public static String generateInsertSQL(Class<?> cls, Collection<String> fieldNames) {
        StringBuilder sql = new StringBuilder("insert into ").append(getTable(cls));
        if (CollectionUtil.isNotEmpty(fieldNames)) {
            int i = 0;
            StringBuilder columns = new StringBuilder(" ");
            StringBuilder values = new StringBuilder(" values ");
            for (String fieldName : fieldNames) {
                String columnName = StringUtil.camelhumpToUnderline(fieldName);
                if (i == 0) {
                    columns.append("(").append(columnName);
                    values.append("(?");
                } else {
                    columns.append(", ").append(columnName);
                    values.append(", ?");
                }
                if (i == fieldNames.size() - 1) {
                    columns.append(")");
                    values.append(")");
                }
                i++;
            }
            sql.append(columns).append(values);
        }
        return sql.toString();
    }

    public static String generateDeleteSQL(Class<?> cls, String condition) {
        StringBuilder sql = new StringBuilder("delete from ").append(getTable(cls));
        sql.append(generateWhere(condition));
        return sql.toString();
    }

    public static String generateUpdateSQL(Class<?> cls, Map<String, Object> fieldMap, String condition) {
        StringBuilder sql = new StringBuilder("update ").append(getTable(cls));
        if (MapUtil.isNotEmpty(fieldMap)) {
            sql.append(" set ");
            int i = 0;
            for (Map.Entry<String, ?> fieldEntry : fieldMap.entrySet()) {
                String columnName = StringUtil.camelhumpToUnderline(fieldEntry.getKey());
                if (i == 0) {
                    sql.append(columnName).append(" = ?");
                } else {
                    sql.append(", ").append(columnName).append(" = ?");
                }
                i++;
            }
        }
        sql.append(generateWhere(condition));
        return sql.toString();
    }

    public static String generateSelectSQLForCount(Class<?> cls, String condition) {
        StringBuilder sql = new StringBuilder("select count(*) from ").append(getTable(cls));
        sql.append(generateWhere(condition));
        return sql.toString();
    }

    public static String generateSelectSQLForPager(int pageNumber, int pageSize, Class<?> cls, String condition, String sort) {
        StringBuilder sql = new StringBuilder();
        String table = getTable(cls);
        String where = generateWhere(condition);
        String order = generateOrder(sort);
        String dbType = DBHelper.getDBType();
        if (dbType.equalsIgnoreCase("mysql")) {
            int pageStart = (pageNumber - 1) * pageSize;
            appendSQLForMySQL(sql, table, where, order, pageStart, pageSize);
        } else if (dbType.equalsIgnoreCase("oracle")) {
            int pageStart = (pageNumber - 1) * pageSize + 1;
            int pageEnd = pageStart + pageSize;
            appendSQLForOracle(sql, table, where, order, pageStart, pageEnd);
        } else if (dbType.equalsIgnoreCase("mssql")) {
            int pageStart = (pageNumber - 1) * pageSize;
            appendSQLForSQLServer(sql, table, where, order, pageStart, pageSize);
        }
        return sql.toString();
    }

    private static String getTable(Class<?> cls) {
        String tableName;
        if (cls.isAnnotationPresent(Table.class)) {
            tableName = cls.getAnnotation(Table.class).value();
        } else {
            tableName = StringUtil.camelhumpToUnderline(cls.getSimpleName());
        }
        return tableName;
    }

    private static String generateWhere(String condition) {
        String where = "";
        if (StringUtil.isNotEmpty(condition)) {
            where += " where " + condition;
        }
        return where;
    }

    private static String generateOrder(String sort) {
        String order = "";
        if (StringUtil.isNotEmpty(sort)) {
            order += " order by " + sort;
        }
        return order;
    }

    private static void appendSQLForMySQL(StringBuilder sql, String table, String where, String order, int pageStart, int pageEnd) {
        /*
            select * from 表名 where 条件 order by 排序 limit 开始位置, 结束位置
         */
        sql.append("select * from ").append(table);
        sql.append(where);
        sql.append(order);
        sql.append(" limit ").append(pageStart).append(", ").append(pageEnd);
    }

    private static void appendSQLForOracle(StringBuilder sql, String table, String where, String order, int pageStart, int pageEnd) {
        /*
            select a.* from (
                select rownum rn, t.* from 表名 t where 条件 order by 排序
            ) a
            where a.rn >= 开始位置 and a.rn < 结束位置
        */
        sql.append("select a.* from (select rownum rn, t.* from ").append(table).append(" t");
        sql.append(where);
        sql.append(order);
        sql.append(") a where a.rn >= ").append(pageStart).append(" and a.rn < ").append(pageEnd);
    }

    private static void appendSQLForSQLServer(StringBuilder sql, String table, String where, String order, int pageStart, int pageEnd) {
        /*
            select top 结束位置 * from 表名 where 条件 and id not in (
                select top 开始位置 id from 表名 where 条件 order by 排序
            ) order by 排序
        */
        sql.append("select top ").append(pageEnd).append(" * from ").append(table);
        if (StringUtil.isNotEmpty(where)) {
            sql.append(where).append(" and ");
        } else {
            sql.append(" where ");
        }
        sql.append("id not in (select top ").append(pageStart).append(" id from ").append(table);
        sql.append(where);
        sql.append(order);
        sql.append(") ").append(order);
    }
}
