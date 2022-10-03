package ru.skypro.homework.controller;

import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skypro.homework.service.impl.AuthServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.controller.Constants.*;

@WebMvcTest(controllers = AuthController.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthServiceImpl authService;

    @InjectMocks
    private AuthController authController;

    @Test
    public void contextLoads() {
        assertThat(authController).isNotNull();
    }

    private final JSONObject req = new JSONObject();

    @Before
    public void init() {
        req.put("username", AUTHORS_EMAIL);
        req.put("password", "password");
    }


    @Test
    public void testLoginForbidden() throws Exception {
        when(authService.login(any(), any())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("http://localhost:8080/login")
                        .content(req.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    public void testLoginOk() throws Exception {
        when(authService.login(AUTHORS_EMAIL, "password")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("http://localhost:8080/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
