package com.team.identify.IdentifyAPI.database;

import com.team.identify.IdentifyAPI.model.Company;
import com.team.identify.IdentifyAPI.model.CompanyRole;
import com.team.identify.IdentifyAPI.model.enums.EAuthority;
import com.team.identify.IdentifyAPI.model.enums.ECompanyRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRoleRepository extends JpaRepository<CompanyRole, UUID> {
    Optional<CompanyRole> findByName(EAuthority name);

    @Query("SELECT cr FROM CompanyRole cr WHERE company = :c")
    List<CompanyRole> getAllCompanyRoles(@Param("c") Company c);

    CompanyRole getCompanyRoleByNameAndCompany(ECompanyRole name, Company company);
}

