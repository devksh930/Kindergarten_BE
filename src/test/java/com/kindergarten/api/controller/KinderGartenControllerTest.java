package com.kindergarten.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindergarten.api.model.dto.KinderGartenDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("dev")
public class KinderGartenControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        //mock mvc 한글 깨짐 처리
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

    }

    KinderGartenDTO.UserCreate userCreateKinder;
    KinderGartenDTO.KinderGartenDetail detail;

    @Test
    public void signupfindbyName() throws Exception {
        final ResultActions resultActions = mockMvc.perform(get("/api/kindergartens/name").param("name", "유치원").param("pageSize", "20")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userCreateKinder)));
        System.out.println(resultActions);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.totalPage").isNumber())
                .andExpect(jsonPath("$.data.currentpage").isNumber())
                .andExpect(jsonPath("$.data.kinderGartens").isArray());

    }

    @Test
    public void sginupfindbyAdress() throws Exception {
        final ResultActions resultActions = mockMvc.perform(get("/api/kindergartens/addr").param("addr", "부산").param("pageSize", "20")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userCreateKinder)));
        System.out.println(resultActions);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.totalPage").isNumber())
                .andExpect(jsonPath("$.data.currentpage").isNumber())
                .andExpect(jsonPath("$.data.kinderGartens").isArray());
    }

    @Test
    public void detailKindergarten() throws Exception {
        final ResultActions resultActions = mockMvc.perform(get("/api/kindergartens/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(detail)));
        System.out.println(resultActions);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.id").value(1));
    }

}
