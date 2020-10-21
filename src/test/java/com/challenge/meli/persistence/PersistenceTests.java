package com.challenge.meli.persistence;

import com.challenge.meli.model.PersonModel;
import com.challenge.meli.repositories.PeopleRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class PersistenceTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PeopleRepository peopleRepository;

    @Test
    void testUniquePersistence() {
        PersonModel someHuman = new PersonModel(new String[]{"AGAGAG", "CTCTCT", "AGAGAG", "CTCTCT", "AGAGAG", "TCTCTC"});
        someHuman.setIsMutant(true);

        PersonModel someMutant = new PersonModel(new String[]{"AAAAAA", "CTCTCT", "AGAGAG", "CTCTCT", "AGAGAG", "TCTCTC"});
        someMutant.setIsMutant(true);

        entityManager.persist(someHuman);
        entityManager.persist(someMutant);
        entityManager.flush();

        PersonModel actual = peopleRepository.findByParsedDna(someHuman.getParsedDna());
        assertThat(actual.getParsedDna()).isEqualTo("AGAGAGCTCTCTAGAGAGCTCTCTAGAGAGTCTCTC");

        actual = peopleRepository.findByParsedDna(someMutant.getParsedDna());
        assertThat(actual.getParsedDna()).isEqualTo("AAAAAACTCTCTAGAGAGCTCTCTAGAGAGTCTCTC");
    }

}
