package com.example.mutants.controller;

import com.example.mutants.dto.StatsResponse;
import com.example.mutants.service.MutantService;
import com.example.mutants.service.StatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MutantService mutantService;

    @MockBean
    private StatsService statsService;

    @Test
    void testMutantEndpoint_ReturnOk() throws Exception {
        String dnaJson = """
                {
                  "dna": ["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
                }
                """;

        when(mutantService.analyzeDna(new String[]{
                "ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"
        })).thenReturn(true);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dnaJson))
                .andExpect(status().isOk());
    }

    @Test
    void testHumanEndpoint_ReturnForbidden() throws Exception {
        String dnaJson = """
                {
                  "dna": ["ATGCGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"]
                }
                """;

        when(mutantService.analyzeDna(new String[]{
                "ATGCGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"
        })).thenReturn(false);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dnaJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void testInvalidDna_ReturnBadRequest() throws Exception {
        String dnaJson = """
                {
                  "dna": []
                }
                """;

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dnaJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testStatsEndpoint_ReturnOk() throws Exception {
        StatsResponse response = new StatsResponse(40L, 100L, 0.4);

        when(statsService.getStats()).thenReturn(response);

        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna", is(40)))
                .andExpect(jsonPath("$.count_human_dna", is(100)))
                .andExpect(jsonPath("$.ratio", is(0.4)));
    }
}
