package com.vnmo.backend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.vnmo.backend.domains.Data;
import com.vnmo.backend.domains.Detail;
import com.vnmo.backend.domains.Indicator;
import com.vnmo.backend.dto.CreateDataRequest;
import com.vnmo.backend.dto.CreateTargetRequest;
import com.vnmo.backend.dto.UpdateMisDataRequest;
import com.vnmo.backend.exception.BusinessException;
import com.vnmo.backend.exception.ExceptionCode;
import com.vnmo.backend.models.MisIndicator;
import com.vnmo.backend.respository.AuthenticationRepository;
import com.vnmo.backend.respository.MisRepository;
import com.vnmo.backend.service.MisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.vnmo.backend.constant.ConstantValue.RESPONSE_CODE_SUCCESS;
import static com.vnmo.backend.core.ResponseObject.response;

@Service
@RequiredArgsConstructor
public class MisServiceImpl implements MisService {

    private final MisRepository misRepository;

    private final AuthenticationRepository authenticationRepository;

    @Override
    public ResponseEntity<?> findAllIndicator(Integer tpId) {
        return response(misRepository.findAllIndicator(tpId));
    }

    @Override
    public ResponseEntity<?> findAllTp() {
        return response(misRepository.findAllTp());
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateMisData(UpdateMisDataRequest request) {
        if (authenticationRepository.existedUsernameByUsername(request.getUsername()) != 1) {
            throw new BusinessException(ExceptionCode.ERROR_USERNAME_NOT_FOUND);
        }
        if (misRepository.existedByMisId(request.getId()) != 1) {
            throw new BusinessException(ExceptionCode.ERROR_MIS_DATA_NOT_FOUND);
        }

        Integer apUser = authenticationRepository.findApIdUserByUsername(request.getUsername());

        Integer apMis = misRepository.findApIdMisDataById(request.getId());

        if (apUser != 0 && !apUser.equals(apMis)) {
            throw new BusinessException(ExceptionCode.ERROR_USER_NOT_HAVE_PERMISSION);
        }

        request.setActualAchieve(request.getBoyNumber()
                + request.getGirlNumber()
                + request.getFemaleNumber()
                + request.getMaleNumber());

        Data data = new Data();
        BeanUtil.copyProperties(request, data);
        data.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        data.setUpdatedBy(request.getUsername());
        return response(misRepository.updateMisData(data));
    }

    @Override
    @Transactional
    public ResponseEntity<?> createData(CreateDataRequest createDataRequest) {
        if (authenticationRepository.existedUsernameByUsername(createDataRequest.getUsername()) != 1) {
            throw new BusinessException(ExceptionCode.ERROR_USERNAME_NOT_FOUND);
        }
        if (misRepository.existedByApId(createDataRequest.getApId()) != 1) {
            throw new BusinessException(ExceptionCode.ERROR_AP_ID_NOT_FOUND);
        }

        if (misRepository.existedByIndicatorCode(createDataRequest.getIndicatorCode()) != 1) {
            throw new BusinessException(ExceptionCode.ERROR_INDICATOR_ID_NOT_FOUND);
        }
        if(misRepository.existedData(createDataRequest.getIndicatorCode(), createDataRequest.getApId(),
                createDataRequest.getYear(), createDataRequest.getMonth()) == 1){
            throw new BusinessException(ExceptionCode.ERROR_DATA_EXISTED);
        }

        createDataRequest.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        createDataRequest.setCreatedBy(createDataRequest.getUsername());
        createDataRequest.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        createDataRequest.setUpdatedBy(createDataRequest.getUsername());

        Optional<Indicator> indicator = misRepository.findIndicatorByCode(createDataRequest.getIndicatorCode());


        if (indicator.isEmpty()) {
            throw new BusinessException(ExceptionCode.ERROR_INDICATOR_ID_NOT_FOUND);
        }

        createDataRequest.setUnitId(indicator.get().getUnitId());
        createDataRequest.setTargetType(indicator.get().getTargetType());
        misRepository.createDataMis(createDataRequest);

        updateDetail(createDataRequest);

        return response(RESPONSE_CODE_SUCCESS);
    }

    private void updateDetail(CreateDataRequest createDataRequest) {
        Optional<Detail> detail = misRepository.findDetail(createDataRequest.getApId(),
                createDataRequest.getIndicatorCode(), createDataRequest.getYear());

        if (detail.isEmpty()) {
            Detail createDetail = new Detail();

            createDetail.setApId(createDataRequest.getApId());
            createDetail.setIndicatorCode(createDataRequest.getIndicatorCode());
            createDetail.setUnitId(createDataRequest.getUnitId());
            createDetail.setYear(createDataRequest.getYear());

            if (10 <= createDataRequest.getMonth() && createDataRequest.getMonth() <= 12
                    && 1 <= createDataRequest.getMonth() && createDataRequest.getMonth() <= 3) {

                createDetail.setF6Target(createDataRequest.getTarget());
                createDetail.setF6Achieved(createDataRequest.getActualAchieve());
                createDetail.setF6Boy(createDataRequest.getBoyNumber());
                createDetail.setF6Girl(createDataRequest.getGirlNumber());
                createDetail.setF6Male(createDataRequest.getMaleNumber());
                createDetail.setF6Female(createDataRequest.getFemaleNumber());
                createDetail.setF6Mvc(createDataRequest.getMvc());
                createDetail.setF6Rc(createDataRequest.getRc());
                createDetail.setF6D1(createDataRequest.getD1());
                createDetail.setF6D2(createDataRequest.getD2());
                createDetail.setF6D3(createDataRequest.getD3());

                createDetail.setL6Target(0);
                createDetail.setL6Achieved(0);
                createDetail.setL6Boy(0);
                createDetail.setL6Girl(0);
                createDetail.setL6Male(0);
                createDetail.setL6Female(0);
                createDetail.setL6Mvc(0);
                createDetail.setL6Rc(0);
                createDetail.setL6D1(0);
                createDetail.setL6D2(0);
                createDetail.setL6D3(0);

            } else {

                createDetail.setF6Target(0);
                createDetail.setF6Achieved(0);
                createDetail.setF6Boy(0);
                createDetail.setF6Girl(0);
                createDetail.setF6Male(0);
                createDetail.setF6Female(0);
                createDetail.setF6Mvc(0);
                createDetail.setF6Rc(0);
                createDetail.setF6D1(0);
                createDetail.setF6D2(0);
                createDetail.setF6D3(0);

                createDetail.setL6Target(createDataRequest.getTarget());
                createDetail.setL6Achieved(createDataRequest.getActualAchieve());
                createDetail.setL6Boy(createDataRequest.getBoyNumber());
                createDetail.setL6Girl(createDataRequest.getGirlNumber());
                createDetail.setL6Male(createDataRequest.getMaleNumber());
                createDetail.setL6Female(createDataRequest.getFemaleNumber());
                createDetail.setL6Mvc(createDataRequest.getMvc());
                createDetail.setL6Rc(createDataRequest.getRc());
                createDetail.setL6D1(createDataRequest.getD1());
                createDetail.setL6D2(createDataRequest.getD2());
                createDetail.setL6D3(createDataRequest.getD3());
            }

            createDetail.setFyTarget(createDataRequest.getTarget());
            createDetail.setFyAchieved(createDataRequest.getActualAchieve());
            createDetail.setTargetType(createDataRequest.getTargetType());
            createDetail.setFyBoy(createDataRequest.getBoyNumber());
            createDetail.setFyGirl(createDataRequest.getGirlNumber());
            createDetail.setFyMale(createDataRequest.getMaleNumber());
            createDetail.setFyFemale(createDataRequest.getFemaleNumber());
            createDetail.setFyMvc(createDataRequest.getMvc());
            createDetail.setFyRc(createDataRequest.getRc());
            createDetail.setFyD1(createDataRequest.getD1());
            createDetail.setFyD2(createDataRequest.getD2());
            createDetail.setFyD3(createDataRequest.getD3());

            createDetail.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            createDetail.setCreatedBy(createDataRequest.getUsername());
            createDetail.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            createDetail.setUpdatedBy(createDataRequest.getUsername());

            misRepository.createDetail(createDetail);
        } else {
            Detail oldDetail = detail.get();
            Detail updateDetail = new Detail();

            updateDetail.setId(oldDetail.getId());

            if (10 <= createDataRequest.getMonth() && createDataRequest.getMonth() <= 12
                    && 1 <= createDataRequest.getMonth() && createDataRequest.getMonth() <= 3) {
                updateDetail.setF6Target(createDataRequest.getTarget() + oldDetail.getF6Target());
                updateDetail.setF6Achieved(createDataRequest.getActualAchieve() + oldDetail.getF6Achieved());
                updateDetail.setF6Boy(createDataRequest.getBoyNumber() + oldDetail.getF6Boy());
                updateDetail.setF6Girl(createDataRequest.getGirlNumber() + oldDetail.getF6Girl());
                updateDetail.setF6Male(createDataRequest.getMaleNumber() + oldDetail.getF6Male());
                updateDetail.setF6Female(createDataRequest.getFemaleNumber() + oldDetail.getF6Female());
                updateDetail.setF6Mvc(createDataRequest.getMvc() + oldDetail.getF6Mvc());
                updateDetail.setF6Rc(createDataRequest.getRc() + oldDetail.getF6Rc());
                updateDetail.setF6D1(createDataRequest.getD1() + oldDetail.getF6D1());
                updateDetail.setF6D2(createDataRequest.getD2() + oldDetail.getF6D2());
                updateDetail.setF6D3(createDataRequest.getD3() + oldDetail.getF6D3());
            } else {
                updateDetail.setL6Target(createDataRequest.getTarget() + oldDetail.getL6Target());
                updateDetail.setL6Achieved(createDataRequest.getActualAchieve() + oldDetail.getL6Achieved());
                updateDetail.setL6Boy(createDataRequest.getBoyNumber() + oldDetail.getL6Boy());
                updateDetail.setL6Girl(createDataRequest.getGirlNumber() + oldDetail.getL6Girl());
                updateDetail.setL6Male(createDataRequest.getMaleNumber() + oldDetail.getL6Male());
                updateDetail.setL6Female(createDataRequest.getFemaleNumber() + oldDetail.getL6Female());
                updateDetail.setL6Mvc(createDataRequest.getMvc() + oldDetail.getL6Mvc());
                updateDetail.setL6Rc(createDataRequest.getRc() + oldDetail.getL6Rc());
                updateDetail.setL6D1(createDataRequest.getD1() + oldDetail.getL6D1());
                updateDetail.setL6D2(createDataRequest.getD2() + oldDetail.getL6D2());
                updateDetail.setL6D3(createDataRequest.getD3() + oldDetail.getL6D3());
            }

            updateDetail.setFyTarget(createDataRequest.getTarget() + oldDetail.getFyTarget());
            updateDetail.setFyAchieved(createDataRequest.getActualAchieve() + oldDetail.getFyAchieved());
            updateDetail.setTargetType(createDataRequest.getTargetType() + oldDetail.getFyTarget());
            updateDetail.setFyBoy(createDataRequest.getBoyNumber() + oldDetail.getFyBoy());
            updateDetail.setFyGirl(createDataRequest.getGirlNumber() + oldDetail.getFyGirl());
            updateDetail.setFyMale(createDataRequest.getMaleNumber() + oldDetail.getFyMale());
            updateDetail.setFyFemale(createDataRequest.getFemaleNumber() + oldDetail.getFyFemale());
            updateDetail.setFyMvc(createDataRequest.getMvc() + oldDetail.getFyMvc());
            updateDetail.setFyRc(createDataRequest.getRc() + oldDetail.getFyRc());
            updateDetail.setFyD1(createDataRequest.getD1() + oldDetail.getFyD1());
            updateDetail.setFyD2(createDataRequest.getD2() + oldDetail.getFyD2());
            updateDetail.setFyD3(createDataRequest.getD3() + oldDetail.getFyD3());

            updateDetail.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            updateDetail.setUpdatedBy(createDataRequest.getUsername());

            misRepository.updateDetail(updateDetail);
        }

    }

    @Override
    public ResponseEntity<?> findAllMisByApId(Integer apId, Integer tpId, String indicatorCode, Integer year, Integer month) {
        List<MisIndicator> misIndicators = misRepository.findAllMisByApId(apId, tpId, indicatorCode, year, month);

        for(int i = 0; i<misIndicators.size(); i++){
            if(misIndicators.get(i).getBoyNumber() == null){
                misIndicators.get(i).setBoyNumber(0);
            }
            if(misIndicators.get(i).getGirlNumber() == null){
                misIndicators.get(i).setGirlNumber(0);
            }
            if(misIndicators.get(i).getMaleNumber() == null){
                misIndicators.get(i).setMaleNumber(0);
            }
            if(misIndicators.get(i).getFemaleNumber() == null){
                misIndicators.get(i).setFemaleNumber(0);
            }
            if(misIndicators.get(i).getMvc() == null){
                misIndicators.get(i).setMvc(0);
            }
            if(misIndicators.get(i).getRc() == null){
                misIndicators.get(i).setRc(0);
            }
            if(misIndicators.get(i).getD1() == null){
                misIndicators.get(i).setD1(0);
            }
            if(misIndicators.get(i).getD2() == null){
                misIndicators.get(i).setD2(0);
            }
            if(misIndicators.get(i).getD3() == null){
                misIndicators.get(i).setD3(0);
            }
        }
        return response(misIndicators);
    }

    @Override
    public ResponseEntity<?> approvedRequest(Integer request, Boolean isApproved) {
        return response(true);
    }

    @Override
    public ResponseEntity<?> createTarget(CreateTargetRequest createTargetRequest) {
        misRepository.createTarget(createTargetRequest);
        return response(RESPONSE_CODE_SUCCESS);
    }
}
