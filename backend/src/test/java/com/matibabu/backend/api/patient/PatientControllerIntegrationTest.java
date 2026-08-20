package com.matibabu.backend.api.patient;

import com.matibabu.backend.api.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class PatientControllerIntegrationTest {

    @Autowired
    private PatientController patientController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(patientController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void registerGetListAndUpdatePatientWorkflow() throws Exception {
        // 1. Create Patient (POST /api/patients)
        String createJson = """
                {
                    "firstName": "John",
                    "lastName": "Kamau",
                    "dateOfBirth": "1995-06-15",
                    "gender": "MALE",
                    "phoneNumber": "+254712345678",
                    "address": "Nairobi"
                }
                """;

        MvcResult createResult = mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Kamau"))
                .andExpect(jsonPath("$.dateOfBirth").value("1995-06-15"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.phoneNumber").value("+254712345678"))
                .andExpect(jsonPath("$.address").value("Nairobi"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        Matcher matcher = Pattern.compile("\"id\"\\s*:\\s*\"([^\"]+)\"").matcher(responseBody);
        assertTrue(matcher.find());
        String patientId = matcher.group(1);

        // 2. Get Patient by ID (GET /api/patients/{id})
        mockMvc.perform(get("/api/patients/" + patientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patientId))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Kamau"))
                .andExpect(jsonPath("$.address").value("Nairobi"));

        // 3. List Patients (GET /api/patients?page=0&size=20)
        mockMvc.perform(get("/api/patients")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        // 4. Update Patient (PUT /api/patients/{id})
        String updateJson = """
                {
                    "firstName": "John",
                    "lastName": "Kamau",
                    "dateOfBirth": "1995-06-15",
                    "gender": "MALE",
                    "phoneNumber": "+254700000000",
                    "address": "Mombasa"
                }
                """;

        mockMvc.perform(put("/api/patients/" + patientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patientId))
                .andExpect(jsonPath("$.phoneNumber").value("+254700000000"))
                .andExpect(jsonPath("$.address").value("Mombasa"));

        // 5. Verify updated fields persisted on GET
        mockMvc.perform(get("/api/patients/" + patientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patientId))
                .andExpect(jsonPath("$.phoneNumber").value("+254700000000"))
                .andExpect(jsonPath("$.address").value("Mombasa"));

        // 6. Delete Patient (DELETE /api/patients/{id})
        mockMvc.perform(delete("/api/patients/" + patientId))
                .andExpect(status().isNoContent());

        // 7. Verify Patient no longer exists on GET (404)
        mockMvc.perform(get("/api/patients/" + patientId))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteNonExistentPatientReturns404() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        mockMvc.perform(delete("/api/patients/" + nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getNonExistentPatientReturns404() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        mockMvc.perform(get("/api/patients/" + nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateNonExistentPatientReturns404() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        String updateJson = """
                {
                    "firstName": "John",
                    "lastName": "Kamau",
                    "dateOfBirth": "1995-06-15",
                    "gender": "MALE",
                    "phoneNumber": "+254700000000",
                    "address": "Mombasa"
                }
                """;

        mockMvc.perform(put("/api/patients/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void createPatientWithInvalidDataReturns400() throws Exception {
        String invalidJson = """
                {
                    "firstName": "",
                    "lastName": "",
                    "dateOfBirth": "2999-01-01",
                    "gender": "MALE",
                    "phoneNumber": "",
                    "address": ""
                }
                """;

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
