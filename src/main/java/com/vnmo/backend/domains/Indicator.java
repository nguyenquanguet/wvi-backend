package com.vnmo.backend.domains;

import lombok.*;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Indicator {

    private Integer id;

    private Integer tpId;

    private String code;

    private Integer unitId;

    private Integer targetType;

    private String description;

    private String note;

    private Timestamp createdAt;

    private String createdBy;

    private Timestamp updatedAt;

    private String updatedBy;
}