package com.team.identify.IdentifyAPI.config;

import com.team.identify.IdentifyAPI.database.*;
import com.team.identify.IdentifyAPI.model.Authority;
import com.team.identify.IdentifyAPI.model.SystemRole;
import com.team.identify.IdentifyAPI.model.User;
import com.team.identify.IdentifyAPI.model.enums.EAuthority;
import com.team.identify.IdentifyAPI.model.enums.ESystemRole;
import com.team.identify.IdentifyAPI.service.CompanyService;
import com.team.identify.IdentifyAPI.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class LoadDatabase implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    private final UserService userService;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    CompanyRepository companyRepo;

    @Autowired
    AuthorityRepository authorityRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    CompanyRoleRepository companyRoleRepo;

    @Autowired
    PasswordEncoder encode;
    @Autowired
    private CompanyService companyService;

    public LoadDatabase(UserService userService) {
        this.userService = userService;
    }


    @Override //TODO:Convert to join queries
    public void run(String... args) throws Exception {

        //PERMISSION JOBS - MUST REMAIN

        //generate role values
        for (ESystemRole e : ESystemRole.values()) {
            Optional<SystemRole> result = roleRepo.findByName(e);
            if (result.isEmpty())
                roleRepo.save(new SystemRole(e));
        }

        //generate authorities

        for (EAuthority ea : EAuthority.values()) {
            Optional<Authority> result = authorityRepo.findByName(ea);
            if (result.isEmpty())
                authorityRepo.save(new Authority(ea));
        }

        //END PERMISSION JOBS

        //generate superuser for development
        Optional<User> findSu = userRepo.findByUsername("superuser");
        if (findSu.isEmpty()) {
            User superUser = userService.createUser("superuser", "super@user.com", "superuser");
            roleRepo.findByName(ESystemRole.ROLE_SUPERUSER).ifPresent(role -> superUser.getRoles().add(role));
            userRepo.save(superUser);
        }

        //generate ips user for development
        Optional<User> findIpsUser = userRepo.findByUsername("ips_server");
        if (findIpsUser.isEmpty()) {
            User newUser = userService.createUser("ips_server", "ips@user.com", "ips");
            roleRepo.findByName(ESystemRole.ROLE_IPS_SERVER).ifPresent(role -> newUser.getRoles().add(role));
            userRepo.save(newUser);
        }

        //generate demo user
        if (userRepo.findByUsername("demo").isEmpty()) {
            User newUser = userService.createUser("demo", "demo@user.com", "demo");
            roleRepo.findByName(ESystemRole.ROLE_CUSTOMER).ifPresent(role -> newUser.getRoles().add(role));
            userRepo.save(newUser);
        }

        //generate demo company
        if (companyRepo.findByName("Demo Corp").isEmpty()) {
            Optional<User> demoUser = userRepo.findByUsername("demo");
            demoUser.ifPresent(user -> companyService.createCompany("Demo Corp", user));
        }

    }

}
