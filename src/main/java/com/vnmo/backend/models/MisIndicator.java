package com.vnmo.backend.models;

import lombok.*;

import java.sql.Timestamp;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MisIndicator {

    private Integer id;

    private Integer apId;

    private Integer indicatorId;

    private String indicatorCode;

    private String indicatorName;

    private Integer year;

    private Integer month;

    private Integer target;

    private Integer actualAchieve;

    private Integer boyNumber;

    private Integer girlNumber;

    private Integer maleNumber;

    private Integer femaleNumber;

    private Integer mvc;

    private Integer rc;

    private Timestamp createdAt;

    private String createdBy;

    private Timestamp updatedAt;

    private String updatedBy;
}
