package com.project.web;

import com.project.web.api.ApiController;
import com.project.web.ui.UiController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({UiController.class, ApiController.class})
@Import(SecurityConfig.class)
public class SecurityConfigTest {

    @Autowired
    MockMvc mockMvc;

    // --- Проверка доступа к публичным ресурсам ---

    @Test
    void staticCss_shouldBeAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/style.css"))
                .andExpect(status().isOk());
    }

    @Test
    void staticJs_shouldBeAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/script.js"))
                .andExpect(status().isOk());
    }

    @Test
    void apiCalculate_shouldBeAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/calculate")
                        .param("intensity0", "100")
                        .param("phi", "0"))
                .andExpect(status().isOk());
    }

    // --- Проверка, что защищённые страницы требуют авторизации ---

    @Test
    void rootPage_shouldRedirectToLogin_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    // --- Проверка формы логина ---

    @Test
    void loginPage_shouldBeAccessible() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void login_withCorrectCredentials_shouldRedirectToRoot() throws Exception {
        mockMvc.perform(formLogin().user("студент").password("12345"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void login_withWrongPassword_shouldFail() throws Exception {
        mockMvc.perform(formLogin().user("студент").password("wrong"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    // --- Проверка авторизованного доступа ---

    @Test
    @WithMockUser(username = "студент", roles = "USER")
    void rootPage_shouldBeAccessible_whenAuthenticated() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "препод", roles = "ADMIN")
    void rootPage_shouldBeAccessible_forAdmin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }
}
