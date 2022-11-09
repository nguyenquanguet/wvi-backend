package com.vnmo.backend.service;

import com.vnmo.backend.dto.UpdateMisDataRequest;
import com.vnmo.backend.dto.CreateTargetRequest;
import org.springframework.http.ResponseEntity;

public interface MisService {

    ResponseEntity<?> findAllIndicator(Integer apId, Integer tpId);

    ResponseEntity<?> findAllTpByApId(Integer apId);

    ResponseEntity<?> updateMisData(UpdateMisDataRequest request);

    ResponseEntity<?> createTarget(CreateTargetRequest createTargetRequest);

    ResponseEntity<?> findAllMisByApId(Integer apId, Integer tpId, Integer indicatorId, Integer year, Integer month);
}
