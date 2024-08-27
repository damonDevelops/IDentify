package com.team.identify.IdentifyAPI.controller.company;

import com.team.identify.IdentifyAPI.database.CompanyRepository;
import com.team.identify.IdentifyAPI.database.UserRepository;
import com.team.identify.IdentifyAPI.model.Company;
import com.team.identify.IdentifyAPI.model.User;
import com.team.identify.IdentifyAPI.payload.request.CompanyCreationRequest;
import com.team.identify.IdentifyAPI.security.services.UserDetailsImpl;
import com.team.identify.IdentifyAPI.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Company endpoints without company ID
 */
@Tag(name = "Company")
@RestController
@RequestMapping("/company")
public class CompanyManagementController {

    private final CompanyService companyService;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyManagementController(CompanyService companyService, CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyService = companyService;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    @Operation(
            summary = "Create a new company",
            description = "Creates a new company with given name"
    )
    @PostMapping("/create")
    @ResponseBody
    public Company createCompany(
            @Parameter(description = "Company name") @RequestBody CompanyCreationRequest company) {
        String name = company.getName();
        Optional<Company> existingCompany = companyRepository.findByName(name);
        if (existingCompany.isPresent()) {
            throw new IllegalArgumentException("A company with the same name already exists");
        }
        else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
            //currently logged-in user
            User owner = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
            return companyService.createCompany(name, owner);
        }
    }
}