package com.team.identify.IdentifyAPI.model;

import com.team.identify.IdentifyAPI.model.enums.ESystemRole;
import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "roles")
public class SystemRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ESystemRole name;

    @ManyToMany(mappedBy = "systemRoles", fetch = FetchType.LAZY)
    private Collection<User> users;

    public SystemRole() {

    }

    public SystemRole(ESystemRole name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ESystemRole getName() {
        return name;
    }

    public void setName(ESystemRole name) {
        this.name = name;
    }

    public Collection<User> getUsers() {
        return users;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SystemRole && ((SystemRole) obj).name == this.name;
    }

    public boolean isRole(ESystemRole role) {
        return this.name == role;
    }
}
