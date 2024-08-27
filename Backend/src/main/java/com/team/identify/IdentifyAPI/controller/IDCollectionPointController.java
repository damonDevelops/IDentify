package com.team.identify.IdentifyAPI.controller;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.team.identify.IdentifyAPI.database.CardDataRepository;
import com.team.identify.IdentifyAPI.database.IDCollectionPointRepository;
import com.team.identify.IdentifyAPI.database.UserRepository;
import com.team.identify.IdentifyAPI.model.IDCollectionPoint;
import com.team.identify.IdentifyAPI.model.User;
import com.team.identify.IdentifyAPI.model.enums.ECollectionPointState;
import com.team.identify.IdentifyAPI.payload.request.EncryptedResourceRequest;
import com.team.identify.IdentifyAPI.payload.response.EncryptedResourceResponse;
import com.team.identify.IdentifyAPI.payload.response.IDCollectionPointInfoResponse;
import com.team.identify.IdentifyAPI.payload.response.MessageResponse;
import com.team.identify.IdentifyAPI.security.AuthenticatedUserService;
import com.team.identify.IdentifyAPI.security.company.CompanyGuard;
import com.team.identify.IdentifyAPI.security.services.UserDetailsImpl;
import com.team.identify.IdentifyAPI.service.IDCollectionPointService;
import com.team.identify.IdentifyAPI.util.exception.CollectionPointNotFoundError;
import com.team.identify.IdentifyAPI.util.exception.IdNotFoundError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.InvalidKeyException;

@Tag(name = "Collection Points", description = "Access the card data associated with a Collection Point")
@RestController
@RequestMapping("/collection/{companyEndpoint}/{collectionEndpoint}")
@PreAuthorize("@companyGuard.checkAccessToCollection(#companyEndpoint, #collectionEndpoint)")
public class IDCollectionPointController {

    private final IDCollectionPointRepository pointRepository;
    private final IDCollectionPointService pointService;
    private final AuthenticatedUserService authenticatedUserService;
    private final UserRepository userRepository;
    private final CompanyGuard companyGuard;
    private final CardDataRepository cardDataRepository;

    public IDCollectionPointController(
            IDCollectionPointRepository pointRepository,
            IDCollectionPointService pointService,
            AuthenticatedUserService authenticatedUserService,
            UserRepository userRepository,
            CompanyGuard companyGuard, CardDataRepository cardDataRepository) {
        this.pointRepository = pointRepository;
        this.pointService = pointService;
        this.authenticatedUserService = authenticatedUserService;
        this.userRepository = userRepository;
        this.companyGuard = companyGuard;
        this.cardDataRepository = cardDataRepository;
    }

    @Operation(
            summary = "Retrieve submitted card data from a collection endpoint",
            description = "Responses are paginated and a page number starting from from number 1 is required"
    )
    @PostMapping("/all")
    public ResponseEntity<EncryptedResourceResponse> retrieveRowsPaginated(
            @Parameter(description = "The company's unique endpoint") @PathVariable String companyEndpoint,
            @Parameter(description = "The collection's points endpoint") @PathVariable String collectionEndpoint,
            @RequestBody EncryptedResourceRequest request) throws IdNotFoundError, InvalidKeyException {
        IDCollectionPoint cp = pointRepository.getIDCollectionPointByEndpointAndCompany_CompanyEndpoint(collectionEndpoint,
                companyEndpoint).orElseThrow(
                () -> new CollectionPointNotFoundError(collectionEndpoint)
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) authenticatedUserService.getAuthentication().getPrincipal();

        // get is OK, they have to exist because user is authenticated
        User user = userRepository.findByUsername(userDetails.getUsername()).get();

        ImmutablePair<ArrayNode, Integer> output = pointService.retrievePagedRows(user,
                request.getPassword(), cp.getId(), request.getPageNumber() - 1);

        return ResponseEntity.ok(new EncryptedResourceResponse(
                request.getPageNumber(),
                output.right,
                output.left
                ));

    }

