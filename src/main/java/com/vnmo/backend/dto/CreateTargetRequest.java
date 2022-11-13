package com.vnmo.backend.dto;

import com.vnmo.backend.core.BaseDomain;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateTargetRequest extends BaseDomain {

    private String username;

    private Integer apId;

    private String indicatorCode;

    private Integer unitId;

    private Integer year;

    private Integer month;

    private Integer target;
}
