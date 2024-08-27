package com.team.identify.IdentifyAPI.payload.request.messaging;

public class IPSEventMessage {
    private String type;

    private String data;

    private String ipsId;

    public IPSEventMessage(String type, String data, String ipsID) {
        this.type = type;
        this.data = data;
        this.ipsId = ipsID;
    }

    public IPSEventMessage() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getIpsId() {
        return ipsId;
    }

    public void setIpsId(String ipsID) {
        this.ipsId = ipsID;
    }
}
