package com.team.identify.IdentifyAPI.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "files")
public class DBFile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Company company;

    private String fileName;
    private String fileType;
    private String description;

    @Lob
    private byte[] data;

    public DBFile(Company company, String fileName, String fileType, String description, byte[] data) {
        this.company = company;
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.description = description;
    }

    public DBFile() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        if(!fileName.isEmpty()) this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {

        if(!fileType.isEmpty()) this.fileType = fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        if(data != null && data.length > 0) this.data = data;
    }

    public String getDescription() {return description;}

    public void setDescription(String description) {
        if(!description.isEmpty()) this.description = description;
    }
}
