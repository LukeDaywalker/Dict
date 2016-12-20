package com.util;

import java.util.Properties;

/**
 * Created by LukeSkywalker on 2016/12/20.
 */
public class Util {
    public static boolean inHome() {
        return false;
    }

    public static void initHttpProxy() {
        if (!inHome()) {
            Properties prop = System.getProperties();
            prop.setProperty("http.proxySet", "true");
// 设置http访问要使用的代理服务器的地址
            prop.setProperty("http.proxyHost", "10.199.75.12");
// 设置http访问要使用的代理服务器的端口
            prop.setProperty("http.proxyPort", "8080");
//// 设置http访问要使用的代理服务器的用户名
//		prop.setProperty("http.proxyUser", "hhhh");
//// 设置http访问要使用的代理服务器的密码
//		prop.setProperty("http.proxyPassword", "hhhh");
        }
    }
}
