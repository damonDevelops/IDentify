package com.team.identify.IdentifyAPI.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.team.identify.IdentifyAPI.apps.crypt.exceptions.PrivateKeyNotValid;
import com.team.identify.IdentifyAPI.apps.crypt.pojo.EncryptedRow;
import com.team.identify.IdentifyAPI.apps.crypt.service.EncryptionService;
import com.team.identify.IdentifyAPI.apps.crypt.utils.KeygenUtils;
import com.team.identify.IdentifyAPI.database.*;
import com.team.identify.IdentifyAPI.model.*;
import com.team.identify.IdentifyAPI.model.enums.ECompanyRole;
import com.team.identify.IdentifyAPI.payload.response.IDCollectionPointResponse;
import com.team.identify.IdentifyAPI.security.services.UserDetailsImpl;
import com.team.identify.IdentifyAPI.util.exception.IdNotFoundError;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IDCollectionPointService {
    private final IDCollectionPointRepository idCollectionPointRepository;
    private final UserRepository userRepository;
    private final CompanyRoleRepository companyRoleRepository;
    private final CardDataRepository cardDataRepository;
    private final EncryptionService encryptionService;
    private final JobScheduler jobScheduler;
    private final IPSJobRepository ipsJobRepository;

    public IDCollectionPointService(
            IDCollectionPointRepository idCollectionPointRepository,
            UserRepository userRepository, 
            CompanyRoleRepository companyRoleRepository, 
            CardDataRepository cardDataRepository, 
            EncryptionService encryptionService, 
            JobScheduler jobScheduler,
            IPSJobRepository ipsJobRepository) {
        this.idCollectionPointRepository = idCollectionPointRepository;
        this.userRepository = userRepository;
        this.companyRoleRepository = companyRoleRepository;
        this.cardDataRepository = cardDataRepository;
        this.encryptionService = encryptionService;
        this.jobScheduler = jobScheduler;
        this.ipsJobRepository = ipsJobRepository;
    }

    public IDCollectionPoint createIDCollectionPoint(Company company, String endpoint, String endpointName) {
        IDCollectionPoint defaultCollectionPoint = new IDCollectionPoint(company, endpoint, endpointName);

        defaultCollectionPoint.getAccessList().add(company.getOwner());
        for (User u : userRepository.findUsersByCompanyRolesContains(
                companyRoleRepository.getCompanyRoleByNameAndCompany(ECompanyRole.ADMINISTRATOR, company))) {
            defaultCollectionPoint.getAccessList().add(u);
        }
        idCollectionPointRepository.save(defaultCollectionPoint);
        return defaultCollectionPoint;
    }

    public ImmutablePair<ArrayNode, Integer> retrievePagedRows(User user,
                                                                  String plaintextPassword,
                                                                  UUID collectionPointId,
                                                                  int pageNum)
            throws IdNotFoundError,
            InvalidKeyException {

        // get user's private key
        PrivateKey pKey = KeygenUtils.decryptPrivateKeyWithPassword(plaintextPassword, user.getUserPrivateKey());

        IDCollectionPoint cp = idCollectionPointRepository.findById(collectionPointId).orElseThrow(
                () -> new IdNotFoundError(collectionPointId.toString())
        );

        Pageable pageSize50 = PageRequest.of(pageNum, 50);
        Page<CardData> rows = cardDataRepository.findAllByCollectionPoint(cp, pageSize50);

        // transform card data list to list of encrypted rows
        List<EncryptedRow> encryptedRows = new ArrayList<>();
        for (CardData row : rows.getContent()) {
            encryptedRows.add(row.getEncryptedCardData());
        }

        // Decrypts rowData and converts it to ArrayNode
        // then adds card id's to the nodes
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode result = mapper.createArrayNode();
        try {
            List<String> rowData = encryptionService.decryptRowsToStringList(pKey, encryptedRows);


            // iterates through rows, generating node and adding ID's individually
            for(int i = 0; i< rowData.size(); i++){
                String row = rowData.get(i);
                try{
                    JsonNode rowNode = mapper.readValue(row, JsonNode.class);

                    ObjectNode modifiedRow = mapper.createObjectNode();
                    modifiedRow.put("id", rows.getContent().get(i).getJobId().toString());

                    modifiedRow.setAll((ObjectNode) rowNode);

                    result.add(modifiedRow);

                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }

            return new ImmutablePair<>(result, rows.getTotalPages());
        } catch (PrivateKeyNotValid e) {
            //TODO: SPRINT 2: Mark row for re-encryption if user should have access
        }
        throw new InvalidKeyException("Key provided was not valid, contact your identify administrator for assistance.");
    }

    public int getNumberOfResults(IDCollectionPoint point) {
        return cardDataRepository.countAllByCollectionPointIs(point);
    }

    //TODO: make run in background
    public void reEncryptPoint(IDCollectionPoint idAccessPoint, byte[] privateKey) throws PrivateKeyNotValid {
        try {
            PKCS8EncodedKeySpec bobPubKeySpec = new PKCS8EncodedKeySpec(privateKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privKey = keyFactory.generatePrivate(bobPubKeySpec);
            for (CardData data : cardDataRepository.findAllByCollectionPoint(idAccessPoint, Pageable.unpaged())) {
                EncryptedRow er = encryptionService.reEncryptRow(privKey, userRepository.findUsersByCollectionPoint
                        (idAccessPoint).stream().map(User::getPublicKey).toList(), data.getEncryptedCardData());

                data.setEncryptedCardData(er);

                cardDataRepository.save(data);

            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ignored) {

        }
    }

    public void reEncryptInBackground(IDCollectionPoint idCollectionPoint, byte[] privateKey) {
        jobScheduler.enqueue(() -> reEncryptPoint(idCollectionPoint, privateKey));
    }

    public List<IDCollectionPointResponse> getAccessibleCollectionPoints(Company company){
        List<IDCollectionPoint> collectionPoints = idCollectionPointRepository.getAllCollectionPointsByCompany(company);
        List<IDCollectionPointResponse> accessiblePoints = new ArrayList<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();

        User currentUser = userRepository.getReferenceById(user.getId());

        for (IDCollectionPoint collectionPoint : collectionPoints){
            IDCollectionPointResponse filteredPoint = new IDCollectionPointResponse();
            filteredPoint.setEndpoint(collectionPoint.getEndpoint());
            filteredPoint.setId(collectionPoint.getId());
            filteredPoint.setCompanyName(collectionPoint.getCompany().getName());
            filteredPoint.setName(collectionPoint.getName());
            filteredPoint.setState(collectionPoint.getState());

            List<User> checkUsers = collectionPoint.getAccessList();

            if(currentUser.hasCompanyRole(company, ECompanyRole.OWNER) || currentUser.hasCompanyRole(company, ECompanyRole.ADMINISTRATOR)){
                filteredPoint.setAccessList(checkUsers);
            } else if (!checkUsers.contains(currentUser)) {
                continue;
            }

            accessiblePoints.add(filteredPoint);
        }

        return accessiblePoints;
    }


    // function to delete ID collection point and all related Card Data or pending Job requests
    // will roll back DB if exception is thrown
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteIDCollectionPoint(IDCollectionPoint point){

        try{
            List<CardData> cardDataList = cardDataRepository.findAllByCollectionPoint(point);
            List<IPSJob> ipsJobList = ipsJobRepository.getAllByCollectionPoint(point);

            // if card list isn't empty will delete all
            if (!cardDataList.isEmpty()){cardDataRepository.deleteAll(cardDataList);}

            // if ipsJobList isn't empty, will delete all
            if(!ipsJobList.isEmpty()){ipsJobRepository.deleteAll(ipsJobList);}

            // deletes the collection point last
            idCollectionPointRepository.delete(point);

        } catch (Exception e){
            return false;
        }

        return true;
    }
}

