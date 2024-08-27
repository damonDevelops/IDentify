package com.team.identify.IdentifyAPI.controller.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.team.identify.IdentifyAPI.database.RoleRepository;
import com.team.identify.IdentifyAPI.database.UserRepository;
import com.team.identify.IdentifyAPI.model.RefreshToken;
import com.team.identify.IdentifyAPI.model.SystemRole;
import com.team.identify.IdentifyAPI.model.User;
import com.team.identify.IdentifyAPI.model.enums.ESystemRole;
import com.team.identify.IdentifyAPI.payload.request.LoginRequest;
import com.team.identify.IdentifyAPI.payload.request.SignupRequest;
import com.team.identify.IdentifyAPI.payload.request.TokenRefreshRequest;
import com.team.identify.IdentifyAPI.payload.response.JwtResponse;
import com.team.identify.IdentifyAPI.payload.response.MessageResponse;
import com.team.identify.IdentifyAPI.payload.response.TokenRefreshResponse;
import com.team.identify.IdentifyAPI.security.jwt.JwtUtils;
import com.team.identify.IdentifyAPI.security.services.RefreshTokenService;
import com.team.identify.IdentifyAPI.security.services.UserDetailsImpl;
import com.team.identify.IdentifyAPI.service.UserService;
import com.team.identify.IdentifyAPI.util.exception.TokenRefreshException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Tag(name = "Authentication", description = "Authentication and Signup")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;
    private final AuthenticationManager authManager;

    private final UserRepository userRepo;

    private final RoleRepository roleRepo;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    public AuthController(
            UserService userService, 
            AuthenticationManager authManager, 
            UserRepository userRepo, 
            RoleRepository roleRepo,
            JwtUtils jwtUtils, 
            RefreshTokenService refreshTokenService
    ) {
        this.userService = userService;
        this.authManager = authManager;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @Operation(
            summary = "Login to the API",
            description = "Returns a JWT & refresh token to use for authentication"
    )
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            @Parameter(description = "A LoginRequest object")
            @Valid @RequestBody LoginRequest loginRequest) throws JsonProcessingException {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        Optional<User> user = userRepo.findByUsername(loginRequest.getUsername());

        // checks if it's first time login for a user by checking if their last seen is null
        // this is used for the front end to show tool tips on login
        boolean firstTimeLogin = false;
        if(user.get().getLastSeen() == null){
            firstTimeLogin = true;
        }

        user.ifPresent(value -> value
                .setLastSeen(OffsetDateTime.now(ZoneOffset.UTC)));
        user.ifPresent(userRepo::save);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        // if it's a users first time login
        // this will generate a json object and add the firstTimeLogin value to the return response
        if(firstTimeLogin){
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode jwtResponse = objectMapper.createObjectNode();

            jwtResponse.put("refreshToken", refreshToken.getToken());
            jwtResponse.put("id", userDetails.getId());
            jwtResponse.put("username", userDetails.getUsername());
            jwtResponse.put("email", userDetails.getEmail());
            ArrayNode rolesNode = jwtResponse.putArray("roles");
            rolesNode.addAll((ArrayNode) objectMapper.valueToTree(roles));

            jwtResponse.put("tokenType", "Bearer");
            jwtResponse.put("accessToken", jwt);
            jwtResponse.put("firstTimeLogin", true);


            return ResponseEntity.ok(jwtResponse);

        }

        return ResponseEntity.ok(new JwtResponse(jwt,
                refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }
    
    @Operation(
            summary = "New user signup",
            description = "Create a user with the ROLE_CUSTOMER system role. To be deprecated by sprint 3"
    )
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepo.existsByUsername(signupRequest.getUsername()))
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        if (userRepo.existsByEmail(signupRequest.getEmail()))
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Email already in use!"));

        User user = userService.createUser(signupRequest.getUsername(),
                signupRequest.getEmail(),
                signupRequest.getPassword());

        Optional<SystemRole> defaultRole = roleRepo.findByName(ESystemRole.ROLE_CUSTOMER);
        if (defaultRole.isPresent()) {
            Set<SystemRole> systemRoles = new HashSet<>();
            systemRoles.add(defaultRole.get());
            user.setRoles(systemRoles);
        }

        userRepo.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @Operation(
            summary = "User signup with email verification",
            description = "Create a user with the given full name and email"
    )
    @PostMapping("/signup/v2")
    public ResponseEntity<?> registerUserV2(
            @Parameter(description = "User's full name")
            @RequestParam(name = "fullName")
            String fullName,
            @Parameter(description = "User's email address")
            @RequestParam(name = "email")
            String email
    ) {
        try {
            userService.createUserV2(fullName, email);
            return ResponseEntity.ok().build();
        } catch (MessagingException e) {
            logger.error(e.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
            summary = "Get a new JWT using a refresh token",
            description = "Consumes a refresh token and returns new secrets"
    )
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String jwtToken = jwtUtils.generateTokenFromUsername(user.getUsername());
                    String newRefreshToken = refreshTokenService.createRefreshToken(user.getId()).getToken();
                    return ResponseEntity.ok(new TokenRefreshResponse(jwtToken,
                            newRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Your refresh token is invalid!"));
    }

    /**
     * Internal controller method for signing out on endpoint <b>/signout</b>
     * @param userDetails An authentication info of current accessing user such as username and ID
     * @return ResponseEntity<MessageResponse> - A response entity of type <b>MessageResponse</b>.
     * Checks if no CRUD operation is completed thus bad request returned as user is not signed in
     */
    @Operation(
            summary = "Sign out of the API",
            description = "Deletes all refresh tokens, makes username and password required for the next JWT request"
    )
    @GetMapping("/signout")
    public ResponseEntity<MessageResponse> signout(@AuthenticationPrincipal UserDetailsImpl userDetails){

        if(refreshTokenService.deleteByUserId(userDetails.getId()) < 1){
            return ResponseEntity.badRequest().body(new MessageResponse("User is not signed in"));
        }

        return ResponseEntity.ok(new MessageResponse("User signed out successfully"));
    }
}
