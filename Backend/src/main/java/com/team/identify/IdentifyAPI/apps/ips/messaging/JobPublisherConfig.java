package com.team.identify.IdentifyAPI.apps.ips.messaging;

import com.team.identify.IdentifyAPI.apps.ips.messaging.callback.JobAssignmentCallback;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobPublisherConfig {


    private final JobAssignmentCallback jobAssignmentCallback;

    public JobPublisherConfig(JobAssignmentCallback jobAssignmentCallback) {
        this.jobAssignmentCallback = jobAssignmentCallback;
    }

    @Bean
    public DirectExchange jobRpcExchange() {
        return new DirectExchange("job.rpc");
    }

    @Bean
    public Queue ackQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding jobResponseBinding(DirectExchange jobRpcExchange,
                                      Queue ackQueue) {
        return BindingBuilder.bind(ackQueue)
                .to(jobRpcExchange)
                .with("acks");
    }


    @Bean
    public AsyncRabbitTemplate jobRabbitTemplate(RabbitTemplateConfigurer configurer, CachingConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        configurer.configure(rabbitTemplate, connectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        return new AsyncRabbitTemplate(rabbitTemplate);
    }

}
