package com.nibelungenlied.yggdrasil.gateway.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("all")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RouteEntity {

    //路由id
    private String id;

    //路由断言集合
    private List<PredicateEntity> predicates = new ArrayList<>();

    //路由过滤器集合
    private List<FilterEntity> filters = new ArrayList<>();

    //路由转发的目标uri
    private String uri;

    //路由执行的顺序
    private int order = 0;

}
