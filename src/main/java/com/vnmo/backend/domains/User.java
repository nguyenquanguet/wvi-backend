package com.vnmo.backend.domains;

import lombok.*;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    private Integer id;

    private String username;

    private String email;

    private String password;

    private String userApId;

    private String firstName;

    private String middleName;

    private String lastName;

    private String gender;

    private Timestamp createdAt;

    private String createdBy;

    private Timestamp updatedAt;

    private String updatedBy;
}