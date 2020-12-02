package com.kindergarten.api.users;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDTO {
    @Data
    public static class Create {
        @Size(min = 6, max = 20, message = "id는 6자이상 20자이하로 입력해주세요.")
        private String userid;

        @Pattern(regexp = "[a-zA-Z1-9]{8,20}", message = "비밀번호는 영어와 숫자로 포함해서 8~20자리 이내로 입력해주세요.")
        private String password;

        @NotBlank
        @Pattern(regexp = "\\S{2,6}", message = "이름은 2~6자로 입력해주세요.")
        private String name;

        @NotBlank(message = "전화번호를 입력해주세요.")
        @Pattern(regexp = "[0-9]{10,11}", message = "10~11자리의 숫자만 입력가능합니다")
        private String phone;

        @Email(message = "올바른 이메일을 입력해주세요 aaa@email.com")
        private String email;

        private String role;
        private String kindergarten_id;
        private List<ADD_Student> student = new ArrayList<>();

    }

    @Data
    public static class ADD_Student {
        private String name;
        private int year;
        private int month;
        private int day;
        private Long kindergarten_id;
    }

    @Data
    public static class ADD_Students {
        private List<ADD_Student> students = new ArrayList<>();
    }

    @Data
    public static class Response_User_Student {
        private String userid;
        private List<Response_Student> students = new ArrayList<>();
    }

    @Data
    public static class Response_Student {
        private Long studentId;
        private String name;
        @DateTimeFormat(pattern = "yyyyMMdd")
        private LocalDate birthday;
        private Long kindergarten_id;
        private String kindergarten_name;
        private boolean access;
    }

    @Data
    public static class Modify_Student {
        private Long studentId;
        private Long kindergartenId;
        private String studentName;
        private int year;
        private int month;
        private int day;
    }

    @Data
    public static class Response {
        private Long id;
        private String email;
    }

    @Data
    public static class Login {
        @NotBlank(message = "비어 있어서는 안됩니다.")
        private String userid;
        @NotBlank(message = "비어 있어서는 안됩니다.")
        private String password;
    }

    @Data
    public static class Login_response {
        private String userid;
        private String name;
        private String token;
    }

    @Data
    public static class UserModify {
        @NotNull(message = "Null일수는 없습니다")
        private String phone;
        @NotNull(message = "Null일수는 없습니다")
        private String email;
        @NotNull
        private String password;
        private String newpassword;
    }

    @Data
    public static class TeacherKinderModify {
        private Long kindergartensid;
    }

    @Data
    public static class TeacherModify {
        @NotNull(message = "Null일수는 없습니다")
        private long kindergraten_id;
    }

    @Data
    public static class UserPasswordModify {
        private String password;
    }

    @Data
    public static class currentUser {
        private String userid;
        private String name;
        private String role;

    }
}
