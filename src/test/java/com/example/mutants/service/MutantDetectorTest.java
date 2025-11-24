package com.example.mutants.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MutantDetectorTest {

    private final MutantDetector detector = new MutantDetector();

    // ================================
    //      TESTS MUTANTES
    // ================================

    @Test
    void testMutantHorizontal() {
        String[] dna = {
                "AAAAGT",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCTCTA",
                "TCACTG"
        };

        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testMutantVertical() {
        String[] dna = {
                "ATGCGA",
                "ATGTGC",
                "ATATGT",
                "ATAAGG",
                "ATCCTA",
                "ATCCTG"
        };

        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testMutantDiagonalDownRight() {
        String[] dna = {
                "AAGCGA",
                "CAATGC",
                "TTAAGT",
                "AGAAAG",
                "CCAATA",
                "TCACTG"
        };

        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testMutantDiagonalUpRight() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTAAGT",
                "AGTAGG",
                "CCTCTA",
                "GCACTG"
        };

        assertTrue(detector.isMutant(dna));
    }

    // ================================
    //      TESTS HUMANOS
    // ================================

    @Test
    void testHumanWithNoSequences() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TTAT",
                "AGAC"
        };

        assertFalse(detector.isMutant(dna));
    }

    @Test
    void testHumanWithOnlyOneSequence() {
        String[] dna = {
                "AAAA",
                "CAGT",
                "TTAT",
                "AGAC"
        };

        assertFalse(detector.isMutant(dna)); // solo 1 secuencia → humano
    }

    // ================================
    //      TESTS DE VALIDACIÓN
    // ================================

    @Test
    void testInvalidNonSquareMatrix() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TTAT"
        };

        assertFalse(detector.isMutant(dna));
    }

    @Test
    void testInvalidCharacters() {
        String[] dna = {
                "ATGX",
                "CAGT",
                "TTAT",
                "AGAC"
        };

        assertFalse(detector.isMutant(dna));
    }

    @Test
    void testNullDna() {
        assertFalse(detector.isMutant(null));
    }

    @Test
    void testEmptyDna() {
        String[] dna = {};
        assertFalse(detector.isMutant(dna));
    }
    @Test
    void testMutantWithMultipleHorizontalSequences() {
        String[] dna = {
                "AAAAGG",
                "CCCCTA",
                "TTATGT",
                "AGAAGG",
                "CCTCTA",
                "TCACTG"
        };
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testMutantCombinedVerticalAndDiagonal() {
        String[] dna = {
                "ATGCGA",
                "AAGTGC",
                "TAATGT",
                "AGTAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testMutant4x4Matrix() {
        String[] dna = {
                "AAAA",
                "CCCC",
                "TTAT",
                "AGAC"
        };
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testMutantExactlyTwoSequences() {
        String[] dna = {
                "AAAAGG",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(detector.isMutant(dna)); // Tiene exactamente 2 secuencias
    }

    @Test
    void testMutantLargeMatrix() {
        String[] dna = new String[10];
        for (int i = 0; i < 10; i++) {
            dna[i] = "ATCGATCGAT";
        }
        assertTrue(detector.isMutant(dna));
    }

    @Test
    void testInvalidDnaWithNullRow() {
        String[] dna = {
                "ATGC",
                null,
                "TTAT",
                "AGAC"
        };
        assertFalse(detector.isMutant(dna));
    }

    @Test
    void testInvalidDnaWithDifferentLengths() {
        String[] dna = {
                "ATGCGA",
                "CAGT",
                "TTATGT",
                "AGAAGG"
        };
        assertFalse(detector.isMutant(dna));
    }

}
