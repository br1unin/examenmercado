package com.example.mutants.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Estadísticas de verificaciones de ADN")
public class StatsResponse {

    @Schema(
            description = "Cantidad de ADN mutante verificados",
            example = "40"
    )
    private long count_mutant_dna;

    @Schema(
            description = "Cantidad de ADN humano verificados",
            example = "100"
    )
    private long count_human_dna;

    @Schema(
            description = "Ratio entre mutantes y humanos (mutantes / humanos). Retorna 0.0 si no hay humanos",
            example = "0.4"
    )
    private double ratio;
}