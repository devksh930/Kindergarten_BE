package com.kindergarten.api.service;

import com.kindergarten.api.users.UserDTO;
import com.kindergarten.api.users.User;
import com.kindergarten.api.users.UserRole;
import com.kindergarten.api.users.UserRepository;
import com.kindergarten.api.security.util.JwtTokenProvider;
import com.kindergarten.api.users.UserService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("dev")
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    UserDTO.Create create;

    @Before
    public void initMember() {
        this.create = new UserDTO.Create();
        this.create.setUserid("testuser");
        this.create.setPassword("password");
        this.create.setName("테스트유저");
        this.create.setEmail("test@test.com");
        this.create.setPhone("010-1234-1234");
        this.create.setRole("USER");

    }

    @Test
    public void signUpParent() {
        //given
        UserDTO.Create create = this.create;
        //when

        userService.signUpParent(create);
        User finduser = userRepository.findByUserid(create.getUserid()).get();

        //then
        Assertions.assertThat(finduser.getRole()).isEqualTo(UserRole.ROLE_USER);
        Assertions.assertThat(finduser.getName()).isEqualTo(create.getName());

    }


    @Test
    public void signUpTeacher() {

        //given
        UserDTO.Create create = this.create;
        create.setRole("TEACHER");
        create.setKindergarten_id("1");

        //when
        userService.signUpTeacher(create);
        User finduser = userRepository.findByUserid(create.getUserid()).get();

        //then
        Assertions.assertThat(finduser.getRole()).isEqualTo(UserRole.ROLE_NOT_PERMITTED_TEACHER);
        Assertions.assertThat(finduser.getName()).isEqualTo(create.getName());

    }

    @Test
    public void signUpDirector() {

        //given
        UserDTO.Create create = this.create;
        create.setRole("DIRECTOR");
        create.setKindergarten_id("1");


        //when
        userService.signUpTeacher(create);
        User finduser = userRepository.findByUserid(create.getUserid()).get();


        //then
        Assertions.assertThat(finduser.getRole()).isEqualTo(UserRole.ROLE_NOT_PERMITTED_DIRECTOR);
        Assertions.assertThat(finduser.getName()).isEqualTo(create.getName());
    }


    @Test
    public void loginUser() {
        // given
        userService.signUpParent(this.create);

        //when
        UserDTO.Login_response response = userService.loginUser(create.getUserid(), create.getPassword());
        String userPk = jwtTokenProvider.getUserId(response.getToken());
        //then
        Assertions.assertThat(create.getUserid()).isEqualTo(userPk);
    }
}