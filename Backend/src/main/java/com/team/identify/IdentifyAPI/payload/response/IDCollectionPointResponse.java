package com.team.identify.IdentifyAPI.payload.response;

import com.team.identify.IdentifyAPI.model.User;
import com.team.identify.IdentifyAPI.model.enums.ECollectionPointState;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

public class IDCollectionPointResponse {
    @Schema(description = "The collection point's UUID")
    private UUID id;
    @Schema(description = "The collection point's endpoint")
    private String endpoint;
    @Schema(description = "Collection point's ACL")
    private List<User> accessList;
    @Schema(description = "The owner company's name")
    private String companyName;
    @Schema(description = "The Collection points name/title")
    private String name;
    @Schema(description = "The Collection points state")
    private ECollectionPointState state;

    public IDCollectionPointResponse() {
    }

    public IDCollectionPointResponse(UUID id, String endpoint, List<User> accessList, String companyName, String name, ECollectionPointState state) {
        this.id = id;
        this.endpoint = endpoint;
        this.accessList = accessList;
        this.companyName = companyName;
        this.name = name;
        this.state = state;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public List<User> getAccessList() {
        return accessList;
    }

    public void setAccessList(List<User> accessList) {
        this.accessList = accessList;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public ECollectionPointState getState() {
        return state;
    }

    public void setState(ECollectionPointState state) {
        this.state = state;
    }
}
