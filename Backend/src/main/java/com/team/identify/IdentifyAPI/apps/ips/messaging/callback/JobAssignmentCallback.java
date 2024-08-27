package com.team.identify.IdentifyAPI.apps.ips.messaging.callback;

import com.team.identify.IdentifyAPI.database.IPSJobRepository;
import com.team.identify.IdentifyAPI.database.IPSRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Service;

// this is not enabled at the moment, left as reference
@Service
public class JobAssignmentCallback implements RabbitTemplate.ConfirmCallback {

    private static final Logger logger = LoggerFactory.getLogger(JobAssignmentCallback.class);

    private final com.team.identify.IdentifyAPI.database.IPSJobRepository ipsJobRepository;
    private final com.team.identify.IdentifyAPI.database.IPSRepository ipsRepository;

    public Jackson2JsonMessageConverter messageConverter;

    public JobAssignmentCallback(Jackson2JsonMessageConverter producerJackson2MessageConverter, IPSJobRepository ipsJobRepository, IPSRepository ipsRepository) {
        this.messageConverter = producerJackson2MessageConverter;
        this.ipsJobRepository = ipsJobRepository;
        this.ipsRepository = ipsRepository;
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (!ack) {
            // handle nack
        }

    }
}
