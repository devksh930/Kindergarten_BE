package com.kindergarten.api.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

public class UserDTO {
    @Data
    public static class ParentCreate {
        private String userid;
        private String password;
        private String name;
        private String phone;
        private String email;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;
    }

    @Data
    public static class TeacherCreate{
        private String userid;
        private String password;
        private String name;
        private String phone;
        private String email;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;
    }

    public static class Update {


    }

    public static class Login {


    }

}
