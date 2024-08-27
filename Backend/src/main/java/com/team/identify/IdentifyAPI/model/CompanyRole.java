package com.team.identify.IdentifyAPI.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team.identify.IdentifyAPI.model.enums.ECompanyRole;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "company_role")
public class CompanyRole {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private ECompanyRole name;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "company_role_authorities",
            joinColumns = @JoinColumn(name = "company_role_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private List<Authority> authorityList;

    @ManyToMany
    @JoinTable(name = "company_role_users",
            joinColumns = @JoinColumn(name = "company_role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> assignedUsers;

    public CompanyRole(ECompanyRole name, Company company) {
        this.name = name;
        this.company = company;
    }

    public CompanyRole() {
    }

    //generates an authority name as [{companyRolePrefix}_{AUTHORITY}, ...]
    @JsonGetter("authorities")
    public List<String> getAuthorities() {
        //make sure each role is unique
        Set<String> authSet = new HashSet<>();
        for (Authority a : authorityList)
            authSet.add(company.getPermissionPrefix() + "_" + a.toString());

        return authSet.stream().toList();
    }

    public UUID getId() {
        return id;
    }

    public ECompanyRole getName() {
        return name;
    }

    public void setName(ECompanyRole name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Authority> getAuthorityList() {
        return authorityList;
    }

    public void setAuthorityList(List<Authority> authorityList) {
        this.authorityList = authorityList;
    }

    public List<User> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(List<User> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    public boolean isRole(ECompanyRole e) {
        return name == e;
    }
}
