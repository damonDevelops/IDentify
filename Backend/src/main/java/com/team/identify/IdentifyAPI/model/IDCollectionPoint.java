package com.team.identify.IdentifyAPI.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team.identify.IdentifyAPI.model.enums.ECollectionPointState;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"company_id", "endpoint"})
})
public class IDCollectionPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Company company;

    private String endpoint;

    private String name;

    @Enumerated(EnumType.STRING)
    private ECollectionPointState state;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "idcollection_acl",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> accessList;

    public IDCollectionPoint(Company company, String endpoint) {
        this.company = company;
        this.endpoint = endpoint;
        this.name = endpoint;
        this.state = ECollectionPointState.ACTIVE;
        this.accessList = new ArrayList<>();
    }

    public IDCollectionPoint(Company company, String endpoint, String name) {
        this.company = company;
        this.endpoint = endpoint;
        this.name = name;
        this.state = ECollectionPointState.ACTIVE;
        this.accessList = new ArrayList<>();
    }

    public IDCollectionPoint() {

    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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

    public ECollectionPointState getState() {return state;}

    public void setState(ECollectionPointState state) {this.state = state;}
}
