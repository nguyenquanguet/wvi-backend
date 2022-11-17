package com.vnmo.backend.domains;

import lombok.*;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApIndicator {

    private Integer id;

    private String indicatorCode;

    private Integer fy;

    private Integer apId;

    private String apName;

    private Integer target;

    private Integer achieved;

    private Integer f6Target;

    private Integer l6Target;

    private Integer f6Achieved;

    private Integer l6Achieved;

    private Timestamp createdAt;

    private String createdBy;

    private Timestamp updatedAt;

    private String updatedBy;
}
