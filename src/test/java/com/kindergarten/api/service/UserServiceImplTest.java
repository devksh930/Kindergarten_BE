package com.kindergarten.api.service;

import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.model.entity.UserRole;
import com.kindergarten.api.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceImplTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void signUpParent() {
        //given
        User inituser = User.builder()
                .userid("user1")
                .password("password")
                .name("테스트")
                .phone("010-1234-1234")
                .email("test@test.com").build();
        //        when

        userService.signUpParent(inituser);
        Optional<User> finduser = userRepository.findByUserid("user1");
        //        then

        Assertions.assertThat(finduser.get().getRole()).isEqualTo(UserRole.ROLE_USER);
        Assertions.assertThat(finduser.get().getName()).isEqualTo(inituser.getName());

    }


    @Test
    public void signUpTeacher() {

        //given
        User inituser = User.builder()
                .userid("user1")
                .password("password")
                .name("테스트")
                .phone("010-1234-1234")
                .email("test@test.com").build();
        //        when

        userService.signUpTeacher(inituser);
        Optional<User> userid = userRepository.findByUserid("user1");

        //        then

        Assertions.assertThat(userid.get().getRole()).isEqualTo(UserRole.ROLE_NOT_PERMITTED_TEACHER);
    }

    @Test
    public void signUpDirector() {

        //given
        User inituser = User.builder()
                .userid("user1")
                .password("password")
                .name("테스트")
                .phone("010-1234-1234")
                .email("test@test.com").build();
        //        when

        userService.signUpDirector(inituser);
        Optional<User> userid = userRepository.findByUserid("user1");


        //        then

        Assertions.assertThat(userid.get().getRole()).isEqualTo(UserRole.ROLE_NOT_PERMITTED_DIRECTOR);
    }


    @Test
    public void loginUser() throws Exception {

        User inituser = User.builder()
                .userid("user1")
                .password("password")
                .name("테스트")
                .phone("010-1234-1234")
                .email("test@test.com").build();
        userService.signUpParent(inituser);

        User user = userService.loginUser("user1", "password");

        Assertions.assertThat(inituser.getUserid()).isEqualTo(user.getUserid());
    }
}