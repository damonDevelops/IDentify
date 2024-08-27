package com.team.identify.IdentifyAPI.payload.request;

import com.team.identify.IdentifyAPI.model.IDCollectionPoint;
import com.team.identify.IdentifyAPI.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO used when adding a user to an ID collection point")
public class IDCollectionPointRequest {
    
    private final IDCollectionPoint idCollectionPoint;

    private final User user;
    private final String plaintextPassword;

    public IDCollectionPointRequest(IDCollectionPoint idCollectionPoint, User user, String planttextPassword) {
        this.idCollectionPoint = idCollectionPoint;
        this.user = user;
        this.plaintextPassword = planttextPassword;
    }

    public IDCollectionPoint getIdCollectionPoint() {
        return idCollectionPoint;
    }

    public User getUser() {
        return user;
    }

    public String getPlaintextPassword() {
        return plaintextPassword;
    }
}
