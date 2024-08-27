package com.team.identify.IdentifyAPI.database;

import com.team.identify.IdentifyAPI.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findByName(String name);

    @Query("SELECT c FROM Company c WHERE id = :id")
    Optional<Company> findByIdString(String id);

    Optional<Company> findByCompanyEndpoint(String companyEndpoint);
}
