package com.example.mutants.service;

import com.example.mutants.entity.DnaRecord;
import com.example.mutants.repository.DnaRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private DnaRecordRepository repository;

    @Mock
    private MutantDetector detector;

    @InjectMocks
    private MutantService service;

    @Test
    void analyzeDna_returnsCachedResultIfExists() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TTAT",
                "AGAC"
        };

        DnaRecord record = new DnaRecord();
        record.setDnaHash("hash123");
        record.setIsMutant(true);

        when(repository.findByDnaHash(anyString())).thenReturn(Optional.of(record));

        boolean result = service.analyzeDna(dna);

        assertTrue(result);
        verify(detector, never()).isMutant(any());
        verify(repository, never()).save(any());
    }

    @Test
    void analyzeDna_calculatesAndSavesWhenNotCached() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        when(repository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(detector.isMutant(dna)).thenReturn(true);

        boolean result = service.analyzeDna(dna);

        assertTrue(result);

        ArgumentCaptor<DnaRecord> captor = ArgumentCaptor.forClass(DnaRecord.class);
        verify(repository).save(captor.capture());

        DnaRecord saved = captor.getValue();
        assertNotNull(saved.getDnaHash());
        assertTrue(saved.isMutant());
        assertNotNull(saved.getCreatedAt());
    }

    @Test
    void analyzeDna_handlesHumanDnaAndSaves() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TTAT",
                "AGAC"
        };

        when(repository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(detector.isMutant(dna)).thenReturn(false);

        boolean result = service.analyzeDna(dna);

        assertFalse(result);
        verify(repository).save(any(DnaRecord.class));
    }
    @Test
    void analyzeDna_handlesDuplicateHashCorrectly() {
        String[] dna1 = {"ATGC", "CAGT", "TTAT", "AGAC"};
        String[] dna2 = {"ATGC", "CAGT", "TTAT", "AGAC"}; // Mismo DNA

        when(repository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(detector.isMutant(any())).thenReturn(false);

        // Primera llamada
        service.analyzeDna(dna1);

        // Segunda llamada con mismo DNA debería usar caché
        when(repository.findByDnaHash(anyString())).thenReturn(Optional.of(new DnaRecord()));
        service.analyzeDna(dna2);

        verify(repository, times(1)).save(any()); // Solo guarda una vez
    }

    @Test
    void analyzeDna_handlesMultipleDifferentDnas() {
        String[] dna1 = {"AAAA", "CCCC", "TTAT", "AGAC"};
        String[] dna2 = {"TTTT", "GGGG", "ATAT", "CGCG"};

        when(repository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(detector.isMutant(dna1)).thenReturn(true);
        when(detector.isMutant(dna2)).thenReturn(true);

        boolean result1 = service.analyzeDna(dna1);
        boolean result2 = service.analyzeDna(dna2);

        assertTrue(result1);
        assertTrue(result2);
        verify(repository, times(2)).save(any());
    }
}
