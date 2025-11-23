package com.example.mutants.service;

import lombok.RequiredArgsConstructor;
import com.example.mutants.dto.StatsResponse;
import com.example.mutants.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final DnaRecordRepository repository;

    public StatsResponse getStats() {
        // TODO: usar countByIsMutant(true/false) y calcular ratio
        return new StatsResponse(0, 0, 0.0);
    }
}
