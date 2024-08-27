package com.team.identify.IdentifyAPI.service;

import com.team.identify.IdentifyAPI.database.DBFileRepository;
import com.team.identify.IdentifyAPI.model.Company;
import com.team.identify.IdentifyAPI.model.DBFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// service used for companies to upload word docs for form generation
@Service
public class FileManagementService {

    private final DBFileRepository dbFileRepository;

    public FileManagementService( DBFileRepository dbFileRepository) {
        this.dbFileRepository = dbFileRepository;
    }

    // function used to save a file as a byte array to the DB
    @Transactional
    public boolean saveFile(MultipartFile file, Company company, String fileName, String description) {

        try{

            DBFile dbFile = new DBFile(company, fileName, file.getContentType(), description, file.getBytes());

            dbFileRepository.save(dbFile);

        } catch (IOException exception){
            return false;
        }


        return true;
    }

    // function used to get a file from the db using the company and id of the file
    @Transactional
    public DBFile getFile(String id, Company company){

        return dbFileRepository.getDBFileByIdAndCompany(UUID.fromString(id), company);

    }

    @Transactional
    public boolean updateFile(MultipartFile file, Company company, String fileName, String description, String fileId) {


        try{

            DBFile dbFile = dbFileRepository.getDBFileByIdAndCompany(UUID.fromString(fileId), company);
            dbFile.setDescription(description);
            dbFile.setFileType(file.getContentType());
            dbFile.setFileName(fileName);
            dbFile.setData(file.getBytes());
            dbFileRepository.save(dbFile);

        } catch (Exception exception){
            return false;
        }

        return true;
    }

    // function used to get list of file names and id's
    public List<DBFile> getListOfFiles(Company company){
        // gets the full list of files by company and creates a new filteredList to hold file information
        // being sent to the frontend
        List<DBFile> rawList = dbFileRepository.getAllByCompany(company);
        List<DBFile> filteredList = new ArrayList<>();

        // iterates through each file and sets specific details without the actual files attached
        for(DBFile file : rawList){
            DBFile filteredFile = new DBFile();
            filteredFile.setFileName(file.getFileName());
            filteredFile.setId(file.getId());
            filteredFile.setDescription(file.getDescription());
            filteredList.add(filteredFile);
        }

        // returns the filtered list
        return filteredList;
    }

    // function used to delete a file from the DB
    @Transactional
    public boolean deleteFileInDB(Company company, String fileId){

        try{
            DBFile file = dbFileRepository.getDBFileByIdAndCompany(UUID.fromString(fileId), company);
            dbFileRepository.delete(file);
        }
        catch (Exception e){
            return false;
        }


        return true;
    }
}
