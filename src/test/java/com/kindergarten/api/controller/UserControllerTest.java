package com.kindergarten.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindergarten.api.common.reqeust.RequestLoginUser;
import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.model.request.SignUpRequest;
import com.kindergarten.api.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    UserService userService;


    User user;

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
        this.user = new User();
        this.user.setUserid("testuser");
        this.user.setPassword("password");
        this.user.setName("테스트유저");
        this.user.setEmail("test@test.com");
        this.user.setPhone("010-1234-1234");

        userService.signUpParent(user);
    }

    @Test
    public void 회원가입_성공() throws Exception {
        //given
        final SignUpRequest dto = new SignUpRequest("test", "password", "이름", "01012341234", "test@test.com");

        //when
        final ResultActions resultActions = mockMvc.perform(post("/api/users/parent")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data").exists());

    }

    @Test
    public void 회원가입_중복회원_실패() throws Exception {

        //given
        final SignUpRequest dto = new SignUpRequest("testuser", "password", "이름", "01012341234", "test@test.com");

        //when
        final ResultActions resultActions2 = mockMvc.perform(post("/api/users/parent")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());

        //then
        resultActions2
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(409))
                .andExpect(jsonPath("$.msg").exists());
    }

    @Test
    public void 로그인_성공() throws Exception {
        //given
        RequestLoginUser loginUser = new RequestLoginUser(user.getUserid(), "password");

        final ResultActions resultActions = mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("accessToken"))
                .andExpect(cookie().exists("refreshToken"));
    }

    @Test
    public void 로그인_실패() throws Exception {
        //given
        RequestLoginUser loginUser = new RequestLoginUser(user.getUserid(), user.getPassword());

        final ResultActions resultActions = mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("error"))
                .andExpect(jsonPath("$.message").value("실패"));
    }
}