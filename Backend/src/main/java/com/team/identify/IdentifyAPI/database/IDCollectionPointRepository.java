package com.team.identify.IdentifyAPI.database;

import com.team.identify.IdentifyAPI.model.Company;
import com.team.identify.IdentifyAPI.model.IDCollectionPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IDCollectionPointRepository extends JpaRepository<IDCollectionPoint, UUID> {
    Optional<IDCollectionPoint> getIDCollectionPointByEndpointAndCompany(String endpoint, Company company);

    List<IDCollectionPoint> getAllCollectionPointsByCompany(Company company);

    Optional<IDCollectionPoint> getIDCollectionPointByEndpointAndCompany_CompanyEndpoint(String endpoint,
                                                                                         String companyEndpoint);
}
