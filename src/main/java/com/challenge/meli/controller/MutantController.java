package com.challenge.meli.controller;

import com.challenge.meli.model.PersonModel;
import com.challenge.meli.model.StatsModel;
import com.challenge.meli.services.BufferService;
import com.challenge.meli.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.challenge.meli.algorithm.DnaSolver.isValidDna;

@RestController
public class MutantController {

    public final String MUTANT_ENDPOINT = "/mutant";
    public final String STATS_ENDPOINT = "/stats";
    public final String ISALIVE_ENDPOINT = "/isAlive";

    @Autowired
    private StatisticsService stats;

    @Autowired
    private BufferService bufferService;

    @GetMapping(ISALIVE_ENDPOINT)
    public ResponseEntity<?> isAlive() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = STATS_ENDPOINT, produces = "application/json")
    public StatsModel getStatistics() {
        return new StatsModel(stats.getHumanCounter(), stats.getMutantCounter());
    }

    @PostMapping(path = MUTANT_ENDPOINT, consumes = "application/json")
    public ResponseEntity<?> checkMutant(@RequestBody PersonModel person) {
        String[] dna = person.getDna();
        if (!isValidDna(dna))
            return new ResponseEntity<>("The DNA syntax is badly formed", HttpStatus.BAD_REQUEST);
        person.processDna();
        bufferService.addToQueue(person);
        if(person.getIsMutant()) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
