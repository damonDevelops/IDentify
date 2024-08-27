package com.team.identify.IdentifyAPI.security.company;

import com.team.identify.IdentifyAPI.database.CardDataRepository;
import com.team.identify.IdentifyAPI.database.CompanyRepository;
import com.team.identify.IdentifyAPI.database.IDCollectionPointRepository;
import com.team.identify.IdentifyAPI.model.CardData;
import com.team.identify.IdentifyAPI.model.Company;
import com.team.identify.IdentifyAPI.model.IDCollectionPoint;
import com.team.identify.IdentifyAPI.model.User;
import com.team.identify.IdentifyAPI.security.AuthenticatedUserService;
import com.team.identify.IdentifyAPI.security.services.UserDetailsImpl;
import com.team.identify.IdentifyAPI.service.CardDataService;
import com.team.identify.IdentifyAPI.util.exception.CardDataNotFound;
import com.team.identify.IdentifyAPI.util.exception.CollectionPointNotFoundError;
import com.team.identify.IdentifyAPI.util.exception.CompanyNotFoundError;
import com.team.identify.IdentifyAPI.util.exception.MissingCompanyPermissionError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@Component("companyGuard")
public class CompanyGuard {

    private static final Logger logger = LoggerFactory.getLogger(CompanyGuard.class);
    private final com.team.identify.IdentifyAPI.database.IDCollectionPointRepository pointRepository;
    @Autowired
    private CompanyRepository companyRepo;

    @Autowired
    private AuthenticatedUserService authUser;
    @Autowired
    private CardDataRepository cardDataRepository;

    public CompanyGuard(IDCollectionPointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public boolean checkUserView(UUID companyID) {
        return checkPermission(companyID, "VIEW_COMPANY");
    }

    public boolean checkPermission(UUID companyID, String permission) {
        Authentication auth = authUser.getAuthentication();
        Optional<Company> company = companyRepo.findById(companyID);
        if (company.isPresent()) {
            String authPrefix = company.get().getPermissionPrefix();
            for (GrantedAuthority ga : auth.getAuthorities()) {
                if ("ROLE_SUPERUSER".equals(ga.getAuthority()))
                    return true;
                if (ga.getAuthority().equals(authPrefix + "_" + permission))
                    return true;
            }
        } else {
            throw new CompanyNotFoundError(companyID);
        }
        throw new MissingCompanyPermissionError(auth.getName(), companyID, permission);
    }

    public boolean checkAccessToCollection(String companyEndpoint, String collectionEndpoint) {
        Authentication auth = authUser.getAuthentication();
        Optional<Company> company = companyRepo.findByCompanyEndpoint(companyEndpoint);
        if (company.isPresent()) {
            if (!checkPermission(company.get().getId(), "VIEW_COMPANY")) {
                return false;
            }
            Optional<IDCollectionPoint> point = pointRepository.
                    getIDCollectionPointByEndpointAndCompany(collectionEndpoint, company.get());
            if (point.isPresent()) {
                UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
                for (User grantedAccess: point.get().getAccessList()) {
                    if (Objects.equals(userDetails.getUsername(), grantedAccess.getUsername())) {
                        return true;
                    }
                }
            } else {
                throw new CollectionPointNotFoundError(collectionEndpoint);
            }
        } else {
            throw new CompanyNotFoundError(companyEndpoint, "endpoint");
        }
        return false;
    }

    public boolean checkAccessToCardData(String id){

        // gets user info
        Authentication auth = authUser.getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();

        // checks if card data exists in repo
        Optional<CardData> card = cardDataRepository.findById(UUID.fromString(id));
        if(!card.isPresent()) throw new CardDataNotFound(id);

        // gets the collection point from the card info
        IDCollectionPoint point = card.get().getCollectionPoint();

        // gets the company from the point
        Company company = point.getCompany();

        // checks if user has permission to view
        if(!checkPermission(company.getId(), "VIEW_COMPANY")) return false;
        for(User grantedAccess: point.getAccessList()){
            if(Objects.equals(user.getUsername(), grantedAccess.getUsername())) return true;
        }

        return false;
    }
}
