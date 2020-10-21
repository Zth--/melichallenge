package com.challenge.meli.services;

import com.challenge.meli.model.PersonModel;
import com.challenge.meli.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DnaService {
    @Autowired
    PeopleRepository peopleRepository;

    public boolean existsInDB(String dna) {
        return peopleRepository.findByParsedDna(dna) != null;
    }

    public void save(Iterable<PersonModel> dna) {
        peopleRepository.saveAll(dna);
    }
}
