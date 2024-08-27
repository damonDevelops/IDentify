package com.team.identify.IdentifyAPI.model.validators;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;

public class JsonObjectValidator {

    private final JsonNode node1;
    private final JsonNode node2;

    public JsonObjectValidator(JsonNode node1, JsonNode node2) {
        this.node1 = node1;
        this.node2 = node2;
    }

    public boolean compareFields(){
        // checks the size of both to make sure they are of the same length
        if(node1.size() != node2.size()){
            return false;
        }

        // checks the top level fields
        for (Iterator<String> it = node1.fieldNames(); it.hasNext(); ) {
            String fieldName = it.next();
            if(!node2.has(fieldName)){
                return false;
            }
        }

        // checks the fields within cardData
        for (Iterator<String> it = node1.get("cardData").fieldNames(); it.hasNext(); ) {
            String fieldName = it.next();
            if(!node2.get("cardData").has(fieldName)){
                return false;
            }
        }
        return true;
    }
}
