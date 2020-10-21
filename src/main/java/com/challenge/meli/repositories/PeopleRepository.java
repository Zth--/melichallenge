package com.challenge.meli.repositories;

import com.challenge.meli.model.PersonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepository extends JpaRepository<PersonModel, Long> {
    public PersonModel findByParsedDna(String dna);

    @Query(value = "select count(*) from people where is_mutant=0", nativeQuery = true)
    public int countMutants();

    @Query(value = "select count(*) from people where is_mutant=1", nativeQuery = true)
    public int countHumans();
}
