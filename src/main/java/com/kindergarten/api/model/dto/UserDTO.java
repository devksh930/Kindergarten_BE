package com.kindergarten.api.model.dto;

import com.kindergarten.api.model.entity.UserRole;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

public class UserDTO {
    @Data
    public static class Create {
        @NotBlank(message = "비어 있어서는 안됩니다.")
        @Min(value = 6,message = "6자 이상 입력해주세요")
        @Max(value = 20,message = "20자 이상 입력할수 없습니다")
        private String userid;
        @NotBlank(message = "비어 있어서는 안됩니다.")
        @Min(value = 8,message = "8자 이상 입력해주세요")
        @Max(value = 20,message = "20자 이상 입력할수 없습니다")
        private String password;
        @NotBlank(message = "비어 있어서는 안됩니다.")
        @Min(value = 2,message = "2자 이상 입력해주세요")
        private String name;

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
    public static class Update {
        private Long id;
        private String userid;
        private String email;
        private String phone;
        private String password;
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


}
