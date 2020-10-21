package com.challenge.meli;

import com.challenge.meli.job.PersistenceBackgroundJob;
import com.challenge.meli.model.PersonModel;
import org.apache.commons.lang3.ArrayUtils;
import org.awaitility.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;

@TestPropertySource(locations="classpath:test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTests {

    @SpyBean
    private PersistenceBackgroundJob persistenceBackgroundJob;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testMutantController() {
        final String uri = "http://localhost:" + port + "/isAlive";
        assertThat(this.restTemplate.getForObject(uri, HttpStatus.class)).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void IntegrationTest() throws InterruptedException {
        final String mutantUrl = "http://localhost:" + port + "/mutant";
        final String statsUrl = "http://localhost:" + port + "/stats";

        PersonModel[] mutants = {
                new PersonModel(new String[]{"TCTCTC","TCTCTC","TCTCTC","TCTCTC","TCTCTC","TCTCTC"}),
                new PersonModel(new String[]{"TCTCTC","TCTCTC","TCTCTC","TCTCTC","TCTCTC","TCTCTC"}), // duplicated
                new PersonModel(new String[]{"ACACAC","TCTCTC","TCTCTC","TCTCTC","TCTCTC","TCTCTC"}),
                new PersonModel(new String[]{"GGGGGG","TCTCTC","TCTCTC","TCTCTC","TCTCTC","TCTCTC"}),
                new PersonModel(new String[]{"TAATAA","GGGCCC","CCCGGG","AAAAAA","TCTCTC","TCTCTC"})
        };
        PersonModel[] humans = {
                new PersonModel(new String[]{"ATATAT","GGGCCC","CCCGGG","TTTAAA","AAATTT","TATATA"}),
                new PersonModel(new String[]{"TTATAT","GGGCCC","CCCGGG","TTTAAA","AAATTT","TATATA"}),
                new PersonModel(new String[]{"ATTTAT","GGGCCC","CCCGGG","TTTAAA","AAATTT","TATATA"}),
                new PersonModel(new String[]{"ATATTT","GGGCCC","CCCGGG","TTTAAA","AAATTT","TATATA"}),
                new PersonModel(new String[]{"TTATTT","GGGCCC","CCCGGG","TTTAAA","AAATTT","TATATA"})
        };

        PersonModel[] people = ArrayUtils.addAll(mutants, humans);
        for(PersonModel person : people) {
            this.restTemplate.postForEntity(mutantUrl, person, String.class);
        }

        Thread.sleep(5000); // we wait until the background job finishes

        String actual = this.restTemplate.getForObject(statsUrl, String.class);
        assertThat(actual).isEqualTo("{\"count_human_dna\":5,\"count_mutant_dna\":4,\"ratio\":1.25}");
    }
}


