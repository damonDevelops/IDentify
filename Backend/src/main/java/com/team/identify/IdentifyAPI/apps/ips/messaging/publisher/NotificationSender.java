package com.team.identify.IdentifyAPI.apps.ips.messaging.publisher;

import com.team.identify.IdentifyAPI.payload.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
public class NotificationSender {
    private static final Logger logger = LoggerFactory.getLogger(NotificationSender.class);

    @Autowired
    @Qualifier("rabbitTemplate")
    private RabbitTemplate template;

    @Autowired
    private FanoutExchange fanout;

    @Autowired
    public JobPublisher publisher;

    @Scheduled(fixedDelay = 10000)
    public void send() {
        String announcement = "Keepalive";
        template.convertAndSend(fanout.getName(), "", new MessageResponse(announcement));
        logger.debug("Sent keepalive notification to system.notification");
    }

}
