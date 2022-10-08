package com.nibelungenlied.yggdrasil.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("all")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FilterEntity {

    //过滤器对应的Name
    private String name;

    //路由规则
    private Map<String, String> args = new LinkedHashMap<>();

}
