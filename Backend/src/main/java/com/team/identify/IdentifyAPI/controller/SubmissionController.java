package com.team.identify.IdentifyAPI.controller;

import com.team.identify.IdentifyAPI.apps.ips.service.IPSJobService;
import com.team.identify.IdentifyAPI.database.*;
import com.team.identify.IdentifyAPI.model.CardData;
import com.team.identify.IdentifyAPI.model.Company;
import com.team.identify.IdentifyAPI.model.IDCollectionPoint;
import com.team.identify.IdentifyAPI.model.IPSJob;
import com.team.identify.IdentifyAPI.model.enums.ECollectionPointState;
import com.team.identify.IdentifyAPI.model.enums.EJobState;
import com.team.identify.IdentifyAPI.payload.response.JobStateResponse;
import com.team.identify.IdentifyAPI.payload.response.SubmissionResponse;
import com.team.identify.IdentifyAPI.util.exception.CollectionPointNotFoundError;
import com.team.identify.IdentifyAPI.util.exception.CompanyNotFoundError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Image Submission", description = "Submit an image for processing")
@RestController
@RequestMapping("/submit")
public class SubmissionController {

    @Value("${flatfileStorage.path:/tmp/}")
    private String storageRoot;

    private final IPSJobService ipsJobService;
    private final CompanyRepository companyRepository;
    private final IDCollectionPointRepository idCollectionPointRepository;
    private final CardDataRepository cardDataRepository;
    private final IPSJobRepository ipsJobRepo;
    private final HttpServletRequest request;

    private static final Logger logger = LoggerFactory.getLogger(SubmissionController.class);

    public SubmissionController(IPSJobService IPSJobService, 
                                CompanyRepository companyRepository, 
                                IDCollectionPointRepository idCollectionPointRepository, 
                                CardDataRepository cardDataRepository, 
                                IPSJobRepository ipsJobRepo, 
                                HttpServletRequest request) {
        this.ipsJobService = IPSJobService;
        this.companyRepository = companyRepository;
        this.idCollectionPointRepository = idCollectionPointRepository;
        this.cardDataRepository = cardDataRepository;
        this.ipsJobRepo = ipsJobRepo;
        this.request = request;
    }


    @Operation(
            summary = "Submit an image",
            description = "Submit an image to the API via a multipart/form-data request"
    )
    @PostMapping(
            value = "/{companyEndpoint}/{collectionEndpoint}",
            consumes = "multipart/form-data")
    public ResponseEntity<SubmissionResponse> submitJob(
            @Parameter(description = "The company's unique endpoint") @PathVariable String companyEndpoint,
            @Parameter(description = "The collection's points endpoint") @PathVariable String collectionEndpoint,
            @Parameter(description = "jpg or png binary image") @RequestParam("image") MultipartFile file
    ) throws IOException {
        Company company = companyRepository.findByCompanyEndpoint(companyEndpoint).orElseThrow(
                () -> new CompanyNotFoundError(companyEndpoint, "company endpoint")
        );
        IDCollectionPoint collectionPoint = idCollectionPointRepository.getIDCollectionPointByEndpointAndCompany(
                collectionEndpoint,
                        company)
                .orElseThrow(
                        () -> new CollectionPointNotFoundError(collectionEndpoint)
                );
        IPSJob job = ipsJobService.submitJob(file.getBytes(), request.getRemoteAddr(), collectionPoint);
        return ResponseEntity.ok(new SubmissionResponse(job.getId().toString(), 3));
    }

    @Operation(
            summary = "Submit an image for customer/not signed in user",
            description = "Submit an image to the API via a multipart/form-data request. Function will check the status of the collection point"
    )
    @PostMapping(
            value = "/{companyEndpoint}/{collectionEndpoint}/customer",
            consumes = "multipart/form-data")
    public ResponseEntity<SubmissionResponse> submitJobUser(
            @Parameter(description = "The company's unique endpoint") @PathVariable String companyEndpoint,
            @Parameter(description = "The collection's points endpoint") @PathVariable String collectionEndpoint,
            @Parameter(description = "jpg or png binary image") @RequestParam("image") MultipartFile file
    ) throws IOException {

        Company company = companyRepository.findByCompanyEndpoint(companyEndpoint).orElseThrow(
                () -> new CompanyNotFoundError(companyEndpoint, "company endpoint")
        );
        IDCollectionPoint collectionPoint = idCollectionPointRepository.getIDCollectionPointByEndpointAndCompany(
                        collectionEndpoint,
                        company)
                .orElseThrow(
                        () -> new CollectionPointNotFoundError(collectionEndpoint)
                );

        // checks if CP is deactivated
        if(collectionPoint.getState() == ECollectionPointState.DEACTIVATED) return ResponseEntity.status(HttpStatus.CONFLICT).build();

        IPSJob job = ipsJobService.submitJob(file.getBytes(), request.getRemoteAddr(), collectionPoint);
        return ResponseEntity.ok(new SubmissionResponse(job.getId().toString(), 3));
    }

    @Operation(
            summary = "Get the state of a job",
            description = "Poll this endpoint to see the state of your job, requires request to be sent from the same IP as the image"
    )
    @GetMapping("/{jobId}/state")
    public ResponseEntity<JobStateResponse> getJobState(
            @Parameter(description = "The ID of the job that was submitted") @PathVariable String jobId, 
            HttpServletRequest request) {
        try {
            EJobState toReturn = ipsJobService.getJobState(jobId);
            if (toReturn == EJobState.MISSING)
                return ResponseEntity.notFound().build();

            if (toReturn == EJobState.COMPLETE) {
                CardData cardData = cardDataRepository.findById(UUID.fromString(jobId)).get(); // get is fine, we know it exists
                if (Objects.equals(request.getRemoteAddr(), cardData.getSubmissionIP()))
                    return ResponseEntity.ok(new JobStateResponse(toReturn));
                else
                    throw new AccessDeniedException("You are not the submitter of this request.");
            }

            IPSJob job = ipsJobRepo.findByIdString(jobId).get(); //get is fine
            if (!Objects.equals(request.getRemoteAddr(), job.getSubmissionIp()))
                throw new AccessDeniedException("You are not the submitter of this request.");

            if (toReturn == EJobState.FAILED) {
                String errorMessage = ipsJobService.getJobError(job);
                return ResponseEntity.ok(new JobStateResponse(toReturn, errorMessage));
            }

            return ResponseEntity.ok(new JobStateResponse(toReturn));
        } catch (NoSuchElementException e) {
            return ResponseEntity.ok(new JobStateResponse(EJobState.PROCESSING));
        }
    }

}
