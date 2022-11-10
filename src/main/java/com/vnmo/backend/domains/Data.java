package com.vnmo.backend.domains;

import lombok.*;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Data {

    private Integer id;

    private Integer apId;

    private String indicatorCode;

    private Integer unitId;

    private Integer year;

    private Integer month;

    private Integer target;

    private Integer targetType;

    private Integer actualAchieve;

    private Integer boyNumber;

    private Integer girlNumber;

    private Integer maleNumber;

    private Integer femaleNumber;

    private Integer mvc;

    private Integer rc;

    private Integer d1;

    private Integer d2;

    private Integer d3;

    private Integer approved;

    private Timestamp createdAt;

    private String createdBy;

    private Timestamp updatedAt;

    private String updatedBy;
}