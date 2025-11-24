package com.example.mutants.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import com.example.mutants.dto.DnaRequest;
import com.example.mutants.dto.StatsResponse;
import com.example.mutants.service.MutantService;
import com.example.mutants.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Mutant Detector", description = "API para detección de mutantes mediante análisis de ADN")
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;

    @PostMapping("/mutant")
    @Operation(
            summary = "Verificar si un ADN es mutante",
            description = "Analiza una secuencia de ADN representada como una matriz NxN y determina si corresponde a un mutante. Un humano es mutante si se encuentran más de una secuencia de cuatro letras iguales de forma horizontal, vertical u oblicua."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Es mutante - Se detectaron 2 o más secuencias de 4 letras iguales"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "No es mutante - Se detectó 0 o 1 secuencia de 4 letras iguales"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ADN inválido - La matriz no es cuadrada, contiene caracteres inválidos o está vacía",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<Void> checkMutant(
            @Validated
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Secuencia de ADN a verificar",
                    required = true
            )
            @RequestBody DnaRequest request) {

        boolean isMutant = mutantService.analyzeDna(request.getDna());

        return isMutant
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/stats")
    @Operation(
            summary = "Obtener estadísticas de verificaciones",
            description = "Retorna estadísticas de las verificaciones de ADN realizadas, incluyendo el conteo de mutantes, humanos y su ratio"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estadísticas obtenidas exitosamente",
                    content = @Content(schema = @Schema(implementation = StatsResponse.class))
            )
    })
    public ResponseEntity<StatsResponse> getStats() {
        StatsResponse stats = statsService.getStats();
        return ResponseEntity.ok(stats);
    }

    // Clase interna para documentación de errores
    @Schema(description = "Respuesta de error")
    private static class ErrorResponse {
        @Schema(description = "Nombre del campo con error", example = "dna")
        private String field;

        @Schema(description = "Mensaje de error", example = "Secuencia de ADN inválida")
        private String message;
    }
}