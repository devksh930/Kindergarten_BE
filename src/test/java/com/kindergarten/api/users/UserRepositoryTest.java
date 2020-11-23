package com.kindergarten.api.users;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("dev")
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @AfterEach
    public void cleanup() { // 데이터 섞임 방지
        userRepository.deleteAll();
    }

    @Test
    public void createUser() throws Exception {
        //given
        User inituser = new User();

        inituser.setUserid("user1");
        inituser.setPassword("password");
        inituser.setName("테스트");
        inituser.setPhone("010-1234-1234");
        inituser.setEmail("test@test.com");
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
        User inituser = new User();

        inituser.setUserid("user1");
        inituser.setPassword("password");
        inituser.setName("테스트");
        inituser.setPhone("010-1234-1234");
        inituser.setEmail("test@test.com");
        userRepository.save(inituser);
        ;

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
        User deleteUser = new User();
        deleteUser.setId(99999L);
        deleteUser.setUserid("user2");
        deleteUser.setPassword("password");
        deleteUser.setName("테스트");
        deleteUser.setPhone("010-1234-1234");
        deleteUser.setEmail("test@test.com");
        userRepository.save(deleteUser);

        //when
        userRepository.delete(deleteUser);

        //지우기전 User List
        Optional<User> delete = userRepository.findById(99999L);
        Assert.assertFalse(delete.isPresent());


    }
}