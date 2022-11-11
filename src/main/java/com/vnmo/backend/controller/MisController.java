package com.vnmo.backend.controller;

import com.vnmo.backend.dto.CreateDataRequest;
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
    @GetMapping("/find-all-indicator-by-tp-id/{tpId}")
    public ResponseEntity<?> findAllIndicator(@PathVariable Integer tpId) {
        return misService.findAllIndicator(tpId);
    }

    @GetMapping("/find-all-tp/")
    public ResponseEntity<?> findAllTp() {
        return misService.findAllTp();
    }


    @PostMapping("/create-data")
    public ResponseEntity<?> createData(@RequestBody @Validated CreateDataRequest createDataRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationExceptionHandle(bindingResult);
        }
        return misService.createData(createDataRequest);
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
                                              @RequestParam(required = false) String indicatorCode,
                                              @RequestParam(required = false) Integer year,
                                              @RequestParam(required = false) Integer month) {
        return misService.findAllMisByApId(apId, tpId, indicatorCode, year, month);
    }

//    @PutMapping("/approve/{apId}")
//    public ResponseEntity<?> approvedRequest(@PathVariable Integer apId) {
//        return misService.approvedRequest(apId, true);
//    }
}
