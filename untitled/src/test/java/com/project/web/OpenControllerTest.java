package com.project.web;

import com.jayway.jsonpath.JsonPath;
import com.project.web.api.ApiController;
import com.project.web.ui.UiController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({UiController.class, ApiController.class})
@Import(SecurityConfig.class)
public class OpenControllerTest {

    @Autowired
    MockMvc mockMvc;

    // --- тесты главной страницы (требует авторизации) ---

    @Test
    @WithMockUser(username = "студент", roles = "USER")
    void index_shouldReturn200_whenAuthenticated() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void index_shouldRenderIndexTemplate() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(view().name("index"));
    }

    @Test
    void index_shouldReturn302_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection());
    }

    // --- тесты API (открыт без авторизации) ---

    @Test
    void calculateApi_phi0_returnsHalfIntensity() throws Exception {
        mockMvc.perform(get("/api/calculate")
                        .param("intensity0", "100")
                        .param("phi", "0"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    double value = ((Number) JsonPath.read(
                            result.getResponse().getContentAsString(), "$.result")).doubleValue();
                    assertEquals(50.5, value, 1.0);
                });
    }

    @Test
    void calculateApi_phi90_returnsZero() throws Exception {
        mockMvc.perform(get("/api/calculate")
                        .param("intensity0", "100")
                        .param("phi", "90"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    double value = ((Number) JsonPath.read(
                            result.getResponse().getContentAsString(), "$.result")).doubleValue();
                    assertEquals(0.5, value, 1.0);
                });
    }

    @Test
    void calculateApi_missingParam_returns400() throws Exception {
        mockMvc.perform(get("/api/calculate")
                        .param("intensity0", "100"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void index_modelContainsModelAttribute() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(model().attributeExists("model"));
    }

    @Test
    void calculateApi_phi45_returnsQuarterIntensity() throws Exception {
        mockMvc.perform(get("/api/calculate")
                        .param("intensity0", "100")
                        .param("phi", "45"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    double value = ((Number) JsonPath.read(
                            result.getResponse().getContentAsString(), "$.result")).doubleValue();
                    assertEquals(25.5, value, 1.0);
                });
    }
}