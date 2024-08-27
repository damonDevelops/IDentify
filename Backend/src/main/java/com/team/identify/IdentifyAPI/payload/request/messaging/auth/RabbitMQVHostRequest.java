package com.team.identify.IdentifyAPI.payload.request.messaging.auth;

public class RabbitMQVHostRequest {
    private String username;
    private String ip;
    private String vhost;

    public RabbitMQVHostRequest(String username, String ip, String vhost) {
        this.username = username;
        this.ip = ip;
        this.vhost = vhost;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getVhost() {
        return vhost;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }
}
