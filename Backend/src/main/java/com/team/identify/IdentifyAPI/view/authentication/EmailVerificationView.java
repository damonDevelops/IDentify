package com.team.identify.IdentifyAPI.view.authentication;

import com.team.identify.IdentifyAPI.model.User;
import com.team.identify.IdentifyAPI.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;

@Tag(name="User Verification")
@RequestMapping("/ui/auth/verify")
@Controller
public class EmailVerificationView {
    private final UserService userService;

    public EmailVerificationView(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String homePage(
            Model model,
            @RequestParam(value = "token", required = false, defaultValue = "")
            String token,
            @RequestParam(value = "email", required = false, defaultValue = "")
            String email
    ) {
        if (!token.isBlank() && !email.isBlank()) {
            Optional<User> user = userService.verifyUserEmailToken(token, email);
            user.ifPresent(value -> model.addAttribute("user", value));
        }
        model.addAttribute("submissionURL",
                ServletUriComponentsBuilder.fromCurrentContextPath().replacePath("/auth/verify").build().toString());
        return "auth/verify/home";
    }
}

