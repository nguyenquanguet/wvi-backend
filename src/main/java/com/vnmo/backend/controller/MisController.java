package com.vnmo.backend.controller;

import com.vnmo.backend.dto.CreateTargetRequest;
import com.vnmo.backend.dto.UpdateMisDataRequest;
import com.vnmo.backend.exception.ValidationExceptionHandle;
import com.vnmo.backend.service.MisService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/mis")
public record MisController(MisService misService) {

    @CrossOrigin
    @GetMapping("/find-all-indicator/{apId}")
    public ResponseEntity<?> findAllIndicator(@PathVariable Integer apId,
                                              @RequestParam(required = false) Integer tpId) {
        return misService.findAllIndicator(apId, tpId);
    }

    @GetMapping("/find-all-tp/{apId}")
    public ResponseEntity<?> findAllTpByApId(@PathVariable Integer apId) {
        return misService.findAllTpByApId(apId);
    }


    @PostMapping("/create-target")
    public ResponseEntity<?> createTarget(@RequestBody @Validated CreateTargetRequest createTargetRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationExceptionHandle(bindingResult);
        }
        return misService.createTarget(createTargetRequest);
    }

    @PutMapping("/update-target")
    public ResponseEntity<?> updateTarget(@RequestBody @Validated CreateTargetRequest createTargetRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationExceptionHandle(bindingResult);
        }
        return misService.createTarget(createTargetRequest);
    }

    @PutMapping("/update-mis")
    public ResponseEntity<?> updateMisData(@RequestBody @Validated UpdateMisDataRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationExceptionHandle(bindingResult);
        }
        return misService.updateMisData(request);
    }

    @GetMapping("/find-all-mis/{apId}")
    public ResponseEntity<?> findAllMisByApId(@PathVariable Integer apId,
                                              @RequestParam(required = false) Integer tpId,
                                              @RequestParam(required = false) Integer indicatorId,
                                              @RequestParam(required = false) Integer year,
                                              @RequestParam(required = false) Integer month) {
        return misService.findAllMisByApId(apId, tpId, indicatorId, year, month);
    }
}
