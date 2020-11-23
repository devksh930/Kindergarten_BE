package com.kindergarten.api.users;

import com.kindergarten.api.common.exception.CUserNotFoundException;
import com.kindergarten.api.security.util.JwtTokenProvider;
import com.kindergarten.api.student.Student;
import com.kindergarten.api.users.*;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
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
        userService.registerAccount(create);
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
        //thenc
        Assertions.assertThat(finduser.getRole()).isEqualTo(UserRole.ROLE_NOT_PERMITTED_DIRECTOR);
        Assertions.assertThat(finduser.getName()).isEqualTo(create.getName());
    }

    @Test
    public void modifyUser() {
        //given
        UserDTO.UserModify modify = new UserDTO.UserModify();
        modify.setEmail("modify@mody.com");
        modify.setPhone("0101231233");
        modify.setKindergraten_id("");
        //when
        User updateUser = userService.modifyUser("devksh930", modify);
        //then
        Assertions.assertThat(updateUser.getEmail()).isEqualTo(modify.getEmail());
        Assertions.assertThat(updateUser.getPhone()).isEqualTo(modify.getPhone());

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

    @Test
    public void parentStudents() {
        //given
        String userid = "devksh930";
        //when
        UserDTO.Response_User_Student response_user_student = userService.parentStudents(userid);
        User user = userRepository.findByUserid(userid).orElseThrow(CUserNotFoundException::new);
        List<Student> student = user.getStudent();
        int userStudentsize = student.size();
        int userStudentsize1 = response_user_student.getStudents().size();
        //then
        Assertions.assertThat(userStudentsize).isEqualTo(userStudentsize1);

    }

    @Test
    public void currentUser() {
        //given
        String userid = "devksh930";
        //when
        User user = userRepository.findByUserid(userid).orElseThrow(CUserNotFoundException::new);
        UserDTO.currentUser currentUser = userService.currentUser(userid);

        Assertions.assertThat(user.getUserid()).isEqualTo(currentUser.getUserid());
        Assertions.assertThat(user.getName()).isEqualTo(currentUser.getName());
        Assertions.assertThat(user.getRole().name()).isEqualTo(currentUser.getRole());

    }
}