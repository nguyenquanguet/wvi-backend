package com.vnmo.backend.respository;

import com.vnmo.backend.domains.Ap;
import com.vnmo.backend.domains.Indicator;
import com.vnmo.backend.domains.MisData;
import com.vnmo.backend.dto.CreateTargetRequest;
import com.vnmo.backend.mapper.MisMapper;
import com.vnmo.backend.models.MisIndicator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MisRepository {

    private final MisMapper misMapper;

    public List<Indicator> findAllIndicator(Integer apId, Integer tpId) {
        return misMapper.findAllIndicator(apId, tpId);
    }

    public List<Ap> findAllTpByApId(Integer apId) {
        return misMapper.findAllTpByApId(apId);
    }

    public int updateMisData(MisData data) {
        return misMapper.updateMisData(data);
    }

    public void createTarget(CreateTargetRequest createTargetRequest) {
        misMapper.createTarget(createTargetRequest);
    }

    public List<MisIndicator> findAllMisByApId(Integer apId, Integer tpId, Integer indicatorId, Integer year, Integer month) {
        return misMapper.findAllMisByApId(apId, tpId, indicatorId, year, month);
    }

    public int existedByApId(Integer apId) {
        return misMapper.existedByApId(apId);
    }

    public int existedByIndicatorId(Integer indicatorId) {
        return misMapper.existedByIndicatorId(indicatorId);
    }

    public int existedByIndicatorIdAndApId(Integer indicatorId, Integer apId) {
        return misMapper.existedByIndicatorIdAndApId(indicatorId, apId);
    }

    public int existedByMisId(Integer id) {
        return misMapper.existedByMisId(id);
    }

    public Integer findApIdMisDataById(Integer id) {
        return misMapper.findApIdMisDataById(id);
    }
}
