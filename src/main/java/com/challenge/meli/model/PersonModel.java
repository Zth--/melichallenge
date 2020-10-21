package com.challenge.meli.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

import static com.challenge.meli.algorithm.DnaSolver.isMutant;

@Entity
@Table(name = "people")
public class PersonModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @JsonInclude()
    @Transient
    private String[] dna;

    @Column(nullable = false, unique = true)
    private String parsedDna;

    private Boolean isMutant;

    public PersonModel() { }

    public PersonModel(String[] dna) {
        this.dna = dna;
        this.isMutant = isMutant(dna);
        this.parsedDna = String.join("", this.dna);
    }

    public void processDna() {
        this.isMutant = isMutant(this.dna);
        this.parsedDna = String.join("", this.dna);
    }

    public String getParsedDna() {
        return parsedDna;
    }

    public void setParsedDna(String parsedDna) {
        this.parsedDna = parsedDna;
    }

    public String[] getDna() {
        return dna;
    }

    public void setDna(String[] dna) {
        this.dna = dna;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public boolean getIsMutant() {
        return isMutant;
    }

    public void setIsMutant(boolean isMutant) {
        this.isMutant = isMutant;
    }
}
