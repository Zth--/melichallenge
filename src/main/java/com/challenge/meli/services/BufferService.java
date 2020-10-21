package com.challenge.meli.services;

import com.challenge.meli.model.PersonModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BufferService {

    @Autowired
    private DnaService dnaService;

    @Autowired
    private StatisticsService stats;

    Logger logger = LoggerFactory.getLogger(BufferService.class);

    Map<String, PersonModel> queue = new ConcurrentHashMap<>();
    Map<String, PersonModel> dataToInsert = new ConcurrentHashMap<>();

    public void addToQueue(PersonModel dnaSequence) {
        queue.put(dnaSequence.getParsedDna(), dnaSequence);
    }

    public void populateDatabase() {
        dataToInsert = queue;
        queue = new ConcurrentHashMap<>();

        logger.debug("Validating data to insert");
        // ok this is probably bad since it's checking every single item in the queue one at a time. This would
        // be THE bottleneck of the application and improving it would drastically improve the overall performance.
        ArrayList<PersonModel> validatedDataToInsert = new ArrayList<>();
        for(Map.Entry<String, PersonModel> keyValue : dataToInsert.entrySet()) {
            if(!dnaService.existsInDB(keyValue.getValue().getParsedDna())) {
                if (keyValue.getValue().getIsMutant()) stats.incrementMutantCounter(); else stats.incrementHumanCounter();
                validatedDataToInsert.add(keyValue.getValue());
            }
        }
        logger.info("Inserting " + validatedDataToInsert.size() + " records to the database.");
        try {
            dnaService.save(validatedDataToInsert);
        } catch (Exception e) {
            // a retry mechanism would be smarter
            logger.error("There was an error while trying to batch insert, this batch will be dropped and data will be lost. Error: " + e.getMessage());
        }
        dataToInsert = new ConcurrentHashMap<>();
    }

    public boolean isReadyToWork() {
        return (!queue.isEmpty()) && (dataToInsert.isEmpty());
    }
}
