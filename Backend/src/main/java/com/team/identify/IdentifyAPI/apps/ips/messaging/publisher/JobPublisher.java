package com.team.identify.IdentifyAPI.apps.ips.messaging.publisher;

import com.team.identify.IdentifyAPI.database.IPSJobRepository;
import com.team.identify.IdentifyAPI.database.IPSRepository;
import com.team.identify.IdentifyAPI.model.IPS;
import com.team.identify.IdentifyAPI.model.IPSJob;
import com.team.identify.IdentifyAPI.payload.request.messaging.JobCallbackRequest;
import com.team.identify.IdentifyAPI.payload.response.JobDataResponse;
import com.team.identify.IdentifyAPI.util.exception.IdNotFoundError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class JobPublisher {
    private final RabbitTemplate rabbitTemplate;

    private final Queue ackQueue;
    private final DirectExchange jobRpcExchange;
    private final com.team.identify.IdentifyAPI.database.IPSRepository ipsRepository;
    private final com.team.identify.IdentifyAPI.database.IPSJobRepository ipsJobRepository;

    private static final Logger logger = LoggerFactory.getLogger(JobPublisher.class);



    public JobPublisher(RabbitTemplate rabbitTemplate, @Qualifier("ackQueue") Queue ackQueue, @Qualifier("jobRpcExchange") DirectExchange jobRpcExchange, IPSRepository ipsRepository, IPSJobRepository ipsJobRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.ackQueue = ackQueue;
        this.jobRpcExchange = jobRpcExchange;
        this.ipsRepository = ipsRepository;
        this.ipsJobRepository = ipsJobRepository;
    }

    /**
     * Publishes job to IPS queue and returns the id of the assigned ips
     * @param jobResp
     * @return ips id
     * @throws IdNotFoundError
     */

    public void publishJob(JobDataResponse jobResp) {
        rabbitTemplate.convertAndSend(jobRpcExchange.getName(), "jobs", jobResp);
    }

    @RabbitListener(queues = "#{ackQueue.name}")
    public void receiveJobAck(JobCallbackRequest response) {
        logger.info("Received job ack");
        try {
            if (null != response.getIpsId() && null != response.getJobId()) {
                IPS ips = ipsRepository.findByIdString(response.getIpsId()).orElseThrow(
                        () -> new IdNotFoundError(response.getIpsId())
                );
                IPSJob job = ipsJobRepository.findByIdString(response.getJobId()).orElseThrow(
                        () -> new IdNotFoundError(response.getJobId())
                );

                job.setAssignedIPS(ips);
                ipsJobRepository.save(job);
            }
        } catch (IdNotFoundError ignored) {
            logger.error(ignored.toString());
            //TODO: implement error handling
        }

    }

}
