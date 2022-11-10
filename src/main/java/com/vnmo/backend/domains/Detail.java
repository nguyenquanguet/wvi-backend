package com.vnmo.backend.domains;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Detail {

    private Integer id;

    private Integer apId;

    private String indicatorCode;

    private Integer unitId;

    private Integer year;

    private Integer fyTarget;

    private Integer fyAchieved;

    private Integer f6Target;

    private Integer l6Target;

    private Integer f6Achieved;

    private Integer l6Achieved;

    private Integer targetType;

    private Integer l6Boy;

    private Integer l6Girl;

    private Integer l6Male;

    private Integer l6Female;

    private Integer l6Mvc;

    private Integer l6Rc;

    private Integer f6Boy;

    private Integer f6Girl;

    private Integer f6Male;

    private Integer f6Female;

    private Integer f6Mvc;

    private Integer f6Rc;

    private Integer fyBoy;

    private Integer fyGirl;

    private Integer fyMale;

    private Integer fyFemale;

    private Integer fyMvc;

    private Integer fyRc;

    private Integer f6D1;

    private Integer f6D2;

    private Integer f6D3;

    private Integer l6D1;

    private Integer l6D2;

    private Integer l6D3;

    private Integer fyD1;

    private Integer fyD2;

    private Integer fyD3;

    private Timestamp createdAt;

    private String createdBy;

    private Timestamp updatedAt;

    private String updatedBy;
}
