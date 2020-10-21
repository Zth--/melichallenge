package com.challenge.meli.algorithm;

// Level 1 algorithm.
public abstract class DnaSolver {
    public static boolean isMutant(String[] dna) {
        return checkRows(dna) || checkColumns(dna) || checkDiagonals(dna);
    }

    public static boolean isValidDna(String[] dna) {
        /*
         * The challenge says "answer 200 if it is a mutant, otherwise answer 403".
         * But should I really treat bad requests as non-mutants? It could easily be an oversight of the document
         * and I could be missing my chance of making a more robust system.
         * What if Magneto has a bug in his system and he's thinking that he has had bad luck with his recruiting
         * when in reality he's been feeding bad requests?
         * Well, I could also assume Magneto is too smart to make mistakes and I could forget about it
         * (he's a mutant after all!)
         * In a real world scenario I would try to talk to Magneto and ask him about it instead of assuming things.
         * So given my context I have two choices:
         * Answer 400 Bad Request or use the same error code as if it was a non mutant (403 Forbidden).
         * Going with 400 also has some security implications but I consider them unimportant for this case
         * So I'm going with 400 Bad Request
         */
        return dna.length >= 4 && dna[0].length() == dna.length;
    }

    public static boolean checkRows(String[] dna) {
        for(int col = 0; col < dna[0].length() - 3; col++) {
            for(int row = 0; row < dna.length; row++) {
                if( (dna[row].charAt(col) == dna[row].charAt(col + 1)) &&
                        (dna[row].charAt(col + 1) == dna[row].charAt(col + 2)) &&
                        (dna[row].charAt(col + 2) == dna[row].charAt(col + 3)) ) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkColumns(String[] dna) {
        for(int col = 0; col < dna[0].length(); col++) {
            for(int row = 0; row < dna.length - 3; row++) {
                if( (dna[row].charAt(col) == dna[row + 1].charAt(col)) &&
                        (dna[row + 1].charAt(col) == dna[row + 2].charAt(col)) &&
                        (dna[row + 2].charAt(col) == dna[row + 3].charAt(col)) ) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkDiagonals(String[] dna) {
        // Major diagonal ╲
        for(int col = 0; col < dna[0].length() - 3; col++) {
            for(int row = 0; row < dna.length - 3; row++) {
                if( (dna[col].charAt(row) == dna[col + 1].charAt(row + 1)) &&
                        (dna[col + 1].charAt(row + 1) == dna[col + 2].charAt(row + 2)) &&
                        (dna[col + 2].charAt(row + 2) == dna[col + 3].charAt(row + 3))) {
                    return true;
                }
            }
        }
        // Minor diagonal ╱
        for(int col = 0; col < dna[0].length() - 3; col++) {
            for(int row = 3; row < dna.length; row++) {
                if( (dna[col].charAt(row) == dna[col + 1].charAt(row - 1)) &&
                        (dna[col + 1].charAt(row - 1) == dna[col + 2].charAt(row - 2)) &&
                        (dna[col + 2].charAt(row - 2) == dna[col + 3].charAt(row - 3))) {
                    return true;
                }
            }
        }
        return false;
    }
}
