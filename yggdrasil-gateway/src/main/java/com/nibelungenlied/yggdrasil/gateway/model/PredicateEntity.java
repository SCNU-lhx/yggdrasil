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
public class PredicateEntity {

    //断言对应的Name
    private String name;

    //断言规则
    private Map<String, String> args = new LinkedHashMap<>();

}
