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
        long countMutant = repository.countByIsMutant(true);
        long countHuman = repository.countByIsMutant(false);

        double ratio = (countHuman == 0)
                ? 0.0
                : (double) countMutant / countHuman;

        return new StatsResponse(countMutant, countHuman, ratio);
    }
}
