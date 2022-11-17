package com.vnmo.backend.mapper;

import com.vnmo.backend.domains.ApIndicator;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface ApIndicatorMapper {

    void createDataApIndicator(ApIndicator apIndicator);

    void updateDataApIndicator(ApIndicator apIndicator);

    Optional<ApIndicator> findApIndicatorById(ApIndicator apIndicator);

    void updateApIndicator(ApIndicator apIndicator);
}
