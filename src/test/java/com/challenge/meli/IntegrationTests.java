package com.challenge.meli;

import com.challenge.meli.model.PersonModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testMutantController() {
        final String uri = "http://localhost:" + port + "/isAlive";
        HttpStatus actual = this.restTemplate.getForObject(uri, ResponseEntity.class).getStatusCode();
       assertThat(this.restTemplate.getForObject(uri, HttpStatus.class)).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void IntegrationTest() {
        WebTestClient testClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
        testClient.get().uri("/isAlive").exchange().expectStatus().isOk();

        final String uri = "http://localhost:" + port + "/mutant";
        final String[] dna = new String[]{"CTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCT",
                "TCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTC",
                "AGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAGAG",
                "TCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTCTC"};
        PersonModel genome = new PersonModel(dna);
        this.restTemplate.postForEntity(uri, genome, String.class);

        long startTime = System.currentTimeMillis();
        for(int i = 0; i < 4; i++) {
            this.restTemplate.postForEntity(uri, genome, String.class);
        }
        long stopTime = System.currentTimeMillis();
        System.out.println((stopTime - startTime) + " ms");
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, genome, String.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}


