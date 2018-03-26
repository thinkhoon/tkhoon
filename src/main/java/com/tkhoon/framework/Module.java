package com.tkhoon.framework;

import javax.servlet.ServletContext;

public interface Module {

    void install(ServletContext context);
}
