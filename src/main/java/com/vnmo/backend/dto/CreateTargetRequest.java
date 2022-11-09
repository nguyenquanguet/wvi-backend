package com.vnmo.backend.dto;

import com.vnmo.backend.core.BaseDomain;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class CreateTargetRequest extends BaseDomain {

    @NotBlank(message = "Username have blank !")
    private String username;

    @NotNull(message = "Not Null")
    @Min(value = 0, message = "Id is negative !")
    private Integer apId;

    @NotNull(message = "Input indicator Id")
    @Min(value = 0, message = "Id is negative !")
    private Integer indicatorId;

    @NotNull(message = "Input year")
    @Min(value = 1900, message = "Year is larger than 1900 !")
    @Max(value = 2100, message = "Year is smaller than 2100 !")
    private Integer year;

    @NotNull(message = "Input target")
    @Min(value = 0, message = "Target is negative !")
    private Integer target;

    private Integer month;
}
