package com.mylightshare.main.controller;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileControllerTest {

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
                .defaultRequest(get("/").with(user("testuser")))
                .apply(springSecurity())
                .build();
    }

    @Test
    public void uploadGet_shouldSucceedWithOkStatus() throws Exception {
        mvc.perform(get("/upload")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void searchGet_shouldSucceedWithOkStatus() throws Exception {
        mvc.perform(get("/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("term", "test-search"))
                .andExpect(status().isOk());
    }

    @Test
    public void filePageGet_shouldSucceedWithOkStatus() throws Exception {
        mvc.perform(get("/files/page=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void sortGet_shouldSucceedWithOkStatus() throws Exception {
        mvc.perform(get("/files")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get("/files")
                .contentType(MediaType.APPLICATION_JSON)
                .param("sort",  "last-download")
                .param("order",  "desc"))
                .andExpect(status().isOk());

        mvc.perform(get("/files")
                .contentType(MediaType.APPLICATION_JSON)
                .param("sort",  "upload-time")
                .param("order",  "asc"))
                .andExpect(status().isOk());
    }

    @Test
    public void downloadGet_shouldReturnJsonWithOkStatus() throws Exception {
        mvc.perform(get("/download/Vw6i6mek")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void filePost_And_FileDelete_shouldSucceedWithOkStatus() throws Exception {
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("file",
                        "test-file-original",
                        "text/plain",
                        "testdata".getBytes());

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.multipart("/upload")
                        .file(mockMultipartFile)
                        .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                        .param(csrfToken.getParameterName(), csrfToken.getToken()))
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String mockFileId = result.getResponse().getContentAsString();

        mvc.perform(delete("/" + mockFileId)
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken()))
                .andExpect(status().isOk());
    }

}
