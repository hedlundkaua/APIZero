package com.APIZero.Zero.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.health.actuate.endpoint.StatusAggregator;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") //
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StatusAggregator statusAggregator;

    @Test
    void deveCriarUtilizadorERetornar201() throws Exception{

        String novoUsuarioJson = """
                {
                    "username": "test",
                    "email": "test@zero.com",
                    "password": "senhaSegura123"
                }
                """;

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(novoUsuarioJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
