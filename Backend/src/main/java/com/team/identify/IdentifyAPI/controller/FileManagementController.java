package com.team.identify.IdentifyAPI.controller;

import com.team.identify.IdentifyAPI.database.CompanyRepository;
import com.team.identify.IdentifyAPI.model.Company;
import com.team.identify.IdentifyAPI.model.DBFile;
import com.team.identify.IdentifyAPI.payload.response.MessageResponse;
import com.team.identify.IdentifyAPI.service.FileManagementService;
import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.UUID;

// Controller which handles uploading/downloading files (word documents)
@Tag(name = "File/Document management", description = "Store/download word docs for form generation")
@RestController
@RequestMapping("/files/{id}")
@PreAuthorize("@companyGuard.checkPermission(#id , 'VIEW_COMPANY')")
public class FileManagementController {

    private final CompanyRepository companyRepo;
    private final FileManagementService fileManagementService;

    public FileManagementController(CompanyRepository companyRepo, FileManagementService fileManagementService) {
        this.companyRepo = companyRepo;
        this.fileManagementService = fileManagementService;
    }

    // function allows upload of a file
    @Operation(
            summary = "Upload a file",
            description = "Allows a user to upload a file to be stored in the database"
    )
    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable String id, @RequestParam String fileName, @RequestParam String description) throws IOException, java.io.IOException {

        // gets company from DB
        Company company = companyRepo.getReferenceById(UUID.fromString(id));

        // calls save file function
        if(!fileManagementService.saveFile(file, company, fileName, description)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Failed to upload file"));

        return ResponseEntity.ok(new MessageResponse("File uploaded successfully"));
    }

    // function allows user to update a file
    @Operation(
            summary = "Update a file",
            description = "Allows a user to update an existing file in the database"
    )
    @PutMapping("/update/{fileId}")
    public ResponseEntity<MessageResponse> updateFile(@RequestParam("file") MultipartFile file, @PathVariable String fileId, @PathVariable String id,  @RequestParam String fileName, @RequestParam String description){
        Company company = companyRepo.getReferenceById(UUID.fromString(id));

        if(!fileManagementService.updateFile(file, company, fileName, description, fileId)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Failed to update file"));

        return ResponseEntity.ok(new MessageResponse("File updated successfully"));
    }

    // function allows download of files
    @Operation(
            summary = "Get/Download a file",
            description = "Allows a user to download an individual file"
    )
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String id, @PathVariable String fileId){
        Company company = companyRepo.getReferenceById(UUID.fromString(id));

        HttpHeaders header = new HttpHeaders();
        DBFile file = fileManagementService.getFile(fileId, company);

        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename =\"" + file.getFileName() + "\"");

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(file.getFileType()))
                .header(String.valueOf(header))
                .body(new ByteArrayResource(file.getData()));
    }

    // function returns a list of the documents a company has uploaded
    @Operation(
            summary = "Get list of files/documents",
            description = "Returns the names and ID's of all files for a specific company"
    )
    @GetMapping("")
    public List<DBFile> getListOfDocuments(@PathVariable String id){
        Company company = companyRepo.getReferenceById(UUID.fromString(id));

        return fileManagementService.getListOfFiles(company);
    }

    @Operation(
            summary = "Delete a file",
            description = "Uses the id of a file to identify and delete it from the database"
    )
    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<MessageResponse> deleteFile(@PathVariable String id, @PathVariable String fileId){
        Company company = companyRepo.getReferenceById(UUID.fromString(id));

        if(!fileManagementService.deleteFileInDB(company, fileId)){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse("File failed to delete"));
        }

        return ResponseEntity.ok(new MessageResponse("File deleted successfully"));
    }
}
