package com.team.identify.IdentifyAPI.controller;

import com.team.identify.IdentifyAPI.payload.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {
    @Operation(
            summary = "Check to see if the API is up"
    )
    @GetMapping("/ping")
    MessageResponse ping() {
        return new MessageResponse("pong");
    }
}
