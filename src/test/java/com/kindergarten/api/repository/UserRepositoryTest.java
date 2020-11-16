package com.kindergarten.api.repository;

import com.kindergarten.api.users.User;
import com.kindergarten.api.users.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;


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
        userRepository.save(inituser);;

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
        User inituser1 = new User();

        inituser1.setUserid("user1");
        inituser1.setPassword("password");
        inituser1.setName("테스트");
        inituser1.setPhone("010-1234-1234");
        inituser1.setEmail("test@test.com");
        userRepository.save(inituser1);

        User inituser2 = new User();

        inituser2.setUserid("user2");
        inituser2.setPassword("password");
        inituser2.setName("테스트");
        inituser2.setPhone("010-1234-1234");
        inituser2.setEmail("test@test.com");
        userRepository.save(inituser2);

        //when

        //지우기전 User List
        List<User> beforeDelete = userRepository.findAll();

        Assertions.assertThat(beforeDelete.size()).isEqualTo(2);

        User user = userRepository.findByUserid("user1").get();
        userRepository.delete(user);
        //지운후 User List
        List<User> afterDelete = userRepository.findAll();
        //then
        Assertions.assertThat(afterDelete.size()).isEqualTo(1);


    }
}