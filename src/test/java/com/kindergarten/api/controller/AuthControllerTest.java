package com.kindergarten.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindergarten.api.users.UserDTO;
import com.kindergarten.api.users.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("dev")
public class AuthControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    UserService userService;


    UserDTO.Create create;

    @Before
    public void setup() {
        //mock mvc 한글 깨짐 처리
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

    }

    @Before
    public void initMember() {
        this.create = new UserDTO.Create();
        this.create.setUserid("testuser");
        this.create.setPassword("password");
        this.create.setName("테스트유저");
        this.create.setRole("USER");
        this.create.setEmail("test@test.com");
        this.create.setPhone("010-1234-1234");
    }

    @Test
    public void 로그인_성공() throws Exception {
        //given
        userService.signUpParent(create);
        UserDTO.Login loginUser = new UserDTO.Login();
        loginUser.setUserid(create.getUserid());
        loginUser.setPassword(create.getPassword());

        //when
        final ResultActions resultActions = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginUser)))
                .andDo(print());
        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(cookie().exists("accessToken"))
                .andExpect(cookie().exists("refreshToken"));
    }

    @Test
    public void 로그인_실패_존재하지않는계정() throws Exception {
        //given
        UserDTO.Login loginUser = new UserDTO.Login();
        loginUser.setUserid(create.getUserid());
        loginUser.setPassword(create.getPassword());


        final ResultActions resultActions = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginUser)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.msg").exists());
    }

    @Test
    public void 로그인_실패_비밀번호틀림() throws Exception {
        //given
        userService.signUpParent(create);

        UserDTO.Login loginUser = new UserDTO.Login();
        loginUser.setUserid(create.getUserid());
        loginUser.setPassword("notpassword");


        final ResultActions resultActions = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginUser)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.msg").exists());
    }
}