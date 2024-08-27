package com.team.identify.IdentifyAPI.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.team.identify.IdentifyAPI.apps.crypt.exceptions.PrivateKeyNotValid;
import com.team.identify.IdentifyAPI.database.CardDataRepository;
import com.team.identify.IdentifyAPI.database.UserRepository;
import com.team.identify.IdentifyAPI.model.CardData;
import com.team.identify.IdentifyAPI.model.User;
import com.team.identify.IdentifyAPI.payload.request.EncryptedResourceRequest;
import com.team.identify.IdentifyAPI.payload.request.UpdateCardInfoRequest;
import com.team.identify.IdentifyAPI.payload.response.MessageResponse;
import com.team.identify.IdentifyAPI.security.AuthenticatedUserService;
import com.team.identify.IdentifyAPI.security.services.UserDetailsImpl;
import com.team.identify.IdentifyAPI.service.CardDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.util.UUID;

@Tag(name = "Card Data", description = "Access,Edit or delete card data with a job id")
@RestController
@RequestMapping("/card/{id}")
@PreAuthorize("@companyGuard.checkAccessToCardData(#id)")
public class CardDataController {

    private final AuthenticatedUserService authenticatedUserService;
    private final UserRepository userRepository;
    private final CardDataRepository cardDataRepository;
    private final CardDataService cardDataService;

    public CardDataController(AuthenticatedUserService authenticatedUserService, UserRepository userRepository, CardDataRepository cardDataRepository, CardDataService cardDataService) {
        this.authenticatedUserService = authenticatedUserService;
        this.userRepository = userRepository;
        this.cardDataRepository = cardDataRepository;
        this.cardDataService = cardDataService;
    }

    @Operation(
            summary = "Gets individual card information",
            description = "Uses a card UUID and users password to retrieve and return the card information"
    )
    @PostMapping("/info")
    public ResponseEntity<String> getCardInfo(@Parameter(description = "The card info's job ID") @PathVariable String id,
                                                              @RequestBody EncryptedResourceRequest request) throws PrivateKeyNotValid, InvalidKeyException {

        // gets card info from repo - error handling is in companyGuard
        CardData card = cardDataRepository.findByJobId(UUID.fromString(id));

        // gets user info
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticatedUserService.getAuthentication().getPrincipal();

        // get is OK, they have to exist because user is authenticated
        User user = userRepository.findByUsername(userDetails.getUsername()).get();

        // calls returnCardData from cardDataService
        String cardInfo = cardDataService.returnCardData(user, request.getPassword(), card.getJobId());
        return ResponseEntity.ok(cardInfo);
    }

    @Operation(
            summary = "Update card information",
            description = "Updates card information with the given info in the request body"
    )
    @PutMapping("/update")
    public ResponseEntity<MessageResponse> updateCardInformation(@Parameter(description = "The card info's job ID") @PathVariable String id,
                                                                 @RequestBody UpdateCardInfoRequest request) throws PrivateKeyNotValid, InvalidKeyException, JsonProcessingException {
        CardData card = cardDataRepository.findByJobId(UUID.fromString(id));

        // gets user info
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticatedUserService.getAuthentication().getPrincipal();

        // get is OK, they have to exist because user is authenticated
        User user = userRepository.findByUsername(userDetails.getUsername()).get();

        ObjectMapper objectMapper = new ObjectMapper();

        // creates new JsonObject without password
        ObjectNode newCardData = objectMapper.createObjectNode();
        newCardData.put("cardType", request.getCardType());
        newCardData.set("cardData", request.getCardData());

        // passes newCardData to cardDataService
        if(!cardDataService.updateCardData(card, newCardData, request.getPassword(), user)) return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Card fields do not match. Information has not been updated"));

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Card information successfully updated"));
    }

    @Operation(
            summary = "Delete card information",
            description = "Deletes card information from the database with the given job ID"
    )
    @DeleteMapping("/delete")
    public ResponseEntity<MessageResponse> requestCardDeletion(@Parameter(description = "The card info's job ID") @PathVariable String id){
        CardData card = cardDataRepository.findByJobId(UUID.fromString(id));

        if(!cardDataService.deleteCardData(card)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Failed to delete card information."));

        return ResponseEntity.ok(new MessageResponse("Card information successfully deleted."));
    }
}
