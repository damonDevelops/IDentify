package com.team.identify.IdentifyAPI.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team.identify.IdentifyAPI.model.enums.ECompanyRole;
import com.team.identify.IdentifyAPI.util.RandomUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Schema(description = "A company/customer")
@Entity
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Schema(
            description = "The company's full name"
    )
    @NotBlank
    private String name;

    private Instant created;

    private String url;
    @Schema(
            nullable = true,
            description = "URL to company logo, max size 500x500"
    )
    private String logoImgUrl;

    @Schema(
            description = "A list of Users who are in the company"
    )
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "company_employees",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> employees = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "company")
    private Set<CompanyRole> companyRoles = new HashSet<>();

    @Schema(
            description = "The companies' owner"
    )
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Schema(
            description = "Internal use only."
    )
    private String permissionPrefix;

    @Schema(
            description = "A unique prefix that is used in company specific endpoints"
    )
    private String companyEndpoint;
    
    // chain of encrypted symmetric encryption keys
    @JsonIgnore
    private String companyKeychain;

    // b64 encoded sha-256 hash of symmetric encryption key
    @JsonIgnore
    private String companyPrivateKeyHash;

    public Company() {
    }

    public Company(String name, User owner) {
        this.name = name;
        this.owner = owner;
        this.employees.add(owner);
        this.created = Instant.now();
        this.permissionPrefix = RandomUtil.RandomString(4); //TODO: check for collide when saving
        this.companyEndpoint = permissionPrefix; //TODO: add functionality to set, must be unique
    }

    public String getCompanyKeychain() {
        return companyKeychain;
    }

    public void setCompanyKeychain(String companyKeychain) {
        this.companyKeychain = companyKeychain;
    }


    public void setCompanyPubliKkey(String companyPublicKey) {
        companyPublicKey = companyPublicKey;
    }

    public String getCompanyEndpoint() {
        return companyEndpoint;
    }

    public void setCompanyEndpoint(String companyEndpoint) {
        this.companyEndpoint = companyEndpoint;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<User> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<User> employees) {
        this.employees = employees;
    }

    public void addEmployee(User u) {
        this.employees.add(u);
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getLogoImgUrl() {
        return logoImgUrl;
    }

    public void setLogoImgUrl(String logoImgUrl) {
        this.logoImgUrl = logoImgUrl;
    }

    public String getPermissionPrefix() {
        return permissionPrefix;
    }

    public void setPermissionPrefix(String permissionPrefix) {
        this.permissionPrefix = permissionPrefix;
    }

    @JsonIgnore
    public Set<CompanyRole> getCompanyRoles() {
        return companyRoles;
    }

    public void setCompanyRoles(Set<CompanyRole> companyRoles) {
        this.companyRoles = companyRoles;
    }

    public Optional<CompanyRole> getCompanyRoleOfType(ECompanyRole cRole) {
        for (CompanyRole cRoleObject : companyRoles) {
            if (cRoleObject.isRole(cRole)) {
                return Optional.of(cRoleObject);
            }
        }
        return Optional.empty();
    }
    //TODO: Finish implementation of company entity


    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }
}
