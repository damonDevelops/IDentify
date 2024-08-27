package com.team.identify.IdentifyAPI.payload.request.messaging.auth;

public class RabbitMQTopicRequest extends RabbitMQResourceRequest{
    private String routing_key;

    public RabbitMQTopicRequest(String username, String vhost, String resource, String name, String permission, String routing_key) {
        super(username, vhost, resource, name, permission);
        this.routing_key = routing_key;
    }

    public String getRouting_key() {
        return routing_key;
    }

    public void setRouting_key(String routing_key) {
        this.routing_key = routing_key;
    }
}
