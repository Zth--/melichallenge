package com.challenge.meli.services;

import com.challenge.meli.repositories.PeopleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StatisticsService {
    AtomicInteger count_human_dna = new AtomicInteger(0);
    AtomicInteger count_mutant_dna = new AtomicInteger(0);

    private final Logger log = LoggerFactory.getLogger(StatisticsService.class);

    @Autowired
    PeopleRepository peopleRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        log.info("Retrieving stats from the db.");
        int mutants = peopleRepository.countMutants();
        int humans = peopleRepository.countHumans();
        this.count_human_dna.set(humans);
        this.count_mutant_dna.set(mutants);
        log.info("Found " + mutants + " mutants and " + humans + " humans on the db.");
    }

    public void incrementMutantCounter() {
        this.count_mutant_dna.incrementAndGet();
    }

    public void incrementHumanCounter() {
        this.count_human_dna.incrementAndGet();
    }

    public int getHumanCounter() {
        return this.count_human_dna.get();
    }

    public int getMutantCounter() {
        return this.count_mutant_dna.get();
    }
}