    @Operation(
            summary = "Sets the state of an ID collection point",
            description = "Takes a state from ECollectionPointState enum and sets it on the collection point"
    )
    @PutMapping("/state")
    public ResponseEntity<MessageResponse> updateIDCollectionPointState(
            @Parameter(description = "The company's unique endpoint") @PathVariable String companyEndpoint,
            @Parameter(description = "The collection's points endpoint") @PathVariable String collectionEndpoint,
            @RequestParam String state
    ){
        // gets the collection point
        IDCollectionPoint cp = pointRepository.getIDCollectionPointByEndpointAndCompany_CompanyEndpoint(collectionEndpoint,
                companyEndpoint).orElseThrow(
                () -> new CollectionPointNotFoundError(collectionEndpoint)
        );

        // this will assign the enum to its type based off the string, otherwise it'll throw an error if it doesn't exist
        ECollectionPointState cpState = ECollectionPointState.valueOf(state);

        // sets the new state and saves the collection point
        cp.setState(cpState);
        pointRepository.save(cp);

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Collection point status successfully set to " + state));
    }

    @Operation(
            summary = "Gets the state of an ID collection point",
            description = "Returns a state from ECollectionPointState enum for specific ID collection point"
    )
    @GetMapping("/state")
    public ECollectionPointState getIDCollectionPointState(
            @Parameter(description = "The company's unique endpoint") @PathVariable String companyEndpoint,
            @Parameter(description = "The collection's points endpoint") @PathVariable String collectionEndpoint
    ){
        IDCollectionPoint cp = pointRepository.getIDCollectionPointByEndpointAndCompany_CompanyEndpoint(collectionEndpoint,
                companyEndpoint).orElseThrow(
                () -> new CollectionPointNotFoundError(collectionEndpoint)
        );


        return cp.getState();
    }

    @Operation(
            summary = "Get basic information about an ID collection point",
            description = "Returns url, number of submissions and an access list (if admin)"
    )
    @GetMapping("/")
    public ResponseEntity<IDCollectionPointInfoResponse> getInfo(
            @Parameter(description = "The company's unique endpoint") @PathVariable String companyEndpoint,
            @Parameter(description = "The collection's points endpoint") @PathVariable String collectionEndpoint) {

        IDCollectionPoint cp = pointRepository.getIDCollectionPointByEndpointAndCompany_CompanyEndpoint(collectionEndpoint,
                companyEndpoint).orElseThrow(
                () -> new CollectionPointNotFoundError(collectionEndpoint)
        );

        String submissionUrl = "/submit/" + companyEndpoint + "/" + collectionEndpoint;
        String viewUrl = "/collection/" + companyEndpoint + "/" + collectionEndpoint + "/all";

        IDCollectionPointInfoResponse response =
                new IDCollectionPointInfoResponse(
                        viewUrl, submissionUrl, pointService.getNumberOfResults(cp));

        if (companyGuard.checkPermission(cp.getCompany().getId(), "EDIT_COMPANY")) {
            response.setUserAccessList(cp.getAccessList());
        }

        return ResponseEntity.ok(response);

    }

    @Operation(
            summary = "Delete an ID collection point",
            description = "Will delete collection point and any linked Card Data"
    )
    @DeleteMapping("/delete")
    public ResponseEntity<MessageResponse> requestCollectionPointDeletion(
            @Parameter(description = "The company's unique endpoint") @PathVariable String companyEndpoint,
            @Parameter(description = "The collection's points endpoint") @PathVariable String collectionEndpoint) {

        // gets collection point
        IDCollectionPoint cp = pointRepository.getIDCollectionPointByEndpointAndCompany_CompanyEndpoint(collectionEndpoint,
                companyEndpoint).orElseThrow(
                () -> new CollectionPointNotFoundError(collectionEndpoint)
        );

        // calls point service to delete collection point
        if(!pointService.deleteIDCollectionPoint(cp)) return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse("Issue when deleting collection point"));

        return ResponseEntity.ok(new MessageResponse(cp.getName() + " has been successfully deleted"));

    }

}
