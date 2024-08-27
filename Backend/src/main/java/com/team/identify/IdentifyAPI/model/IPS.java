package com.team.identify.IdentifyAPI.model;

import com.team.identify.IdentifyAPI.model.enums.EIPSState;
import jakarta.persistence.*;


import java.util.UUID;

@Entity
@Table(name = "ips_server")
public class IPS {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int maxJobs;


    @Enumerated(EnumType.STRING)
    private EIPSState state;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", columnDefinition = "bigint", referencedColumnName = "id")
    private User user;

    public IPS() {}

    public IPS(int maxJobs, EIPSState state, User user) {
        this.maxJobs = maxJobs;
        this.state = state;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EIPSState getState() {
        return state;
    }

    public void setState(EIPSState state) {
        this.state = state;
    }

    public int getMaxJobs() {
        return maxJobs;
    }

    public void setMaxJobs(int maxJobs) {
        this.maxJobs = maxJobs;
    }


}
