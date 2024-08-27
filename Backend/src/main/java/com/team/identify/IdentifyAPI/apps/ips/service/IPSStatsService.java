package com.team.identify.IdentifyAPI.apps.ips.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.identify.IdentifyAPI.database.IPSRepository;
import com.team.identify.IdentifyAPI.database.IPSStatsRepository;
import com.team.identify.IdentifyAPI.model.IPS;
import com.team.identify.IdentifyAPI.model.IPSStats;
import com.team.identify.IdentifyAPI.payload.request.messaging.IPSEventMessage;
import com.team.identify.IdentifyAPI.util.exception.IdNotFoundError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IPSStatsService {

    private static final Logger logger = LoggerFactory.getLogger(IPSStatsService.class);

    private IPSStatsRepository ipsStatsRepository;
    private IPSRepository ipsRepository;
    final static int GPUs = 8; // change this if max GPUs change

    @Autowired
    public IPSStatsService(IPSRepository ipsRepository, IPSStatsRepository ipsStatsRepository){
        this.ipsRepository = ipsRepository;
        this.ipsStatsRepository = ipsStatsRepository;
    }

    public IPSStatsService(){
    }

    public void processHeartBeatMessage(IPSEventMessage message){
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            JsonNode jsonObj = objectMapper.readTree(message.getData());
            int cpuUtil = jsonObj.get("cpuUtilisation").asInt();
            int ramUtil = jsonObj.get("ramUtilisation").asInt();
            Long maxRam = jsonObj.get("maxRam").asLong();
            Long usedRam = jsonObj.get("usedRam").asLong();


            IPSStats ipsStat = new IPSStats(cpuUtil, ramUtil, maxRam, usedRam);

            int[] vramUtil = new int[GPUs];
            int[] gpuUtil = new int[GPUs];

            // checks jsonObj for gpu info
            // if none exists sets the value to 0
            for(int i = 1; i <= GPUs; i++){
                String vramStr = "gpu" + i + "VramUtilisation";
                String gpuStr = "gpu" + i + "Utilisation";
                if(jsonObj.has(vramStr)){
                    vramUtil[i-1] = jsonObj.get(vramStr).asInt();
                    gpuUtil[i-1] = jsonObj.get(gpuStr).asInt();
                } else {
                    vramUtil[i-1] = 0;
                    gpuUtil[i-1] = 0;
                }
            }

            // sets Util values for ipsStat
            ipsStat.setGPUUtil(vramUtil, gpuUtil);

            // finds related IPS to add to IPSStat
            IPS ips = ipsRepository.findByIdString(message.getIpsId()).orElseThrow(() -> new IdNotFoundError(message.getIpsId()));
            ipsStat.setRelatedIPS(ips);

            // Saves ipsStat to repo
            ipsStatsRepository.save(ipsStat);

        } catch (IdNotFoundError idError){
            idError.printStackTrace();
            logger.error(idError.toString());
        } catch (JsonProcessingException jsonProcessingException) {
            System.out.println("Exception while processing JSON: " + jsonProcessingException.getOriginalMessage());
            jsonProcessingException.printStackTrace();
            logger.error(jsonProcessingException.toString());
        }


    }



}
