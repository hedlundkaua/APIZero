package com.APIZero.Zero.controllers;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveCriarTarefaComtokenJwtValido() throws Exception {

        String usuarioJson = """
                {
                    "username": "todo",
                    "email": "todo@zero.com",
                    "password": "senhaSegura123"
                }
                """;
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioJson));

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = loginResult.getResponse().getContentAsString();

        String token = JsonPath.read(response, "$.accessToken");

        String todoJson = """
                {
                    "title": "Estudar Testes de integração",
                    "description": "testar roats protegidas com MocMvc",
                    "state": "DRAFT"
                }
                """;
        mockMvc.perform(post("/todos")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(todoJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
