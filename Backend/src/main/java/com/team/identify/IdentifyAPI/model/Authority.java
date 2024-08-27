package com.team.identify.IdentifyAPI.model;

import com.team.identify.IdentifyAPI.model.enums.EAuthority;
import jakarta.persistence.*;

@Entity
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EAuthority name;

    public Authority() {
    }

    public Authority(EAuthority name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public EAuthority getName() {
        return name;
    }

    public void setName(EAuthority name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name.toString();
    }
}
