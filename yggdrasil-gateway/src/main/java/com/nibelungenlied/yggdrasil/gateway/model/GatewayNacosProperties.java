package com.nibelungenlied.yggdrasil.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@SuppressWarnings("all")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ConfigurationProperties(prefix = "gateway.nacos")
@Component
public class GatewayNacosProperties {

    private String serverAddr;
    private String dataId;
    private String namespace;
    private String group;

}
