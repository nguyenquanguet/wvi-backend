package com.vnmo.backend.service;

import com.vnmo.backend.dto.CreateTargetRequest;
import com.vnmo.backend.dto.UpdateMisDataRequest;
import com.vnmo.backend.dto.CreateDataRequest;
import org.springframework.http.ResponseEntity;

public interface MisService {

    ResponseEntity<?> findAllIndicator(Integer tpId);

    ResponseEntity<?> findAllTp();

    ResponseEntity<?> updateMisData(UpdateMisDataRequest request);

    ResponseEntity<?> createData(CreateDataRequest createDataRequest);

    ResponseEntity<?> findAllMisByApId(Integer apId, Integer tpId, String indicatorCode, Integer year, Integer month);
    ResponseEntity<?> approvedRequest(Integer request, Boolean isApproved);

    ResponseEntity<?> createTarget(CreateTargetRequest createTargetRequest);
}
