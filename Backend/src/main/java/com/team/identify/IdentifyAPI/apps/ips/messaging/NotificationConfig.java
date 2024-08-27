package com.team.identify.IdentifyAPI.apps.ips.messaging;

import com.team.identify.IdentifyAPI.apps.ips.messaging.publisher.NotificationSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfig {

    private static final Logger logger = LoggerFactory.getLogger(NotificationConfig.class);
    @Bean
    public FanoutExchange notificationFanout() {
        FanoutExchange exchange = new FanoutExchange("system.notification");
        exchange.shouldDeclare();
        return exchange;
    }

    @Bean
    public NotificationSender sender() {
        return new NotificationSender();
    }
}
