package com.example.mutants.controller;

import lombok.RequiredArgsConstructor;
import com.example.mutants.dto.DnaRequest;
import com.example.mutants.dto.StatsResponse;
import com.example.mutants.service.MutantService;
import com.example.mutants.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;

    @PostMapping("/mutant")
    public ResponseEntity<Void> checkMutant(@Validated @RequestBody DnaRequest request) {
        // TODO: llamar a mutantService.analyzeDna(...) y devolver 200/403
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        // TODO: llamar a statsService.getStats() y devolver los datos
        return ResponseEntity.ok(new StatsResponse(0, 0, 0.0));
    }
}
