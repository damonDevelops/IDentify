package com.team.identify.IdentifyAPI.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.team.identify.IdentifyAPI.apps.crypt.pojo.UserKeyPair;
import com.team.identify.IdentifyAPI.apps.crypt.pojo.UserPrivateKey;
import com.team.identify.IdentifyAPI.model.enums.ECompanyRole;
import com.team.identify.IdentifyAPI.model.enums.ESystemRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.security.PublicKey;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Schema(description = "The User object")
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "userName"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {

    @JsonIgnore
    @ManyToMany(mappedBy = "employees", fetch = FetchType.LAZY)
    private final Set<Company> companies = new HashSet<>();

    @Schema(
            description = "Primary key (auto-generated)"
    )
    @Id
    @GeneratedValue
    private Long id;

    @Schema(
            description = "Username, must be unique"
    )
    @NotBlank
    @Size(max = 75)
    private String username;

    @Schema(
            description = "Email, must be unique"
    )
    @NotBlank
    @Size(max = 75)
    @Email
    private String email;

    @Schema(
            description = "The user's full name"
    )
    @Size(max=100)
    private String fullName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Size(max = 120)
    private String password;

    @Schema(
            description = "User creation timestamp"
    )
    private OffsetDateTime creationDate;

    @Schema(
            description = "Last login timestamp"
    )
    private OffsetDateTime lastSeen;

    @JsonIgnore
    private UUID verificationToken;

    @JsonIgnore
    private Instant verificationTokenExpiry;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<SystemRole> systemRoles = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "assignedUsers", fetch = FetchType.LAZY)
    private Set<CompanyRole> companyRoles = new HashSet<>();

    @JsonIgnore
    @Column(columnDefinition = "text")
    private String publicKey;

    @JsonIgnore
    @Column(columnDefinition = "text")
    private String encryptedPrivateKey;

    @JsonIgnore
    @Column(columnDefinition = "text")
    private String privateKeyIv;

    @JsonIgnore
    @Column(columnDefinition = "text")
    private String privateKeySha256;

    private Boolean active;

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.creationDate = OffsetDateTime.now(ZoneOffset.UTC);
        this.active = true;
    }

    public User(String fullName, String email, int tokenExpiryMinutes) {
        this.email = email;
        this.fullName = fullName;
        this.username = email;
        this.creationDate = OffsetDateTime.now(ZoneOffset.UTC);
        this.active = false;
        this.verificationToken = UUID.randomUUID();
        this.verificationTokenExpiry = Instant.now().plus(tokenExpiryMinutes, ChronoUnit.MINUTES);
    }

    @JsonIgnore
    public Set<CompanyRole> getCompanyRoles() {
        return companyRoles;
    }

    public PublicKey getPublicKey() {
        return UserKeyPair.getPublicKeyFromBase64(this.publicKey);
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getEncryptedPrivateKey() {
        return encryptedPrivateKey;
    }

    public void setEncryptedPrivateKey(String encryptedPrivateKey) {
        this.encryptedPrivateKey = encryptedPrivateKey;
    }

    public void setCompanyRoles(Set<CompanyRole> companyRoles) {
        this.companyRoles = companyRoles;
    }

    public String getPrivateKeyIv() {
        return privateKeyIv;
    }

    public void setPrivateKeyIv(String privateKeyIv) {
        this.privateKeyIv = privateKeyIv;
    }

    public String getPrivateKeySha256() {
        return privateKeySha256;
    }

    public void setPrivateKeySha256(String privateKeySha256) {
        this.privateKeySha256 = privateKeySha256;
    }

    @JsonIgnore
    public UserPrivateKey getUserPrivateKey() {
        return new UserPrivateKey(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public Set<SystemRole> getRoles() {
        return systemRoles;
    }

    public void setRoles(Set<SystemRole> systemRoles) {
        this.systemRoles = systemRoles;
    }

    public OffsetDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(OffsetDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public OffsetDateTime getLastSeen() {
        return lastSeen;
    }

    public Set<Company> getCompanies() {return companies;}

    public void setLastSeen(OffsetDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }

    public Instant getVerificationTokenExpiry() {
        return verificationTokenExpiry;
    }

    public void setVerificationTokenExpiry(Instant verificationTokenExpiry) {
        this.verificationTokenExpiry = verificationTokenExpiry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof User user))
            return false;
        return Objects.equals(this.id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.username, this.email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + systemRoles +
                '}';
    }

    @JsonGetter("systemRoles")
    protected List<String> getListSystemRoleNames() {
        ArrayList<String> roleNames = new ArrayList<>();
        for (SystemRole r : this.systemRoles) {
            roleNames.add(r.getName().toString());
        }
        return roleNames;
    }

    @JsonGetter("companyRoles")
    protected HashMap<String, List<String>> getListCompanyRoleNames() {
        HashMap<String, List<String>> companiesAndRoles = new HashMap<>();
        for (CompanyRole r : this.companyRoles) {
            companiesAndRoles.putIfAbsent(r.getCompany().getName(), new ArrayList<>());
            companiesAndRoles.get(r.getCompany().getName()).add(r.getName().toString());
        }
        return companiesAndRoles;
    }

    public boolean hasCompanyRole(Company company, ECompanyRole role){

        HashMap<String, List<String>> companyRoleList = getListCompanyRoleNames();

        if(companyRoleList.containsKey(company.getName())){
            return companyRoleList.get(company.getName()).contains(role.toString());
        }
        return false;
    }

    public boolean isSuperUser() {
        boolean result = false;
        for (SystemRole role : systemRoles) {
            result = role.isRole(ESystemRole.ROLE_SUPERUSER);
            if (result) {
                break;
            }
        }
        return result;
    }

    public void inactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public Boolean isActive() {
        return active;
    }

    public UUID getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(UUID verificationToken) {
        this.verificationToken = verificationToken;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
