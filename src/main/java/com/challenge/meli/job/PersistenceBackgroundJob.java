package com.challenge.meli.job;

import com.challenge.meli.services.BufferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PersistenceBackgroundJob {

    @Autowired
    BufferService bufferService;

    final Logger logger = LoggerFactory.getLogger(PersistenceBackgroundJob.class);

    // fixedDelay will wait until the last invocation finishes before executing again.
    @Scheduled(fixedDelay = 1000) // 1 second
    public void scheduleBatchInsert() {
        if(bufferService.isReadyToWork()) {
            logger.debug("Executing batch insertion"); // this may get too spammy
            bufferService.populateDatabase();
            logger.debug("Done");
        }
    }
}
