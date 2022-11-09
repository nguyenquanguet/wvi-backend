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
public class UpdateMisDataRequest extends BaseDomain {

    @NotNull(message = "Input Id")
    private Integer id;

    @NotBlank(message = "Username have blank !")
    private String username;

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
}
