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
public class CreateDataRequest extends BaseDomain {

    @NotBlank(message = "Username have blank !")
    private String username;

    @NotNull(message = "Not Null")
    @Min(value = 0, message = "ApId is negative !")
    private Integer apId;

    @NotNull(message = "Input indicator Id")
    private String indicatorCode;

    private Integer unitId;

    private Integer targetType;

    @NotNull(message = "Input year")
    @Min(value = 1900, message = "Year is larger than 1900 !")
    @Max(value = 2100, message = "Year is smaller than 2100 !")
    private Integer year;

    private Integer target;

    @NotNull(message = "Input target")
    @Min(value = 0, message = "Target is negative !")
    @Max(value = 12, message = "Month is larger than 12")
    private Integer month;

    @NotNull(message = "Input target")
    @Min(value = 0, message = "Actual Achieve is negative !")
    private Integer actualAchieve;

    @NotNull(message = "Input Boy Number")
    @Min(value = 0, message = "Boy Number is negative !")
    private Integer boyNumber;

    @NotNull(message = "Input Girl Number")
    @Min(value = 0, message = "Girl Number is negative !")
    private Integer girlNumber;

    @NotNull(message = "Input Male Number")
    @Min(value = 0, message = "Male Number is negative !")
    private Integer maleNumber;

    @NotNull(message = "Input Female Number")
    @Min(value = 0, message = "Female Number is negative !")
    private Integer femaleNumber;

    @NotNull(message = "Input Mvc")
    @Min(value = 0, message = "Mvc is negative !")
    private Integer mvc;

    @NotNull(message = "Input Rc")
    @Min(value = 0, message = "Rc is negative !")
    private Integer rc;

    @NotNull(message = "Input d1")
    @Min(value = 0, message = "D1 is negative !")
    private Integer d1;

    @NotNull(message = "Input d2")
    @Min(value = 0, message = "D2 is negative !")
    private Integer d2;

    @NotNull(message = "Input d3")
    @Min(value = 0, message = "D3 is negative !")
    private Integer d3;
}
