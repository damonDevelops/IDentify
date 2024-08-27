package com.team.identify.IdentifyAPI.security.services;

import com.team.identify.IdentifyAPI.database.UserRepository;
import com.team.identify.IdentifyAPI.model.User;
import com.team.identify.IdentifyAPI.payload.request.messaging.auth.RabbitMQResourceRequest;
import com.team.identify.IdentifyAPI.payload.request.messaging.auth.RabbitMQTopicRequest;
import com.team.identify.IdentifyAPI.payload.request.messaging.auth.RabbitMQVHostRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class RabbitMQAuthService {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQAuthService.class);

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    UserRepository userRepo;

    public String userResult(String username, String password) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            User user = userRepo.findByUsername(username).orElseThrow(RuntimeException::new);

            user.setLastSeen(OffsetDateTime.now(ZoneOffset.UTC));
            userRepo.save(user);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (user.isSuperUser())
                return "allow administrator";
            else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_IPS_SERVER")))
                return "allow";
            else
                logger.warn("Non IPS user \"{}\" just tried to authenticate!", username);
                return "deny";

        } catch (BadCredentialsException e) {
            logger.info("RabbitMQ Authentication for user {} failed. Reason: bad credentials", username);
        }
        return "deny";
    }

    public String vhostResult(RabbitMQVHostRequest vhostRequest) {
        return "allow";
    }

    public String resourceResult(RabbitMQResourceRequest resourceRequest) {
        return "allow";
    }

    public String topicResult(RabbitMQTopicRequest topicRequest) {
        return "allow";
    }


}
