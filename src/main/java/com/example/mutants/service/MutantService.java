package com.example.mutants.service;

import com.example.mutants.entity.DnaRecord;
import com.example.mutants.exception.DnaHashCalculationException;
import com.example.mutants.repository.DnaRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class MutantService {

    private final DnaRecordRepository repository;
    private final MutantDetector mutantDetector;

    public boolean analyzeDna(String[] dna) {
        String hash = calculateDnaHash(dna);

        return repository.findByDnaHash(hash)
                .map(DnaRecord::isMutant)
                .orElseGet(() -> {
                    boolean isMutant = mutantDetector.isMutant(dna);
                    DnaRecord record = new DnaRecord();
                    record.setDnaHash(hash);
                    record.setIsMutant(isMutant);
                    record.setCreatedAt(LocalDateTime.now());
                    repository.save(record);
                    return isMutant;
                });
    }

    private String calculateDnaHash(String[] dna) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String dnaString = String.join("", dna);
            byte[] hashBytes = digest.digest(dnaString.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                String h = Integer.toHexString(0xff & b);
                if (h.length() == 1) sb.append('0');
                sb.append(h);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new DnaHashCalculationException("Error calculando hash", e);
        }
    }
}
