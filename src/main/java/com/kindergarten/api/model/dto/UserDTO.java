package com.kindergarten.api.model.dto;

import com.kindergarten.api.model.entity.UserRole;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
        private String kindergarten_id;
    }

    @Data
    public static class Response {
        private Long id;
        private String email;
        private UserRole role;
    }

    @Data
    public static class Login {
        @NotBlank(message = "비어 있어서는 안됩니다.")
        private String userid;
        @NotBlank(message = "비어 있어서는 안됩니다.")
        private String password;
    }

    @Data
    public static class UserModify {
        private String phone;
        private String email;
        private String kindergraten_id;
        private List<ADD_Student> student = new ArrayList<>();
    }

    @Data
    public static class UserPasswordModify {
        private String password;
    }
}
