package com.challenge.meli.services;

import com.challenge.meli.model.PersonModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:test.properties")
public class StatisticsServiceTests {

    private final TestRestTemplate restTemplate;
    private final BufferService bufferService;
    private final String statsEndpoint;
    private final String mutantEndpoint;

    StatisticsServiceTests(@Autowired TestRestTemplate restTemplate,
                           @Autowired BufferService bufferService,
                           @LocalServerPort int port) {
        this.restTemplate = restTemplate;
        this.bufferService = bufferService;
        this.statsEndpoint = "http://localhost:" + port + "/stats";
        this.mutantEndpoint = "http://localhost:" + port + "/mutant";

    }

    @Test
    void testNoStatistics() {
        String actualResponse = restTemplate.getForObject(statsEndpoint, String.class);
        String expectedResponse = "{\"count_human_dna\":0,\"count_mutant_dna\":0,\"ratio\":0.0}";
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void testAllMutantStatistics() {
        PersonModel someMutant = new PersonModel(new String[]{"AAAAAA", "CTCTCT", "AGAGAG", "CTCTCT", "AGAGAG", "TCTCTC"});
        // the next mutant shouldn't count
        PersonModel duplicatedMutant = new PersonModel(new String[]{"AAAAAA", "CTCTCT", "AGAGAG", "CTCTCT", "AGAGAG", "TCTCTC"});

        this.restTemplate.postForEntity(mutantEndpoint, someMutant, String.class);
        this.restTemplate.postForEntity(mutantEndpoint, duplicatedMutant, String.class);
        bufferService.populateDatabase();

        String actualResponse = restTemplate.getForObject(statsEndpoint, String.class);
        String expectedResponse = "{\"count_human_dna\":0,\"count_mutant_dna\":1,\"ratio\":0.0}";
        assertThat(actualResponse).isEqualTo(expectedResponse);


        PersonModel[] mutants = new PersonModel[]{
                new PersonModel(new String[]{"AAAAAA", "QWERTY", "AGAGAG", "CTCTCT", "AGAGAG", "TCTCTC"}),
                new PersonModel(new String[]{"AAAAAA", "ASDFG", "AGAGAG", "CTCTCT", "AGAGAG", "TCTCTC"}),
                new PersonModel(new String[]{"AAAAAA", "ZXCVB", "AGAGAG", "CTCTCT", "AGAGAG", "TCTCTC"})
        };

        for(PersonModel mutant : mutants) {
            this.restTemplate.postForEntity(mutantEndpoint, mutant, String.class);
        }
        bufferService.populateDatabase();

        actualResponse = restTemplate.getForObject(statsEndpoint, String.class);
        expectedResponse = "{\"count_human_dna\":0,\"count_mutant_dna\":4,\"ratio\":0.0}";
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void testAllHumansStatistics() {
        PersonModel person = new PersonModel(new String[]{"AAAAAA", "CTCTCT", "AGAGAG", "CTCTCT", "AGAGAG", "TCTCTC"});

        this.restTemplate.postForEntity(mutantEndpoint, person, String.class);
        bufferService.populateDatabase();
        String actualResponse = restTemplate.getForObject(statsEndpoint, String.class);
        String expectedResponse = "{\"count_human_dna\":0,\"count_mutant_dna\":1,\"ratio\":0.0}";
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

}
