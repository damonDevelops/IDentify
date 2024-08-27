package com.team.identify.IdentifyAPI.database;

import com.team.identify.IdentifyAPI.model.Company;
import com.team.identify.IdentifyAPI.model.DBFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface DBFileRepository extends JpaRepository<DBFile, UUID> {

    DBFile getDBFileByIdAndCompany(UUID id, Company company);

    List<DBFile> getAllByCompany(Company company);

}
