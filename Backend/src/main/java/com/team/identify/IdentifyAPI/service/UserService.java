package com.team.identify.IdentifyAPI.service;

import com.team.identify.IdentifyAPI.apps.crypt.pojo.UserPrivateKey;
import com.team.identify.IdentifyAPI.apps.crypt.utils.KeygenUtils;
import com.team.identify.IdentifyAPI.apps.email.pojo.VerificationEmail;
import com.team.identify.IdentifyAPI.apps.email.service.EmailService;
import com.team.identify.IdentifyAPI.database.RoleRepository;
import com.team.identify.IdentifyAPI.database.UserRepository;
import com.team.identify.IdentifyAPI.model.Company;
import com.team.identify.IdentifyAPI.model.User;
import com.team.identify.IdentifyAPI.model.enums.ESystemRole;
import com.team.identify.IdentifyAPI.payload.response.CompanyResponse;
import com.team.identify.IdentifyAPI.payload.response.UserInfoResponse;
import com.team.identify.IdentifyAPI.util.exception.TokenNotValidException;
import com.team.identify.IdentifyAPI.util.exception.UserNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.time.Instant;
import java.util.*;

@Service
public class UserService {

    public final boolean emailIsUsername;
    public final boolean requireEmailVerification;

    public final int tokenExpiryMinutes;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RoleRepository roleRepo;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${identify.user.verify_email:false}")
            boolean requireEmailVerification,
            @Value("${identify.user.email_is_username:false}")
            boolean emailIsUsername,
            @Value("${identify.user.verify_email_expiry:60}")
            int tokenExpiryMinutes,
            EmailService emailService, RoleRepository roleRepo) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailIsUsername = emailIsUsername;
        this.requireEmailVerification = requireEmailVerification;
        this.emailService = emailService;
        this.tokenExpiryMinutes = tokenExpiryMinutes;
        this.roleRepo = roleRepo;
    }

    /**
     * Creates a user without requiring verification
     * @param username
     * @param email
     * @param password
     * @return The saved User object
     */
    public User createUser(String username, String email, String password) {
        User newUser = new User(username, email, passwordEncoder.encode(password));
        initUserKeypair(newUser, password);
        userRepository.save(newUser);
        return newUser;
    }

    private void initUserKeypair(User user, String password) {
        KeyPair newKeypair = KeygenUtils.generateRsaKeypair();
        UserPrivateKey newUserPrivateKey = KeygenUtils.encryptPrivateKeyWithPassword(password, newKeypair.getPrivate());
        user.setEncryptedPrivateKey(newUserPrivateKey.getB64EncryptedKey());
        user.setPrivateKeyIv(newUserPrivateKey.getB64Iv());
        user.setPrivateKeySha256(newUserPrivateKey.getSha256());
        user.setPublicKey(Base64.getEncoder().encodeToString(newKeypair.getPublic().getEncoded()));
    }

    @Transactional
    public User createUserV2(String fullName, String email) throws MessagingException {
        User newUser = new User(fullName, email, this.tokenExpiryMinutes);
        newUser.setPassword("INVALID");
        UriComponentsBuilder uri = UriComponentsBuilder.fromPath("/ui/auth/verify");
        uri.queryParam("token", newUser.getVerificationToken().toString());
        uri.queryParam("email", newUser.getEmail());
        roleRepo.findByName(ESystemRole.ROLE_CUSTOMER).ifPresent(role -> newUser.getRoles().add(role));
        VerificationEmail templatedEmail = new VerificationEmail(newUser, uri.toUriString());
        userRepository.save(newUser);
        emailService.sendVerificationEmail(templatedEmail);
        return newUser;
    }


    /**
     * Verifies a user's email token, returns the User if valid
     * @param verificationToken the user's verificationToken
     * @param emailAddress the user's email address
     */
    public Optional<User> verifyUserEmailToken(String verificationToken, String emailAddress) {
        Optional<User> user = userRepository.findByEmail(emailAddress);
        if (user.isEmpty()) {
            return user;
        } else {
            if (Objects.equals(user.get().getVerificationToken(), UUID.fromString(verificationToken))
                    && user.get().getVerificationTokenExpiry().isAfter(Instant.now())) {
                return user;
            } else {
                return Optional.empty();
            }
        }
    }

    public boolean verifyTokenValidity(String verificationToken, String emailAddress) {
        return verifyUserEmailToken(verificationToken, emailAddress).isPresent();
    }

    public void completeUserVerification(String email, String password, String verificationToken) throws TokenNotValidException {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(email)
        );

        if (!verifyTokenValidity(verificationToken, email)) {
            throw new TokenNotValidException(verificationToken);
        }

        user.setVerificationTokenExpiry(Instant.now());

        initUserKeypair(user, password);
        user.activate();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public void changePassword(User user, String oldPassword, String newPassword) throws InvalidKeyException {
        UserPrivateKey currentPrivateKey = new UserPrivateKey(user);
        PrivateKey privateKey = KeygenUtils.decryptPrivateKeyWithPassword(oldPassword, currentPrivateKey);
        UserPrivateKey newPrivateKey = KeygenUtils.encryptPrivateKeyWithPassword(newPassword, privateKey);
        user.setEncryptedPrivateKey(newPrivateKey.getB64EncryptedKey());
        user.setPrivateKeyIv(newPrivateKey.getB64Iv());
        user.setPrivateKeySha256(newPrivateKey.getSha256());
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);
    }


    //public void addRolesToUser()


    // function collects user info as well as company info for the user.
    // to be used with the getUserInfo in UserController
    public UserInfoResponse getUserInfo(User user){

        UserInfoResponse userInfo = new UserInfoResponse();
        List<CompanyResponse> companyResponseList = new ArrayList<>();

        userInfo.setUserName(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setUserId(user.getId());

        for(Company c: user.getCompanies()){
            CompanyResponse companyResponse = new CompanyResponse();
            companyResponse.setCompanyName(c.getName());
            companyResponse.setId(c.getId());
            companyResponse.setCompanyEndpoint(c.getCompanyEndpoint());

            if(c.getCreated() == null) c.setCreated(Instant.now()); // TODO: This line can be deleted after everyone has re-created their DB
            companyResponse.setCreated(c.getCreated());

            companyResponseList.add(companyResponse);
        }

        // sort companies by ID (Get User Bug #68)
        companyResponseList.sort(
                Comparator.comparing(CompanyResponse::getCreated)
        );

        userInfo.setCompanyList(companyResponseList);

        return userInfo;
    }
}
