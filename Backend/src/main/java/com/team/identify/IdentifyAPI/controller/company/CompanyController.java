package com.team.identify.IdentifyAPI.controller.company;

import com.team.identify.IdentifyAPI.apps.crypt.utils.KeygenUtils;
import com.team.identify.IdentifyAPI.database.CompanyRepository;
import com.team.identify.IdentifyAPI.database.CompanyRoleRepository;
import com.team.identify.IdentifyAPI.database.IDCollectionPointRepository;
import com.team.identify.IdentifyAPI.database.UserRepository;
import com.team.identify.IdentifyAPI.model.Company;
import com.team.identify.IdentifyAPI.model.CompanyRole;
import com.team.identify.IdentifyAPI.model.IDCollectionPoint;
import com.team.identify.IdentifyAPI.model.User;
import com.team.identify.IdentifyAPI.model.enums.ECompanyRole;
import com.team.identify.IdentifyAPI.payload.request.CompanyInviteRequest;
import com.team.identify.IdentifyAPI.payload.request.IDCollectionPointRequest;
import com.team.identify.IdentifyAPI.payload.response.IDCollectionPointResponse;
import com.team.identify.IdentifyAPI.payload.response.MessageResponse;
import com.team.identify.IdentifyAPI.security.services.UserDetailsImpl;
import com.team.identify.IdentifyAPI.service.CompanyService;
import com.team.identify.IdentifyAPI.service.IDCollectionPointService;
import com.team.identify.IdentifyAPI.util.exception.CollectionPointNotFoundError;
import com.team.identify.IdentifyAPI.util.exception.UserAlreadyInCompany;
import com.team.identify.IdentifyAPI.util.exception.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Company", description = "Company management & info, requires at least VIEW_COMPANY permissions")
@RestController
@RequestMapping("/company/{id}")
@PreAuthorize("@companyGuard.checkPermission(#id , 'VIEW_COMPANY')")
public class CompanyController {

    private final CompanyRepository companyRepo;

    private final CompanyRoleRepository companyRoleRepo;
    private final CompanyService  companyService;

    private final UserRepository userRepo;

    private final IDCollectionPointRepository collectionPointRepo;

    private final IDCollectionPointService idCollectionPointService;

    public CompanyController(
            CompanyRepository companyRepo,
            CompanyRoleRepository companyRoleRepo,
            CompanyService companyService, UserRepository userRepo, IDCollectionPointRepository collectionPointRepo,
            IDCollectionPointService idCollectionPointService
    ) {
        this.companyRepo = companyRepo;
        this.companyRoleRepo = companyRoleRepo;
        this.companyService = companyService;
        this.userRepo = userRepo;
        this.collectionPointRepo = collectionPointRepo;
        this.idCollectionPointService = idCollectionPointService;
    }
    
    
    @Operation(
            summary = "Get the details of a company"
    )
    @GetMapping("/")
    @ResponseBody
    
    public Company getCompanyDetails(
            @Parameter(description = "Company ID") 
            @PathVariable UUID id) {
        return companyRepo.getReferenceById(id);
    }

    @Operation(
            summary = "Updates the endpoint of a company",
            description = "Requires EDIT_COMPANY")
    @PostMapping("/")
    @PreAuthorize("@companyGuard.checkPermission(#id, 'EDIT_COMPANY')")
    public ResponseEntity<MessageResponse> updateCompanyEndpoint(@Parameter(description = "Company ID") @PathVariable UUID id, @Parameter String newEndpoint){

        // checks if company already exists with new endpoint
        Company checkCompany = companyRepo.findByCompanyEndpoint(newEndpoint).orElse(null);
        if(checkCompany != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Endpoint " + newEndpoint  + " is already in use"));
        }

        companyService.updateCompanyEndpoint(newEndpoint, id);


        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Endpoint " + newEndpoint  + " has been successfully updated"));

    }
    
    @Operation(
            summary = "Get all users within a company"
    )
    @GetMapping("/users/")
    @PreAuthorize("@companyGuard.checkPermission(#id, 'EDIT_COMPANY')")
    @ResponseBody
    
    public Set<User> getAllUsers(
            @Parameter(description = "Company ID") 
            @PathVariable UUID id) {
        
        return companyRepo.getReferenceById(id).getEmployees();
        
    }
    
    @Operation(
            summary = "Invite a user to a company",
            description = "Requires EDIT_COMPANY"
    )
    @PostMapping("/users/invite")
    @PreAuthorize("@companyGuard.checkPermission(#id, 'EDIT_COMPANY')")
    
