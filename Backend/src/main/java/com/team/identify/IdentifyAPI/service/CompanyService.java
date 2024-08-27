package com.team.identify.IdentifyAPI.service;

import com.team.identify.IdentifyAPI.database.AuthorityRepository;
import com.team.identify.IdentifyAPI.database.CompanyRepository;
import com.team.identify.IdentifyAPI.database.CompanyRoleRepository;
import com.team.identify.IdentifyAPI.model.Authority;
import com.team.identify.IdentifyAPI.model.Company;
import com.team.identify.IdentifyAPI.model.CompanyRole;
import com.team.identify.IdentifyAPI.model.User;
import com.team.identify.IdentifyAPI.model.enums.EAuthority;
import com.team.identify.IdentifyAPI.model.enums.ECompanyRole;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyRoleRepository companyRoleRepository;
    private final AuthorityRepository authorityRepository;
    private final IDCollectionPointService collectionPointService;

    public CompanyService(CompanyRepository companyRepository, CompanyRoleRepository companyRoleRepository, AuthorityRepository authorityRepository, IDCollectionPointService collectionPointService) {
        this.companyRepository = companyRepository;
        this.companyRoleRepository = companyRoleRepository;
        this.authorityRepository = authorityRepository;
        this.collectionPointService = collectionPointService;
    }

    @PostConstruct
    public void startupCheck() {
        newCompanyRoleStartupCheck();
    }

    /**
     * @param name  The new company's name. Must be unique.
     * @param owner The user who will be the owner of the new company.
     * @return The newly created company.
     */
    public Company createCompany(String name, User owner) {
        Company newCompany = new Company(name, owner);
        companyRepository.save(newCompany);
        generateCompanyRoles(newCompany);
        collectionPointService.createIDCollectionPoint(newCompany, "default", "default");
        return newCompany;
    }

    public void generateCompanyRoles(Company company) {
        for (ECompanyRole role : ECompanyRole.values()) {
            CompanyRole newRole = new CompanyRole(role, company);
            initDefaultRoleAuthorities(newRole);
            newRole.setAssignedUsers(new ArrayList<>());
            if (newRole.isRole(ECompanyRole.OWNER))
                newRole.getAssignedUsers().add(company.getOwner());
            companyRoleRepository.save(newRole);
        }
    }

    public void newCompanyRoleStartupCheck() {
        for (Company c : companyRepository.findAll()) {
            List<CompanyRole> listRoles = companyRoleRepository.getAllCompanyRoles(c);
            List<ECompanyRole> roleEnums = new ArrayList<>(Arrays.stream(ECompanyRole.values()).toList());

            if (listRoles.size() == roleEnums.size())
                continue; //if company has the same number of roles as our enum, we can stop.

            ArrayList<ECompanyRole> appliedRoles = new ArrayList<>();
            listRoles.forEach(role ->
                    appliedRoles.add(role.getName())
            );

            //remove all appliedRoles from the role enums list, if role enums still has data, insert
            roleEnums.removeAll(appliedRoles);

            for (ECompanyRole leftover : roleEnums) {
                CompanyRole newCr = new CompanyRole(leftover, c);
                initDefaultRoleAuthorities(newCr);
                companyRoleRepository.save(newCr);
            }

        }
    }

    public void initDefaultRoleAuthorities(CompanyRole cr) {
        if (Objects.isNull(cr.getAuthorityList())) {
            cr.setAuthorityList(new ArrayList<>());
        }
        List<Authority> authList = cr.getAuthorityList();
        switch (cr.getName()) {
            case OWNER:
                authList.addAll(authorityRepository.findAll()); //owner role gets all authority roles by default
                return;
            case ADMINISTRATOR:
                authorityRepository.findByName(EAuthority.READ_IDENTITY_IMAGES).ifPresent(authList::add);
                authorityRepository.findByName(EAuthority.EDIT_COMPANY).ifPresent(authList::add);
                authorityRepository.findByName(EAuthority.EDIT_USERS).ifPresent(authList::add);
                authorityRepository.findByName(EAuthority.EDIT_COLLECTION).ifPresent(authList::add);
            case TRUSTED_EMPLOYEE:
                authorityRepository.findByName(EAuthority.READ_IDENTITY_IMAGES).ifPresent(authList::add);
        }
        //add remaining authorities to all company roles
        authorityRepository.findByName(EAuthority.VIEW_COMPANY).ifPresent(authList::add);
        authorityRepository.findByName(EAuthority.READ_IDENTITY_OUTPUT).ifPresent(authList::add);
    }


    // updates company endpoint
    @Transactional
    public void updateCompanyEndpoint(String newEndpoint, UUID companyID){
        Company company = companyRepository.getReferenceById(companyID);
        company.setCompanyEndpoint(newEndpoint);

        companyRepository.save(company);
    }
}
