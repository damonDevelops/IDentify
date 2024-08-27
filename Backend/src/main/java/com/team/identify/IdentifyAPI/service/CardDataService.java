package com.team.identify.IdentifyAPI.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.identify.IdentifyAPI.apps.crypt.exceptions.PrivateKeyNotValid;
import com.team.identify.IdentifyAPI.apps.crypt.pojo.EncryptedRow;
import com.team.identify.IdentifyAPI.apps.crypt.service.EncryptionService;
import com.team.identify.IdentifyAPI.apps.crypt.utils.KeygenUtils;
import com.team.identify.IdentifyAPI.database.CardDataRepository;
import com.team.identify.IdentifyAPI.database.UserRepository;
import com.team.identify.IdentifyAPI.model.CardData;
import com.team.identify.IdentifyAPI.model.User;
import com.team.identify.IdentifyAPI.model.validators.JsonObjectValidator;
import com.team.identify.IdentifyAPI.util.exception.CardDataNotFound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CardDataService {

    CardDataRepository cardDataRepository;
    UserRepository userRepository;
    EncryptionService encryptionService;

    public CardDataService(CardDataRepository cardDataRepository, UserRepository userRepository, EncryptionService encryptionService) {
        this.cardDataRepository = cardDataRepository;
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
    }

    // function decrypts singular row of card data and returns that info as a string
    public String returnCardData(User user, String plaintextPassword, UUID jobid) throws InvalidKeyException, PrivateKeyNotValid {

        PrivateKey pKey = KeygenUtils.decryptPrivateKeyWithPassword(plaintextPassword, user.getUserPrivateKey());

        CardData card = cardDataRepository.findById(jobid).orElseThrow(() -> new CardDataNotFound(jobid.toString()));

        List<EncryptedRow> encryptedRows = new ArrayList<>();
        encryptedRows.add(card.getEncryptedCardData());

        // since we're only decrypting 1 record, we get() index 0
        return encryptionService.decryptRowsToStringList(pKey, encryptedRows).get(0);
    }

    // function to update card information
    @Transactional
    public boolean updateCardData(CardData card, JsonNode newCardData, String plaintextPassword, User user) throws PrivateKeyNotValid, InvalidKeyException, JsonProcessingException {

        // gets decrypted card data as string and converts it to Json object
        String cardInfo = returnCardData(user, plaintextPassword, card.getJobId());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode currentCardInfo = objectMapper.readTree(cardInfo);

        // validates that the fields match in both json objects
        JsonObjectValidator jsonObjectValidator = new JsonObjectValidator(currentCardInfo, newCardData);
        if(!jsonObjectValidator.compareFields()) return false;

        // encrypts the new card information and sets it on the card
        ArrayList<PublicKey> allowedKeys = new ArrayList<>();
        for(User userKeys : card.getCollectionPoint().getAccessList()){
            allowedKeys.add(userKeys.getPublicKey());
        }
        EncryptedRow row = encryptionService.encryptData(allowedKeys, newCardData.toString().getBytes(StandardCharsets.UTF_8));
        card.setEncryptedCardData(row);

        // saves the new card information
        cardDataRepository.save(card);

        return true;
    }

    // function to delete card information
    @Transactional
    public boolean deleteCardData(CardData card){

        try{
            cardDataRepository.delete(card);
        } catch (Exception e){
            return false;
        }

        return true;
    }
}
