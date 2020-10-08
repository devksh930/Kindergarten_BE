package com.kindergarten.api.repository;

import com.kindergarten.api.model.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;


    @Test
    public void createUser() throws Exception {
        //given
        User inituser = User.builder()
                .userid("user1")
                .password("password")
                .name("테스트")
                .phone("010-1234-1234")
                .email("test@test.com").build();
        userRepository.save(inituser);

        //when
        User findUser = userRepository.findByUserid(inituser.getUserid()).get();

        //then
        Assertions.assertThat(findUser.getUserid()).isEqualTo(inituser.getUserid());
        Assertions.assertThat(findUser.getName()).isEqualTo(inituser.getName());
    }

    @Test
    public void updateUser() throws Exception {
        //given
        User inituser = User.builder()
                .userid("user1")
                .password("password")
                .name("테스트")
                .phone("010-1234-1234")
                .email("test@test.com").build();
        userRepository.save(inituser);

        //when
        String changeName = "이름변경";
        User updateuser = userRepository.findByUserid("user1").get();
        updateuser.setName(changeName);
        userRepository.save(updateuser);
        User findUser = userRepository.findByUserid("user1").get();

        //then
        Assertions.assertThat(findUser.getName()).isEqualTo(changeName);
    }

    @Test
    public void deleteUser() throws Exception {
        //given
        User inituser1 = User.builder()
                .userid("user1")
                .password("password")
                .name("테스트1")
                .phone("010-1234-1234")
                .email("test@test.com").build();
        userRepository.save(inituser1);

        User inituser2 = User.builder()
                .userid("user2")
                .password("password")
                .name("테스트2")
                .phone("010-1234-1234")
                .email("test@test.com").build();
        userRepository.save(inituser2);

        //when

        //지우기전 User List
        List<User> beforeDelete = userRepository.findAll();

        Assertions.assertThat(beforeDelete.size()).isEqualTo(2);

        User user = userRepository.findByUserid("user1").get();
        userRepository.deleteById(user.getId());

        //지운후 User List
        List<User> afterDelete = userRepository.findAll();
        //then
        Assertions.assertThat(afterDelete.size()).isEqualTo(1);


    }
}