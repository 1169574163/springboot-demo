package com.hushijie.springboottest.common.config.mybatis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * MybatisPlusPropertis
 *
 * @author wubin email: wubin@hushijie.com.cn
 * @date 2018/10/29
 */
@Component
@ConfigurationProperties(prefix = "mybatis-plus")
public class MybatisPlusPropertis {
    private ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    private String configLocation;

    private String mapperLocations;

    public MybatisPlusPropertis() {
    }

    public MybatisPlusPropertis(
            ResourcePatternResolver resolver, String configLocation,
            String mapperLocations) {
        this.resolver = resolver;
        this.configLocation = configLocation;
        this.mapperLocations = mapperLocations;
    }

    /**
     * 解析mybatis的配置文件
     *
     * @return Resource
     */
    public Resource resolveConfigLocation() {
        Resource resource = null;
        if (configLocation != null && !"".equals(configLocation)) {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            try {
                Resource[] resources = resolver.getResources(configLocation);
                if (resources != null && resources.length > 0) {
                    resource = resources[0];
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resource;
    }

    /**
     * 解析mybatis的Mapper配置文件
     *
     * @return Resource[]
     */
    public Resource[] resolveMapperLocations() {
        Resource[] resources = null;
        if (mapperLocations != null && !"".equals(mapperLocations)) {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            try {
                resources = resolver.getResources(mapperLocations);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resources;
    }

    //getter setter

    public ResourcePatternResolver getResolver() {
        return resolver;
    }

    public void setResolver(ResourcePatternResolver resolver) {
        this.resolver = resolver;
    }

    public String getConfigLocation() {
        return configLocation;
    }

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

    public String getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(String mapperLocations) {
        this.mapperLocations = mapperLocations;
    }
}
