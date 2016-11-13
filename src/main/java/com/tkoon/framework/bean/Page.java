package com.tkoon.framework.bean;

import com.tkoon.framework.base.BaseBean;

import java.util.HashMap;
import java.util.Map;

public class Page extends BaseBean {

    private String path;              // 页面路径
    private Map<String, Object> data; // 相关数据

    public Page(String path) {
        this.path = path;
        data = new HashMap<String, Object>();
    }

    public Page data(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public boolean isRedirect() {
        return path.startsWith("/");
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
