package ru.skypro.homework.controller;

import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.service.impl.AuthServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.controller.Constants.AUTHORS_EMAIL;

@WebMvcTest(controllers = AuthController.class)
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@ContextConfiguration
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @SpyBean
    private AuthServiceImpl authService;
    @MockBean
    private UserDetailsManager manager;
    @MockBean
    private PasswordEncoder encoder;
    @MockBean
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @Test
    public void contextLoads() {
        assertThat(authController).isNotNull();
    }

    private final JSONObject req = new JSONObject();
    private final JSONObject register = new JSONObject();

    @Before
    public void init() {
        req.put("username", AUTHORS_EMAIL);
        req.put("password", "password");

        register.put("username", "user");
        register.put("password", "password2");
        register.put("lastName", "LastName");
        register.put("phone", "+7888555469");
        register.put("role", "USER");

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
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
                        .content(req.toString()))
                .andExpect(status().isOk());
    }

//    @Test
//    public void loggingInWithRightUser() throws Exception {
//
//        mockMvc.perform(formLogin()
//                        .user("ed2408@yandex.ru").password("password1"))
//                .andExpect(status().isOk())
//                .andExpect(authenticated());
//    }

    @Test
    public void testRegister() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .post("http://localhost:8080/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(register.toString()))
                .andExpect(status().isOk());
    }


}
