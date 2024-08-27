package com.team.identify.IdentifyAPI.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.team.identify.IdentifyAPI.database.RoleRepository;
import com.team.identify.IdentifyAPI.database.UserRepository;
import com.team.identify.IdentifyAPI.model.SystemRole;
import com.team.identify.IdentifyAPI.model.User;
import com.team.identify.IdentifyAPI.model.enums.ESystemRole;
import com.team.identify.IdentifyAPI.payload.request.RoleRequest;
import com.team.identify.IdentifyAPI.payload.request.SignupRequest;
import com.team.identify.IdentifyAPI.payload.response.MessageResponse;
import com.team.identify.IdentifyAPI.service.UserService;
import com.team.identify.IdentifyAPI.util.exception.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Internals", description = "Internal functions for superuser use")
@RestController
@PreAuthorize("hasRole('SUPERUSER')")
@RequestMapping("/admin")
public class AdminController {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;
    private final UserService userService;

    AdminController(UserRepository repo, RoleRepository roleRepo, UserService userService) {
        this.userRepository = repo;
        this.roleRepository = roleRepo;
        this.userService = userService;
    }

    @Operation(summary = "Returns all users that exist in the system.")
    @GetMapping("/user/")
    List<User> all() {
        return userRepository.findAll();
    }

    @Operation(summary = "Gets user details by their ID")
    @GetMapping("/user/{id}/")
    User one(@Parameter(description = "User's ID") @PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Operation(summary = "Returns all available roles in the system.")
    @GetMapping("/user/roles")
    ESystemRole[] getAllRoles() {
        return ESystemRole.values();
    }

    @Operation(
            summary = "Replace a user's roles",
            description = "Provide a RoleRequest with the user's new roles, all roles will be replaced with the ones provided"
    )
    @PostMapping("/user/{id}/roles")
    MessageResponse setUserRoles(
            @Parameter(description = "Target user id") @PathVariable Long id,
            @Parameter(description = "The user's new roles") @Valid @RequestBody RoleRequest request) {
        List<ESystemRole> givenRoles = request.getRoles();
        User givenUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        givenUser.getRoles().clear();
        for (ESystemRole role : givenRoles) {
            SystemRole unpackedRole = roleRepository.findByName(role)
                    .orElseThrow(() -> new RuntimeException("Role was not found but we have validation, is there something wrong?"));
            givenUser.getRoles().add(unpackedRole);
        }

        userRepository.save(givenUser);

        return new MessageResponse("Added roles " + givenRoles + " to user " + givenUser.getUsername());
    }

    @PostMapping("/user/{id}/password")
    MessageResponse setUserPassword(@PathVariable Long id, @RequestBody JsonNode request) {
        return new MessageResponse("Feature currently disabled");
        //TODO: update to use UserService
    }

    @Operation(
            summary = "Adds a user without any email verification.",
            description = "Provide a SignupRequest with the Username, Password and Email populated. The user will be created without any roles"
    )
    @PostMapping("/user/new")
    ResponseEntity<User> addNewUser(@Validated @RequestBody SignupRequest user) {
        User new_user = userService.createUser(user.getUsername(), user.getEmail(), user.getPassword());
        return ResponseEntity.ok(new_user);
    }

}
