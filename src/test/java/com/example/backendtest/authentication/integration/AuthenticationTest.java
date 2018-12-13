package com.example.backendtest.authentication.integration;

import com.example.backendtest.authentication.entity.User;
import com.example.backendtest.authentication.repository.UserRepository;
import com.example.backendtest.authentication.service.PasswordHasherService;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"jwtSigningKey=secret key"})
@AutoConfigureMockMvc
public class AuthenticationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Before
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void createUser() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"foo\", \"password\": \"bar\"}")

        ).andExpect(status().isOk()).andReturn().getResponse();

        JSONObject resultObj = (JSONObject) JSONValue.parse(response.getContentAsString());
        Assert.assertEquals("foo", resultObj.getAsString("username"));
    }

    @Test
    public void createUserWithNoPasswordFails() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"foo\", \"password\": \"\"}")

        ).andExpect(status().is4xxClientError()).andReturn().getResponse();
    }

    @Test
    public void createExistingUserFails() throws Exception {
        mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"foo\", \"password\": \"bar\"}")
        ).andReturn();

        MockHttpServletResponse response = mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"foo\", \"password\": \"bar\"}")

        ).andExpect(status().is4xxClientError()).andReturn().getResponse();
    }

    @Test
    public void login() throws Exception {

        userRepository.save(new User(null, "foo", new PasswordHasherService().hashPassword("bar")));

        MockHttpServletResponse response = mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"foo\", \"password\": \"bar\"}")


        ).andExpect(status().isOk()).andReturn().getResponse();

        assertTrue(response.getContentAsString().length() > 0);
    }

    @Test
    public void loginWithWrongPasswordFails() throws Exception {

        userRepository.save(new User(null, "foo", new PasswordHasherService().hashPassword("baz")));

        mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"foo\", \"password\": \"bar\"}")

        ).andExpect(status().is(401)).andReturn();
    }
}