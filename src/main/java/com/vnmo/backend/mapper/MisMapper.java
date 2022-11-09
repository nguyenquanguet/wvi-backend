package com.vnmo.backend.mapper;

import com.vnmo.backend.domains.Ap;
import com.vnmo.backend.domains.Indicator;
import com.vnmo.backend.domains.MisData;
import com.vnmo.backend.dto.CreateTargetRequest;
import com.vnmo.backend.models.MisIndicator;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MisMapper {

    List<Indicator> findAllIndicator(Integer apId, Integer tpId);

    List<Ap> findAllTpByApId(Integer apId);

    int updateMisData(MisData data);

    void createTarget(CreateTargetRequest createTargetRequest);

    List<MisIndicator> findAllMisByApId(Integer apId, Integer tpId, Integer indicatorId, Integer year, Integer month);

    int existedByApId(Integer apId);

    int existedByIndicatorId(Integer indicatorId);

    int existedByIndicatorIdAndApId(Integer indicatorId, Integer apId);

    int existedByMisId(Integer id);

    Integer findApIdMisDataById(Integer id);
}
