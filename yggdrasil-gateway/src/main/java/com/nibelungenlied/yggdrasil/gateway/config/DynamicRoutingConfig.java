package com.nibelungenlied.yggdrasil.gateway.config;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;

import com.nibelungenlied.yggdrasil.gateway.model.FilterEntity;
import com.nibelungenlied.yggdrasil.gateway.model.GatewayNacosProperties;
import com.nibelungenlied.yggdrasil.gateway.model.PredicateEntity;
import com.nibelungenlied.yggdrasil.gateway.model.RouteEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 此类实现了Spring Cloud Gateway + nacos 的动态路由,
 * 它实现一个Spring提供的事件推送接口ApplicationEventPublisherAware
 */
@SuppressWarnings("all")
@Slf4j
@Component
public class DynamicRoutingConfig implements ApplicationEventPublisherAware {

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    @Autowired
    private GatewayNacosProperties gatewayProperties;

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 这个方法主要负责监听Nacos的配置变化,这里先使用参数构建一个ConfigService,
     * 再使用ConfigService开启一个监听,
     * 并且在监听的方法中刷新路由信息。
     */
    @Bean
    public void refreshRouting() throws NacosException {
        //创建Properties配置类
        Properties properties = new Properties();
        System.out.println(gatewayProperties);
        //设置nacos的服务器地址,从配置类GatewayProperties中获取
        properties.put(PropertyKeyConst.SERVER_ADDR, gatewayProperties.getServerAddr());
        //设置nacos的命名空间,表示从具体的命名空间中获取配置信息,不填代表默认从public获得
        if (gatewayProperties.getNamespace() != null) {
            properties.put(PropertyKeyConst.NAMESPACE, gatewayProperties.getNamespace());
        }
        //根据Properties配置创建ConfigService类
        ConfigService configService = NacosFactory.createConfigService(properties);
        //获得nacos中已有的路由配置
        String json = configService.getConfig(gatewayProperties.getDataId(), gatewayProperties.getGroup(), 5000);
        if(json == null){
            return;
        }
        this.parseJson(json);

        //添加监听器,监听nacos中的数据修改事件
        configService.addListener(gatewayProperties.getDataId(), gatewayProperties.getGroup(), new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }

            /**
             * 用于接收远端nacos中数据修改后的回调方法
             */
            @Override
            public void receiveConfigInfo(String configInfo) {
                log.warn(configInfo);
                //获取nacos中修改的数据并进行转换
                parseJson(configInfo);
            }
        });

    }

    /**
     * 解析从nacos读取的路由配置信息(json格式)
     */
    public void parseJson(String json) {
        log.warn("从Nacos返回的路由配置(JSON格式)：" + json);
        boolean refreshGatewayRoute = JSONObject.parseObject(json).getBoolean("refreshGatewayRoute");
        if (refreshGatewayRoute) {
            List<RouteEntity> list = JSON.parseArray(JSONObject.parseObject(json).getString("routeList")).toJavaList(RouteEntity.class);
            for (RouteEntity route : list) {
                update(assembleRouteDefinition(route));
            }
        } else {
            log.warn("路由未发生变更");
        }
    }


    /**
     * 路由更新
     */
    public void update(RouteDefinition routeDefinition) {

        try {
            this.routeDefinitionWriter.delete(Mono.just(routeDefinition.getId()));
            log.warn("路由删除成功:" + routeDefinition.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        try {
            routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
            this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
            log.warn("路由更新成功:" + routeDefinition.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    /**
     * 根据自己的路由实体类生成gateway的路由定义对象
     */
    public RouteDefinition assembleRouteDefinition(RouteEntity routeEntity) {

        RouteDefinition definition = new RouteDefinition();

        // ID
        definition.setId(routeEntity.getId());

        // Predicates
        List<PredicateDefinition> pdList = new ArrayList<>();
        for (PredicateEntity predicateEntity : routeEntity.getPredicates()) {
            PredicateDefinition predicateDefinition = new PredicateDefinition();
            predicateDefinition.setArgs(predicateEntity.getArgs());
            predicateDefinition.setName(predicateEntity.getName());
            pdList.add(predicateDefinition);
        }
        definition.setPredicates(pdList);

        // Filters
        List<FilterDefinition> fdList = new ArrayList<>();
        for (FilterEntity filterEntity : routeEntity.getFilters()) {
            FilterDefinition filterDefinition = new FilterDefinition();
            filterDefinition.setArgs(filterEntity.getArgs());
            filterDefinition.setName(filterEntity.getName());
            fdList.add(filterDefinition);
        }
        definition.setFilters(fdList);

        // URI
        URI uri = UriComponentsBuilder.fromUriString(routeEntity.getUri()).build().toUri();
        definition.setUri(uri);

        return definition;
    }

}
