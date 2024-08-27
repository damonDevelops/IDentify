package com.team.identify.IdentifyAPI.controller.authentication;

import com.team.identify.IdentifyAPI.payload.request.messaging.auth.RabbitMQResourceRequest;
import com.team.identify.IdentifyAPI.payload.request.messaging.auth.RabbitMQTopicRequest;
import com.team.identify.IdentifyAPI.payload.request.messaging.auth.RabbitMQUserRequest;
import com.team.identify.IdentifyAPI.payload.request.messaging.auth.RabbitMQVHostRequest;
import com.team.identify.IdentifyAPI.security.services.RabbitMQAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "RabbitMQ Auth", description = "Deprecated. Only used for dev and testing.")
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(path="/mq/auth")
public class RabbitMQAuthController {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQAuthController.class);

    @Autowired
    RabbitMQAuthService rabbitAuthService;

    @PostMapping(
            path = "/user",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public ResponseEntity<String> userRequest(RabbitMQUserRequest userRequest) {
        return ResponseEntity.ok("allow"); //TODO: this is needed for first run, make it not needed
        //return ResponseEntity.ok(rabbitAuthService.userResult(userRequest.getUsername(), userRequest.getPassword()));
    }

    @PostMapping(
            path = "/vhost",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public ResponseEntity<String> vhostRequest(RabbitMQVHostRequest vhostRequest) {
        return ResponseEntity.ok(rabbitAuthService.vhostResult(vhostRequest));
    }

    @PostMapping(
            path="/resource",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public ResponseEntity<String> resourceRequest(RabbitMQResourceRequest resourceRequest) {
        return ResponseEntity.ok(rabbitAuthService.resourceResult(resourceRequest));
    }

    @PostMapping(
            path="/topic",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public ResponseEntity<String> topicRequest(RabbitMQTopicRequest topicRequest) {
        return ResponseEntity.ok(rabbitAuthService.topicResult(topicRequest));
    }

}
