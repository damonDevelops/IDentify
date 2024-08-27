package com.team.identify.IdentifyAPI.payload.request;

import com.fasterxml.jackson.databind.JsonNode;

public class UpdateCardInfoRequest {

    private String password;
    private String cardType;
    private JsonNode cardData;

    public UpdateCardInfoRequest() {
    }
    public UpdateCardInfoRequest(String password, String cardType, JsonNode cardData) {
        this.password = password;
        this.cardType = cardType;
        this.cardData = cardData;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public JsonNode getCardData() {
        return cardData;
    }

    public void setCardData(JsonNode cardData) {
        this.cardData = cardData;
    }
}
