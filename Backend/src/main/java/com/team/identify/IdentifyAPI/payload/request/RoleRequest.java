package com.team.identify.IdentifyAPI.payload.request;

import com.team.identify.IdentifyAPI.model.enums.ESystemRole;
import com.team.identify.IdentifyAPI.model.validators.ValueOfEnum;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RoleRequest {
    @NotNull
    @ValueOfEnum(enumClass = ESystemRole.class)
    List<String> roles;

    public RoleRequest() {
    }

    public List<ESystemRole> getRoles() {
        ArrayList<ESystemRole> givenRoles = new ArrayList<>();
        for (String input : roles) {
            givenRoles.add(ESystemRole.valueOf(input));
        }
        return givenRoles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
