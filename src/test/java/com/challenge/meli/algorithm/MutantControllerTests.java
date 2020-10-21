package com.challenge.meli.algorithm;

import org.junit.jupiter.api.Test;

import static com.challenge.meli.algorithm.DnaSolver.*;
import static org.assertj.core.api.Assertions.*;

class MutantControllerTests {

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
        String[] firstRow = new String[]{"AAAAGT", "TGTGTG", "GTGTGT", "TGTGTG", "GTGTGT", "TGTGTG"};
        assertThat(checkRows(firstRow)).isEqualTo(true);
        String[] lastRow = new String[]{"TGTGTG", "CTCTCT", "GTGTGT", "TGTGTG", "CGCGCG", "GGAAAA"};
        assertThat(checkRows(lastRow)).isTrue();
        String[] thirdRow = new String[]{"TGTGTG", "CTCTCT", "GAAAAG", "TGTGTG", "CGCGCG", "TGTGTG"};
        assertThat(checkRows(thirdRow)).isTrue();
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
                "TTTTTT",
                "TTTTTT",
                "GGGGGA",
                "GGGGGA",
                "TTTTTA",
                "TTTTTA"};
        assertThat(checkColumns(lastColumnLastRow)).isEqualTo(true);
        String[] firstColumn4x4 = new String[]{
                "AGGG",
                "ACCC",
                "ATTT",
                "AGGG",};
        assertThat(checkColumns(firstColumn4x4)).isEqualTo(true);
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
        String[] firstColumnFirstRow = new String[]{
                 "ATTTTT",
                 "TATTTT",
                 "CCACCC",
                 "CCCACC",
                 "TTTTTT",
                 "TTTTTT"};
        assertThat(checkDiagonals(firstColumnFirstRow)).isTrue();
        String[] thirdColumnFirstRow = new String[]{
                 "GGAGGG",
                 "CCCACC",
                 "TTTTAT",
                 "GGGGGA",
                 "CCCCCC",
                 "TTTTTT"};
        assertThat(checkDiagonals(thirdColumnFirstRow)).isTrue();
        String[] firstColumnThirdRow = new String[]{
                "TTTTTT",
                "GGGGGG",
                "ATTTTT",
                "GAGGGG",
                "CCACCC",
                "TTTATT"};
        assertThat(checkDiagonals(firstColumnThirdRow)).isTrue();
        String[] noMajorDiagonal = new String[]{
                "TTTTTT",
                "GGGGGG",
                "TTTTTT",
                "GAGGGG",
                "CCACCC",
                "TTTATT"};
        assertThat(checkDiagonals(noMajorDiagonal)).isFalse();
    }

    @Test
    void testMinorDiagonal() {
        // Minor diagonal ╱
        String[] firstColumnFourthRow = new String[]{
                "TTTATT",
                "TTATTT",
                "GAGGGG",
                "AGGGGG",
                "CCCCCC",
                "CCCCCC"};
        assertThat(checkDiagonals(firstColumnFourthRow)).isTrue();
        String[] thirdColumnLastRow = new String[]{
                "GGGGGG",
                "TTTTTT",
                "CCCCCA",
                "CCCCAC",
                "GGGAGG",
                "TTATTT"};
        assertThat(checkDiagonals(thirdColumnLastRow)).isTrue();
        String[] simple4x4 = new String[]{
                "TTTA",
                "TTAT",
                "TATT",
                "ATTT"};
        assertThat(checkDiagonals(simple4x4)).isTrue();
    }
}
