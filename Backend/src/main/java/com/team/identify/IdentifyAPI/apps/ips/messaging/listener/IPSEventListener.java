package com.team.identify.IdentifyAPI.apps.ips.messaging.listener;


import com.team.identify.IdentifyAPI.apps.ips.service.IPSService;
import com.team.identify.IdentifyAPI.apps.ips.service.IPSStatsService;
import com.team.identify.IdentifyAPI.payload.request.messaging.IPSEventMessage;
import com.team.identify.IdentifyAPI.util.exception.IdNotFoundError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class IPSEventListener {

    private static final Logger logger = LoggerFactory.getLogger(IPSEventListener.class);

    private IPSStatsService ipsStatsService;

    private IPSService ipsService;

    @Autowired
    public IPSEventListener(IPSStatsService ipsStatsService, IPSService ipsService){
        this.ipsStatsService = ipsStatsService;
        this.ipsService = ipsService;
    }

    @RabbitListener(queues = "#{ipsShutdownEventQueue.name}")
    public void receiveIpsShutdownEvent(IPSEventMessage event) throws IdNotFoundError {
        logger.info("Received shutdown event from: {}", event.getIpsId());
        ipsService.processShutdownMessage(event.getIpsId());
    }

    @RabbitListener(queues = "#{ipsStartupEventQueue.name}")
    public void receiveIpsStartupEvent(IPSEventMessage event) {
        logger.info("Received startup event from: {}", event.getIpsId());
    }

    @RabbitListener(queues = "#{ipsHeartbeatResponseQueue.name}")
    public void receiveHeartbeatResponse(IPSEventMessage event) {
        logger.debug("Received heartbeat event: {}", event.toString());
        ipsStatsService.processHeartBeatMessage(event);
    }

}
