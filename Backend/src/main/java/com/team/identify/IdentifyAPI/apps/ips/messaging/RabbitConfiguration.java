package com.team.identify.IdentifyAPI.apps.ips.messaging;

import com.rabbitmq.client.DefaultSaslConfig;
import com.team.identify.IdentifyAPI.config.LoadDatabase;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    @Autowired
    LoadDatabase loadDatabase; // hack to make class run after database is created


    @Autowired
    private RabbitProperties rabbitProperties;

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;


//    @Bean
//    public CachingConnectionFactory connectionFactory(AbstractConnectionFactoryConfigurer<CachingConnectionFactory> configurer) {
//        CachingConnectionFactory factory = new CachingConnectionFactory(host);
//        factory.setUsername(username);
//        factory.setPassword(password);
//        configurer.configure(factory);
//        factory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
//        return factory;
//        amqpAdmin.
//    }

    @Bean
    public RabbitTemplate rabbitTemplate(RabbitTemplateConfigurer configurer, CachingConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        configurer.configure(rabbitTemplate, connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

//    @Bean
//    public RabbitAdmin amqpAdmin(CachingConnectionFactory connectionFactory) {
//        return new RabbitAdmin(connectionFactory);
//    }


    @PostConstruct
    public void init() {
        if ( rabbitProperties.getSsl().getEnabled() && rabbitProperties.getSsl().getKeyStore() != null ) {
            cachingConnectionFactory.getRabbitConnectionFactory().setSaslConfig( DefaultSaslConfig.EXTERNAL );
        }
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
