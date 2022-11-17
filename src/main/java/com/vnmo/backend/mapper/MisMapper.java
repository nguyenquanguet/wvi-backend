package com.vnmo.backend.mapper;

import com.vnmo.backend.domains.Ap;
import com.vnmo.backend.domains.Detail;
import com.vnmo.backend.domains.Indicator;
import com.vnmo.backend.domains.Data;
import com.vnmo.backend.dto.CreateDataRequest;
import com.vnmo.backend.dto.CreateTargetRequest;
import com.vnmo.backend.models.MisIndicator;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MisMapper {

    List<Indicator> findAllIndicator( Integer tpId);

    List<Ap> findAllTp();

    int updateMisData(Data data);

    void createData(CreateDataRequest createDataRequest);

    List<MisIndicator> findAllMisByApId(Integer apId, Integer tpId, String indicatorCode, Integer year, Integer month);

    int existedByApId(Integer apId);

    int existedByIndicatorCode(String indicatorCode);

    int existedByMisId(Integer id);

    Integer findApIdMisDataById(Integer id);

    Optional<Indicator> findIndicatorById(String indicatorCode);

    Optional<Detail> findDetail(Integer apId, String indicatorCode, Integer year);

    void createDetail(Detail createDetail);

    void updateDetail(Detail updateDetail);

    Integer existedData(String indicatorCode, Integer apId, Integer year, Integer month);

    void createTarget(CreateTargetRequest createTargetRequest);

    Optional<String> findApNameByApId(Integer apId);

    void updateData(CreateDataRequest createDataRequest);
}
