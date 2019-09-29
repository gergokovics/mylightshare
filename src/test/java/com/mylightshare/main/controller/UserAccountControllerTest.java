package com.mylightshare.main.controller;

import com.mylightshare.main.com.mylightshare.main.formview.AccountUpdateForm;
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
public class UserAccountControllerTest {

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
    public void loginGet_anonymous_shouldSucceedWithOkStatus() throws Exception {
        mvc.perform(get("/login")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

    @WithMockUser("testuser")
    @Test
    public void loginGet_authorized_shouldRedirect() throws Exception {
        mvc.perform(get("/login")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(redirectedUrl("/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void passwordRecoveryGet_anonymous_shouldSucceedWithOkStatus() throws Exception {
        mvc.perform(get("/password-recovery")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

    @WithMockUser("testuser")
    @Test
    public void passwordRecoveryGet_authorized_shouldRedirect() throws Exception {
        mvc.perform(get("/password-recovery")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(redirectedUrl("/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void passwordRecoveryPost_shouldSucceedWithOkStatus() throws Exception {
        mvc.perform(post("/password-recovery")
                .flashAttr("email", "mylightshare@gmail.com")
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken()))
                .andExpect(status().isOk());
    }

    @WithMockUser("testuser")
    @Test
    public void accountSettingsGet_shouldSucceedWithOkStatus() throws Exception {
        mvc.perform(get("/account-settings")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

    @WithMockUser("testuser")
    @Test
    public void accountSettingsPost_shouldRedirect() throws Exception {
        AccountUpdateForm accountUpdateForm = new AccountUpdateForm();

        accountUpdateForm.setUsername("testuser");
        accountUpdateForm.setEmail("mylightshare@gmail.com");
        accountUpdateForm.setNewPassword("testpass");
        accountUpdateForm.setCurrentPassword("testpass");

        mvc.perform(post("/account-settings")
                .flashAttr("accountUpdateForm", accountUpdateForm)
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken()))
                .andExpect(redirectedUrl("/account-settings"))
                .andExpect(status().is3xxRedirection());
    }

}
