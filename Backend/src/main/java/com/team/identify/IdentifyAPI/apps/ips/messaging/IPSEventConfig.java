package com.team.identify.IdentifyAPI.apps.ips.messaging;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IPSEventConfig {

    // https://www.rabbitmq.com/tutorials/tutorial-four-spring-amqp.html
    @Bean
    public DirectExchange ipsEventExchange() {
        DirectExchange exchange = new DirectExchange("ips.events");
        exchange.setShouldDeclare(true);
        return exchange;
    }

    /**
     *
     * @return a queue that receives ips shutdown events
     */

    @Bean
    public Queue ipsShutdownEventQueue() {
        return new AnonymousQueue();
    }

    /**
     * @return a queue that receives ips startup events
     */
    @Bean
    public Queue ipsStartupEventQueue() {
        return new AnonymousQueue();
    }

    /**
     * @return queue that receives responses from notification heartbeats
     */
    @Bean
    public Queue ipsHeartbeatResponseQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding startupBinding(DirectExchange ipsEventExchange,
                                  Queue ipsStartupEventQueue) {
        return BindingBuilder.bind(ipsStartupEventQueue)
                .to(ipsEventExchange)
                .with("startup");
    }

    @Bean
    public Binding shutdownBinding(DirectExchange ipsEventExchange,
                                   Queue ipsShutdownEventQueue) {
        return BindingBuilder.bind(ipsShutdownEventQueue)
                .to(ipsEventExchange)
                .with("shutdown");
    }

    @Bean
    public Binding heartbeatBinding(DirectExchange ipsEventExchange,
                                   Queue ipsHeartbeatResponseQueue) {
        return BindingBuilder.bind(ipsHeartbeatResponseQueue)
                .to(ipsEventExchange)
                .with("heartbeat");
    }

}
