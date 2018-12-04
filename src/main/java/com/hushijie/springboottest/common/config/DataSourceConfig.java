package com.hushijie.springboottest.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MybatisDBConfig
 *
 * @author wubin email: wubin@hushijie.com.cn
 * @date 2018/10/25
 */
@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceConfig {
    /**
     * 数据库 url
     */
    @Value("url")
    private String url;

    /**
     * 数据库用户名
     */
    @Value("username")
    private String username;

    /**
     * 数据库密码
     */
    @Value("password")
    private String password;

    /**
     * 数据库驱动类名
     */
    @Value("driver-class-name")
    private String driverClassName;

    public String getUrl() {
        return url;
    }

    public DataSourceConfig setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public DataSourceConfig setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DataSourceConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public DataSourceConfig setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
        return this;
    }
}
