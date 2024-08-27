package com.team.identify.IdentifyAPI.apps.ips.messaging.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.BrokerEvent;
import org.springframework.amqp.rabbit.core.BrokerEventListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
//TODO: fix
@Controller
public class EventExchangeListener{
    private static final Logger logger = LoggerFactory.getLogger(EventExchangeListener.class);


    public BrokerEventListener brokerEventListener;

    public EventExchangeListener(ConnectionFactory cachingConnectionFactory) {
        brokerEventListener = new BrokerEventListener(cachingConnectionFactory, "connection.*");
    }


    @EventListener
    public void handleConnection(BrokerEvent event) {
        logger.info(event.toString());
    }

}
