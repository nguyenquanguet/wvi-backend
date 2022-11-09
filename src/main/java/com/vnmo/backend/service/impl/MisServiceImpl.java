package com.vnmo.backend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.vnmo.backend.domains.MisData;
import com.vnmo.backend.dto.UpdateMisDataRequest;
import com.vnmo.backend.dto.CreateTargetRequest;
import com.vnmo.backend.exception.BusinessException;
import com.vnmo.backend.exception.ExceptionCode;
import com.vnmo.backend.respository.AuthenticationRepository;
import com.vnmo.backend.respository.MisRepository;
import com.vnmo.backend.service.MisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;

import static com.vnmo.backend.constant.ConstantValue.RESPONSE_CODE_SUCCESS;
import static com.vnmo.backend.core.ResponseObject.response;

@Service
@RequiredArgsConstructor
public class MisServiceImpl implements MisService {

    private final MisRepository misRepository;

    private final AuthenticationRepository authenticationRepository;

    @Override
    public ResponseEntity<?> findAllIndicator(Integer apId, Integer tpId) {
        return response(misRepository.findAllIndicator(apId, tpId));
    }

    @Override
    public ResponseEntity<?> findAllTpByApId(Integer apId) {
        return response(misRepository.findAllTpByApId(apId));
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateMisData(UpdateMisDataRequest request) {
        if(authenticationRepository.existedUsernameByUsername(request.getUsername()) != 1){
            throw new BusinessException(ExceptionCode.ERROR_USERNAME_NOT_FOUND);
        }
        if(misRepository.existedByMisId(request.getId()) != 1){
            throw new BusinessException(ExceptionCode.ERROR_MIS_DATA_NOT_FOUND);
        }

        Integer apUser = authenticationRepository.findApIdUserByUsername(request.getUsername());

        Integer apMis = misRepository.findApIdMisDataById(request.getId());

        if(apUser != 0 && !apUser.equals(apMis)){
            throw new BusinessException(ExceptionCode.ERROR_USER_NOT_HAVE_PERMISSION);
        }

        request.setActualAchieve(request.getBoyNumber()
                                + request.getGirlNumber()
                                + request.getFemaleNumber()
                                + request.getMaleNumber());

        MisData data = new MisData();
        BeanUtil.copyProperties(request, data);
        data.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        data.setUpdatedBy(request.getUsername());
        return response(misRepository.updateMisData(data));
    }

    @Override
    @Transactional
    public ResponseEntity<?> createTarget(CreateTargetRequest createTargetRequest) {
        if(authenticationRepository.existedUsernameByUsername(createTargetRequest.getUsername()) != 1){
            throw new BusinessException(ExceptionCode.ERROR_USERNAME_NOT_FOUND);
        }
        if(misRepository.existedByApId(createTargetRequest.getApId()) != 1){
            throw new BusinessException(ExceptionCode.ERROR_AP_ID_NOT_FOUND);
        }

        if(misRepository.existedByIndicatorId(createTargetRequest.getIndicatorId()) != 1){
            throw new BusinessException(ExceptionCode.ERROR_INDICATOR_ID_NOT_FOUND);
        }

        if(misRepository.existedByIndicatorIdAndApId(createTargetRequest.getIndicatorId(), createTargetRequest.getApId()) != 1){
            throw new BusinessException(ExceptionCode.ERROR_AP_NOT_HAVE_INDICATOR);
        }

        createTargetRequest.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        createTargetRequest.setCreatedBy(createTargetRequest.getUsername());
        createTargetRequest.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        createTargetRequest.setUpdatedBy(createTargetRequest.getUsername());

        for(int i = 1; i <= 12; i++){
            createTargetRequest.setMonth(i);
            misRepository.createTarget(createTargetRequest);
        }
        return response(RESPONSE_CODE_SUCCESS);
    }

    @Override
    public ResponseEntity<?> findAllMisByApId(Integer apId, Integer tpId, Integer indicatorId, Integer year, Integer month) {
        return response(misRepository.findAllMisByApId(apId, tpId, indicatorId, year, month));
    }

    @Override
    public ResponseEntity<?> approvedRequest(Integer request, Boolean isApproved) {
        return response(true);
    }
}
