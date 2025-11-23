package com.example.mutants.service;

import org.springframework.stereotype.Service;

import java.util.Set;



@Service
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4;
    private static final Set<Character> VALID_BASES = Set.of('A','T','C','G');

    public boolean isMutant(String[] dna) {
        if (!isValidDna(dna)) return false;

        int n = dna.length;
        int sequenceCount = 0;

        char[][] matrix = new char[n][];
        for (int i = 0; i < n; i++) {
            matrix[i] = dna[i].toCharArray();
        }

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {

                // horizontal
                if (col <= n - SEQUENCE_LENGTH &&
                    checkHorizontal(matrix, row, col)) {
                    if (++sequenceCount > 1) return true;
                }

                // vertical
                if (row <= n - SEQUENCE_LENGTH &&
                    checkVertical(matrix, row, col)) {
                    if (++sequenceCount > 1) return true;
                }

                // diagonal ↘
                if (row <= n - SEQUENCE_LENGTH &&
                    col <= n - SEQUENCE_LENGTH &&
                    checkDiagonalDown(matrix, row, col)) {
                    if (++sequenceCount > 1) return true;
                }

                // diagonal ↙
                if (row <= n - SEQUENCE_LENGTH &&
                    col >= SEQUENCE_LENGTH - 1 &&
                    checkDiagonalUp(matrix, row, col)) {
                    if (++sequenceCount > 1) return true;
                }
            }
        }
        return false;
    }

    private boolean isValidDna(String[] dna) {
        if (dna == null || dna.length == 0) return false;
        int n = dna.length;
        for (String row : dna) {
            if (row == null || row.length() != n) return false;
            for (char c : row.toCharArray()) {
                if (!VALID_BASES.contains(c)) return false;
            }
        }
        return true;
    }

    private boolean checkHorizontal(char[][] m, int r, int c) {
        char b = m[r][c];
        return m[r][c+1]==b && m[r][c+2]==b && m[r][c+3]==b;
    }

    private boolean checkVertical(char[][] m, int r, int c) {
        char b = m[r][c];
        return m[r+1][c]==b && m[r+2][c]==b && m[r+3][c]==b;
    }

    private boolean checkDiagonalDown(char[][] m, int r, int c) {
        char b = m[r][c];
        return m[r+1][c+1]==b && m[r+2][c+2]==b && m[r+3][c+3]==b;
    }

    private boolean checkDiagonalUp(char[][] m, int r, int c) {
        char b = m[r][c];
        return m[r+1][c-1]==b && m[r+2][c-2]==b && m[r+3][c-3]==b;
    }
}
