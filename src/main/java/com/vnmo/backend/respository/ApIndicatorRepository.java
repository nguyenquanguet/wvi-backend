package com.vnmo.backend.respository;

import com.vnmo.backend.domains.ApIndicator;
import com.vnmo.backend.mapper.ApIndicatorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ApIndicatorRepository {

    private final ApIndicatorMapper apIndicatorMapper;

    public void createDataApIndicator(ApIndicator apIndicator) {
        apIndicatorMapper.createDataApIndicator(apIndicator);
    }

    public void updateDataApIndicator(ApIndicator apIndicator){
        apIndicatorMapper.updateDataApIndicator(apIndicator);
    }

    public void updateApIndicator(ApIndicator apIndicator){
        apIndicatorMapper.updateApIndicator(apIndicator);
    }

    public Optional<ApIndicator> findApIndicatorById(ApIndicator apIndicator) {
        return apIndicatorMapper.findApIndicatorById(apIndicator);
    }
}
