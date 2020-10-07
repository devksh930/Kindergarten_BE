package com.kindergarten.api.model.dto;

import com.kindergarten.api.model.entity.UserRole;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {
    @Data
    public static class Create {
        private String userid;
        private String password;
        private String name;
        private String phone;
        private String email;
        private String role;
        private String kindergarten_id;
        private List<ADD_Student> student=new ArrayList<>();

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
        private String userid;
        private String password;
    }
}
