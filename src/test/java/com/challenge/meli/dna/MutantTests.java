package com.challenge.meli.dna;

import org.junit.jupiter.api.Test;

import static com.challenge.meli.dna.DnaSolver.*;
import static org.assertj.core.api.Assertions.*;

class MutantTests {

    // Expected paths are always the As

    @Test
    void testInvalidDNAs() {
        String[] emptyDNA = new String[]{"", "", "", "", "", ""};
        String[] tooSmallDNA = new String[]{"AAA", "TTT", "CCC"};
        String[] inconsistentDNA = new String[]{"AAAA", "TTTTT", "CCCCCC", "GGGGGGG", "AAAAAAAA", "TTTTTTTT"};
        String[] validDNA = new String[]{"AAAAGA", "CAGTGC", "TTTTGT", "TGAAGG", "TCCCTA", "TCACTG"};

        assertThat(isValidDna(emptyDNA)).isFalse();
        assertThat(isValidDna(tooSmallDNA)).isFalse();
        assertThat(isValidDna(inconsistentDNA)).isFalse();

        assertThat(isValidDna(validDNA)).isTrue();
    }

    @Test
    void testHorizontalGenome() {
        String[] firstRow = new String[]{"AAAAGA", "AGAGAG", "GAGAGA", "AGAGAG", "GAGAGA", "AGAGAG"};
        assertThat(checkRows(firstRow)).isEqualTo(true);
        String[] lastRow = new String[]{"GAGAGA", "AGAGAG", "GAGAGA", "AGAGAG", "GAGAGA", "GGAAAA"};
        assertThat(checkRows(lastRow)).isTrue();
    }

    @Test
    void testColumns() {
        String[] firstColumnFirstRow = new String[]{
                "AGAGAG",
                "AAGAGA",
                "AGAGAG",
                "AAGAGA",
                "TTTTTT",
                "TTTTTT"};
        assertThat(checkColumns(firstColumnFirstRow)).isEqualTo(true);
        String[] lastColumnLastRow = new String[]{
                "AAAAAA",
                "AAAAAA",
                "GGGGGT",
                "GGGGGT",
                "TTTTTT",
                "TTTTTT"};
        assertThat(checkColumns(lastColumnLastRow)).isEqualTo(true);
        String[] smallMatrix = new String[]{
                "AGAG",
                "AAGA",
                "AGAG",
                "AAGA",};
        assertThat(checkColumns(smallMatrix)).isEqualTo(true);
        String[] invalid4x4 = new String[]{
                "GAAA",
                "GGGG",
                "GGGG",
                "AAAG",};
        assertThat(checkColumns(invalid4x4)).isEqualTo(false);
    }

    @Test
    void testMajorDiagonal() {
        // Major diagonal ╲
        String[] majorDiagonal = new String[]{
                 "AGCTGC",
                 "TATGTG",
                 "TTATGT",
                 "TGAAGG",
                 "AAAAGA",
                 "TCACTG"};
        assertThat(checkDiagonals(majorDiagonal)).isTrue();
        String[] rightBorder = new String[]{
                 "GGAGGG",
                 "CCCACC",
                 "TTTTAT",
                 "GGGGGA",
                 "CCCCCC",
                 "TTTTTT"};
        assertThat(checkDiagonals(rightBorder)).isTrue();
    }

    @Test
    void testMinorDiagonal() {
        // Minor diagonal ╱
        String[] minorDiagonal = new String[]{
                "TTTATT",
                "TTATTT",
                "TATTTT",
                "ATTTTT",
                "TTTTTT",
                "TTTTTT"};
        assertThat(checkDiagonals(minorDiagonal)).isTrue();
        String[] rightBorder = new String[]{
                "GGGGGG",
                "TTTTTT",
                "CCCCCA",
                "CCCCAC",
                "GGGAGG",
                "TTATTT"};
        assertThat(checkDiagonals(rightBorder)).isTrue();
        String[] simplest4x4 = new String[]{
                "TTTA",
                "TTAT",
                "TATT",
                "ATTT"};
        assertThat(checkDiagonals(simplest4x4)).isTrue();
    }
}
