package com.team.identify.IdentifyAPI.apps.ips.controller;


import com.team.identify.IdentifyAPI.apps.ips.service.IPSJobService;
import com.team.identify.IdentifyAPI.apps.ips.service.IPSService;
import com.team.identify.IdentifyAPI.model.IPS;
import com.team.identify.IdentifyAPI.payload.request.FinishedJobRequest;
import com.team.identify.IdentifyAPI.payload.response.AESKeyResponse;
import com.team.identify.IdentifyAPI.payload.response.MessageResponse;
import com.team.identify.IdentifyAPI.security.services.UserDetailsImpl;
import com.team.identify.IdentifyAPI.util.exception.IdNotFoundError;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('IPS_SERVER')")
@RequestMapping("/ips")
public class IPSController {

    private static final Logger logger = LoggerFactory.getLogger(IPSService.class);
    private final IPSJobService ipsJobService;

    @Value("${flatfileStorage.path:/tmp/}")
    private String storageRoot;

    @Autowired
    IPSService ipsService;

    public IPSController(IPSJobService ipsJobService) {
        this.ipsJobService = ipsJobService;
    }


    @PostMapping("/register")
    public ResponseEntity<IPS> registerServer(@RequestParam int maxJobs, Authentication auth) throws IdNotFoundError {
        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
        IPS server = ipsService.registerServer(maxJobs, user.getId());
        return ResponseEntity.ok(server);
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<AESKeyResponse> getJobKeys(@PathVariable String jobId, Authentication auth) throws IdNotFoundError {
        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
        ImmutablePair<String, String> keyAndIv = ipsJobService.getEncryptionKeys(jobId, user.getUsername());
        return ResponseEntity.ok(new AESKeyResponse(keyAndIv.getLeft(), keyAndIv.getRight()));
    }

    @PostMapping("/job/{jobId}")
    public ResponseEntity<MessageResponse> finishedJob(@PathVariable String jobId, @RequestBody FinishedJobRequest req) throws IdNotFoundError {
        if (null != req.error) {
            ipsJobService.jobError(jobId, req.error);
        } else {
            ipsJobService.finishedJob(jobId, req);
        }

        return ResponseEntity.ok(new MessageResponse("OK!"));
    }
}
