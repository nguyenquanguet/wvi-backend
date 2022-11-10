package com.vnmo.backend.respository;

import com.vnmo.backend.domains.Ap;
import com.vnmo.backend.domains.Detail;
import com.vnmo.backend.domains.Indicator;
import com.vnmo.backend.domains.Data;
import com.vnmo.backend.dto.CreateDataRequest;
import com.vnmo.backend.mapper.MisMapper;
import com.vnmo.backend.models.MisIndicator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MisRepository {

    private final MisMapper misMapper;

    public List<Indicator> findAllIndicator(Integer tpId) {
        return misMapper.findAllIndicator(tpId);
    }

    public List<Ap> findAllTp() {
        return misMapper.findAllTp();
    }

    public int updateMisData(Data data) {
        return misMapper.updateMisData(data);
    }

    public void createTarget(CreateDataRequest createDataRequest) {
        misMapper.createData(createDataRequest);
    }

    public List<MisIndicator> findAllMisByApId(Integer apId, Integer tpId, String indicatorCode, Integer year, Integer month) {
        return misMapper.findAllMisByApId(apId, tpId, indicatorCode, year, month);
    }

    public int existedByApId(Integer apId) {
        return misMapper.existedByApId(apId);
    }

    public int existedByIndicatorCode(String indicatorCode) {
        return misMapper.existedByIndicatorCode(indicatorCode);
    }

    public int existedByMisId(Integer id) {
        return misMapper.existedByMisId(id);
    }

    public Integer findApIdMisDataById(Integer id) {
        return misMapper.findApIdMisDataById(id);
    }

    public Optional<Indicator> findIndicatorByCode(String indicatorCode) {
        return misMapper.findIndicatorById(indicatorCode);
    }

    public Optional<Detail> findDetail(Integer apId, String indicatorCode, Integer year) {
        return misMapper.findDetail(apId, indicatorCode, year);
    }

    public void createDetail(Detail createDetail) {
        misMapper.createDetail(createDetail);
    }

    public void updateDetail(Detail updateDetail) {
        misMapper.updateDetail(updateDetail);
    }

    public int existedData(String indicatorCode, Integer apId, Integer year, Integer month) {
        return misMapper.existedData(indicatorCode, apId, year, month);
    }
}
