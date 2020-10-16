package com.challenge.meli.route;

import com.challenge.meli.model.Genome;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.challenge.meli.dna.DnaSolver.isMutant;
import static com.challenge.meli.dna.DnaSolver.isValidDna;

@RestController
public class Mutant {

    @GetMapping("/isAlive")
    public String sayHello() {
        return "OK";
    }

    @PostMapping(path = "/mutant", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> checkMutant(@RequestBody Genome genome) {
        if (!isValidDna(genome.getDna()))
            return new ResponseEntity<>("The DNA syntax is badly formed", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(isMutant(genome.getDna()) ? HttpStatus.OK : HttpStatus.FORBIDDEN);
    }
}
