package com.nibelungenlied.yggdrasil.gateway.events;

import com.nibelungenlied.yggdrasil.gateway.config.BusConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class EventSender {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private MessageConverter messageConverter;
    @PostConstruct
    public void init(){
        kafkaTemplate.setMessageConverter((RecordMessageConverter) messageConverter);
    }
    public void send(String routingKey, Object object) {
        log.info("routingKey:{}=>message:{}", routingKey, object);
        kafkaTemplate.send(BusConfig.EXCHANGE_NAME,routingKey,object);
    }
}
