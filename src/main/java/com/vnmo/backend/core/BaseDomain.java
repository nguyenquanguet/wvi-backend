package com.vnmo.backend.core;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BaseDomain {

    private Timestamp createdAt;

    private String createdBy;

    private Timestamp updatedAt;

    private String updatedBy;
}
