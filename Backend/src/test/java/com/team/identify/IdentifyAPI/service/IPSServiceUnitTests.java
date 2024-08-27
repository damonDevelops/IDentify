package com.team.identify.IdentifyAPI.service;

import com.team.identify.IdentifyAPI.database.CardDataRepository;
import com.team.identify.IdentifyAPI.database.IPSJobRepository;
import com.team.identify.IdentifyAPI.database.IPSRepository;
import com.team.identify.IdentifyAPI.apps.ips.service.IPSService;
import com.team.identify.IdentifyAPI.model.IPS;
import com.team.identify.IdentifyAPI.model.User;
import com.team.identify.IdentifyAPI.model.enums.EIPSState;
import com.team.identify.IdentifyAPI.util.FlatfileStorage;
import com.team.identify.IdentifyAPI.util.exception.IdNotFoundError;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

@SpringBootTest
class IPSServiceUnitTests {

    @MockBean
    IPSRepository ipsRepo;

    @MockBean
    IPSJobRepository ipsJobRepo;

    @MockBean
    CardDataRepository cardRepo;

    @Autowired
    IPSService ipsService;



    @BeforeEach
    void setUp() {
//        User demoUser = new User("user", "user@user.com", "user");
//        IPS testIPS = new IPS(10, EIPSState.UP, demoUser);;
//        IPSJob job = new IPSJob("TESTIMAGEID", EJobState.NEW, testIPS, "0.0.0.0");
//        List<IPSJob> testList = new ArrayList<>();
//        testList.add(job);
//
//        when(ipsRepo.findByIdString(Mockito.anyString())).thenReturn(Optional.of(testIPS));
//        when(ipsRepo.save(Mockito.any(IPS.class)))
//                .thenAnswer(i -> i.getArguments()[0]);
//        when(ipsJobRepo.getIpsJobsByIps(Mockito.any(IPS.class))).thenReturn(Optional.of(testList));
//        when(ipsJobRepo.findByIdString(Mockito.anyString())).thenReturn(Optional.of(job));


    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void registerServer() throws IdNotFoundError {
        User demoUser = new User("user", "user", "user@user.com");
        IPS expectedServer = new IPS(10, EIPSState.UP, demoUser);
        IPS result = ipsService.registerServer(10, demoUser.getId());
        assert result.getMaxJobs() == expectedServer.getMaxJobs();
        assert result.getState() == expectedServer.getState();
        //assert Objects.equals(demoUser.getId(), result.getUser().getId());
    }

    @Test
    void deleteServer() throws IdNotFoundError {
        ipsService.deleteServer("adadw");
        verify(ipsRepo, atLeastOnce()).delete(Mockito.any(IPS.class));
    }

    @Test
    void getJobQueue() throws IdNotFoundError{
//        List<IPSJob> job_list = ipsService.getJobQueue("TEST");
//        assert job_list.size() == 1;
    }

    @Test
    void getJobDataResponse() throws IdNotFoundError{
        try (MockedStatic<FlatfileStorage> utils = Mockito.mockStatic(FlatfileStorage.class)) {
            utils.when(() -> FlatfileStorage.getFileById(Mockito.any(), Mockito.any())).thenReturn(
                    StandardCharsets.UTF_8.encode("TestMethod").array()
            );
            //JobDataResponse resp = ipsService.getJobDataResponse("TEST", "TEST");
            //assert resp.getImageB64().equals(Base64.getEncoder().encodeToString(StandardCharsets.UTF_8.encode("TestMethod").array()));
        }
    }

    @Test
    void jobFinished() {

    }
}