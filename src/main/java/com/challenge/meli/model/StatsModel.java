package com.challenge.meli.model;

public class StatsModel {
    int count_human_dna;
    int count_mutant_dna;
    double ratio;

    public StatsModel(int count_human_dna, int count_mutant_dna) {
        this.count_human_dna = count_human_dna;
        this.count_mutant_dna = count_mutant_dna;
        if(count_human_dna == 0 && count_mutant_dna == 0) {
            this.ratio = 0f;
        } else {
            // Considering what should I do when the denominator is zero or both arguments are zero is rather
            // unimportant since it can only happen with a freshly deployed app with an empty db.
            this.ratio = (count_mutant_dna == 0) ? (double) count_human_dna : (double) count_human_dna / (double)count_mutant_dna;
        }
    }

    public int getCount_human_dna() {
        return count_human_dna;
    }

    public int getCount_mutant_dna() {
        return count_mutant_dna;
    }

    public double getRatio() {
        return ratio;
    }
}