    public ResponseEntity<MessageResponse> inviteUser(
            @Parameter(description = "Company ID") 
            @PathVariable UUID id, 
            @Validated @RequestBody CompanyInviteRequest request
    ) {
        
        User result = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getEmail(), "email"));
        Company company = companyRepo.getReferenceById(id);


        if (!company.getEmployees().contains(result)) {
            company.addEmployee(result);
            companyRepo.save(company);
        } else {
            throw new UserAlreadyInCompany(result);
        }

        String roleMsg = "with no roles.";

        if (Objects.nonNull(request.getRole())) {
            CompanyRole cRoleObject = company.getCompanyRoleOfType(ECompanyRole.valueOf(request.getRole()))
                    .orElseThrow(() -> 
                            new RuntimeException("Role was not found but we have validation, is there something wrong?"));
            
            cRoleObject.getAssignedUsers().add(result);
            companyRoleRepo.save(cRoleObject);
            roleMsg = "with role " + cRoleObject.getName() + ".";
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new MessageResponse(result.getUsername() + 
                        " has been successfully added to the company " + roleMsg));
    }
    
    @Operation(
            summary = "Create a new collection point",
            description = "Creates a new collection point with the given endpoint"
    )
    @PostMapping("/point")
    @PreAuthorize("@companyGuard.checkPermission(#id, 'EDIT_COMPANY')")
    public ResponseEntity<IDCollectionPointResponse> createCollectionPoint(
            @Parameter(description = "Company ID") 
            @PathVariable UUID id, 
            @RequestBody IDCollectionPoint point
    ){

        // gets company based on ID, then checks the IDCollectionPoint repo for an already existing collection point
        Company company = companyRepo.getReferenceById(id);
        
        IDCollectionPoint collectionPoint = collectionPointRepo.getIDCollectionPointByEndpointAndCompany(
                point.getEndpoint(), company).orElse(null);

        // returns conflict response if collection point already exists.
        if(collectionPoint != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        IDCollectionPoint newPoint = idCollectionPointService.createIDCollectionPoint(company, point.getEndpoint(), point.getName());
        
        return ResponseEntity.status(HttpStatus.OK).body(
                new IDCollectionPointResponse(
                        newPoint.getId(), 
                        newPoint.getEndpoint(), 
                        newPoint.getAccessList(), 
                        newPoint.getCompany().getName(),
                        newPoint.getName(),
                        newPoint.getState()));
    }
    
    @Operation(
            summary = "Get all visible collection points",
            description = "Only returns points that the authenticated principal can access"
    )
    @GetMapping("/point")
    @PreAuthorize("@companyGuard.checkPermission(#id, 'VIEW_COMPANY')")
    public ResponseEntity<List<IDCollectionPointResponse>> getIDCollectionPoints(
            @Parameter(description = "Company ID") 
            @PathVariable UUID id
    ){

        Company company = companyRepo.getReferenceById(id);
        return ResponseEntity.ok(idCollectionPointService.getAccessibleCollectionPoints(company));
    }
    
    @Operation(
            summary = "Grant the given user access to a collection point",
            description = "Requires EDIT_COMPANY and access to the point"
    )
    @Transactional
    @PostMapping("/point/user")
    @PreAuthorize("@companyGuard.checkPermission(#id, 'EDIT_COMPANY')")
    public ResponseEntity<MessageResponse> addUserToCollectionPoint(
            @Parameter(description = "Company ID") 
            @PathVariable UUID id, 
            @RequestBody IDCollectionPointRequest point, 
            Authentication auth
    ){
        UserDetailsImpl currentUserDetails = (UserDetailsImpl) auth.getPrincipal();
        
        // we know user exists, .get is fine
        User currentUser = userRepo.findByUsername(currentUserDetails.getUsername()).get();
        
        // gets company based on ID, then checks the IDCollectionPoint repo for an already existing collection point
        Company company = companyRepo.getReferenceById(id);
        IDCollectionPoint collectionPoint = collectionPointRepo.getIDCollectionPointByEndpointAndCompany(
                point.getIdCollectionPoint().getEndpoint(), company)
                .orElseThrow(() -> 
                        new CollectionPointNotFoundError(point.getIdCollectionPoint().getEndpoint()));

        // checks if the user exists
        User addUser = userRepo.findByUsername(point.getUser().getUsername()).orElse(null);
        if(addUser == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
        }

        List<User> accessList = collectionPoint.getAccessList();
        if(accessList.contains(addUser)){
            return ResponseEntity.status(HttpStatus.FOUND).body(new MessageResponse("User already exists"));
        }

        // get user's private key, adds user to accessList then reEncrypts
        // TODO: possibly look into further error handling
        try{
            accessList.add(addUser);
            PrivateKey pKey = KeygenUtils.decryptPrivateKeyWithPassword(point.getPlaintextPassword(), currentUser.getUserPrivateKey());
            collectionPointRepo.save(collectionPoint);
            idCollectionPointService.reEncryptInBackground(collectionPoint, pKey.getEncoded());
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("User added to collection point."));
    }

    @Operation(summary = "Return the users permissions for the current company",
                description = "Will return the current users list of permissions within the requested company")
    @GetMapping("/permissions")
    @PreAuthorize("@companyGuard.checkPermission(#id, 'VIEW_COMPANY')")
    public ResponseEntity<?> returnUserPermissions(@PathVariable UUID id, Authentication auth) {
        Optional<Company> company = companyRepo.findById(id);

        ArrayList<String> permissions = new ArrayList<>();

        if (company.isPresent()) {
            String authPrefix = company.get().getPermissionPrefix();
            for (GrantedAuthority ga : auth.getAuthorities()) {
                if (ga.getAuthority().contains(authPrefix))
                    permissions.add(ga.toString());
            }
        }

        // i don't think this will trigger as they need VIEW_COMPANY to access the endpoint but just in case
        if(permissions.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User does not have any permissions with this company"));

        return ResponseEntity.ok(permissions);
    }
}
