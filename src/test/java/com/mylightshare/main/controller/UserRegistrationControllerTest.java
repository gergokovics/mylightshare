package com.mylightshare.main.controller;

import com.mylightshare.main.com.mylightshare.main.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserRegistrationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    // CSRF Token
    private HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository;
    private CsrfToken csrfToken;
    private static final String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";

    @Before
    public void setup() {

        httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
        csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void registerGet_anonymous_shouldSucceedWithOkStatus() throws Exception {
        mvc.perform(get("/register")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

    @WithMockUser("testuser")
    @Test
    public void registerGet_authorized_shouldRedirect() throws Exception {
        mvc.perform(get("/register")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(redirectedUrl("/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void registerPost_shouldSucceedWithOkStatus() throws Exception {

        User user = new User();

        user.setUsername("testuser");
        user.setEmail("invalid");
        user.setPassword("password");

        mvc.perform(post("/register")
                .flashAttr("user", user)
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken()))
                .andExpect(status().isOk());
    }

    @Test
    public void confirmAccountGet_anonymous_shouldShowErrorPage() throws  Exception {
        mvc.perform(get("/confirm-account")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().is4xxClientError());
    }

    @WithMockUser("testuser")
    @Test
    public void confirmAccountGet_authorized_shouldShowErrorPage() throws  Exception {
        mvc.perform(get("/confirm-account")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().is4xxClientError());
    }
}
