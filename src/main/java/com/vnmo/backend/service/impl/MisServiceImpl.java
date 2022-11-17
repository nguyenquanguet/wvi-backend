package com.vnmo.backend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.vnmo.backend.domains.ApIndicator;
import com.vnmo.backend.domains.Data;
import com.vnmo.backend.domains.Detail;
import com.vnmo.backend.domains.Indicator;
import com.vnmo.backend.dto.CreateDataRequest;
import com.vnmo.backend.dto.CreateTargetRequest;
import com.vnmo.backend.dto.UpdateMisDataRequest;
import com.vnmo.backend.exception.BusinessException;
import com.vnmo.backend.exception.ExceptionCode;
import com.vnmo.backend.models.MisIndicator;
import com.vnmo.backend.respository.ApIndicatorRepository;
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

    private final ApIndicatorRepository apIndicatorRepository;

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
        validate(createDataRequest.getUsername(), createDataRequest.getApId(), createDataRequest.getIndicatorCode());
        if (misRepository.existedData(createDataRequest.getIndicatorCode(), createDataRequest.getApId(),
                createDataRequest.getYear(), createDataRequest.getMonth()) == 1) {
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

        if (misRepository.existedData(createDataRequest.getIndicatorCode(), createDataRequest.getApId(),
                createDataRequest.getYear(), createDataRequest.getMonth()) == 1) {
            misRepository.updateData(createDataRequest);
        } else {
            misRepository.createDataMis(createDataRequest);
        }
        updateDetail(createDataRequest);
        updateApIndicator(createDataRequest);
        return response(RESPONSE_CODE_SUCCESS);
    }

    private void validate(String username, Integer apId, String indicatorCode) {
        if (authenticationRepository.existedUsernameByUsername(username) != 1) {
            throw new BusinessException(ExceptionCode.ERROR_USERNAME_NOT_FOUND);
        }
        if (misRepository.existedByApId(apId) != 1) {
            throw new BusinessException(ExceptionCode.ERROR_AP_ID_NOT_FOUND);
        }

        if (misRepository.existedByIndicatorCode(indicatorCode) != 1) {
            throw new BusinessException(ExceptionCode.ERROR_INDICATOR_ID_NOT_FOUND);
        }
    }

    private void updateApIndicator(CreateDataRequest createDataRequest) {
        Optional<String> apName = misRepository.findApNameByApId(createDataRequest.getApId());
        if (apName.isEmpty()) {
            throw new BusinessException(ExceptionCode.ERROR_AP_ID_NOT_FOUND);
        }

        ApIndicator apIndicator = new ApIndicator();
        apIndicator.setApId(createDataRequest.getApId());
        apIndicator.setFy(createDataRequest.getYear());
        apIndicator.setIndicatorCode(createDataRequest.getIndicatorCode());
        apIndicator.setApName(apName.get());

        Optional<ApIndicator> apIndicatorOptional = apIndicatorRepository.findApIndicatorById(apIndicator);
        if (apIndicatorOptional.isEmpty()) {
            apIndicator.setTarget(0);
            apIndicator.setF6Target(0);
            apIndicator.setL6Target(0);
            apIndicator.setAchieved(createDataRequest.getActualAchieve());
            apIndicator.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            apIndicator.setCreatedBy(createDataRequest.getUsername());
            if (10 <= createDataRequest.getMonth() && createDataRequest.getMonth() <= 12
                    || 1 <= createDataRequest.getMonth() && createDataRequest.getMonth() <= 3) {
                apIndicator.setF6Achieved(createDataRequest.getActualAchieve());
                apIndicator.setL6Achieved(0);
            } else {
                apIndicator.setL6Achieved(createDataRequest.getActualAchieve());
                apIndicator.setF6Achieved(0);
            }
            apIndicatorRepository.createDataApIndicator(apIndicator);
        } else {
            BeanUtil.copyProperties(apIndicatorOptional.get(), apIndicator);
            apIndicator.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            apIndicator.setUpdatedBy(createDataRequest.getUsername());
            apIndicator.setAchieved(apIndicatorOptional.get().getAchieved() + createDataRequest.getActualAchieve());
            if (10 <= createDataRequest.getMonth() && createDataRequest.getMonth() <= 12
                    || 1 <= createDataRequest.getMonth() && createDataRequest.getMonth() <= 3) {
                apIndicator.setF6Achieved(apIndicatorOptional.get().getF6Achieved() + createDataRequest.getActualAchieve());
            } else {
                apIndicator.setL6Achieved(apIndicatorOptional.get().getL6Achieved() + createDataRequest.getActualAchieve());
            }
            apIndicatorRepository.updateApIndicator(apIndicator);
        }
    }


    private void updateDetail(CreateDataRequest createDataRequest) {
        Optional<Detail> detail = misRepository.findDetail(createDataRequest.getApId(),
                createDataRequest.getIndicatorCode(), createDataRequest.getYear());

        if (detail.isEmpty()) {
            Detail createDetail = new Detail();
            createDetail.setFyTarget(0);
            createDetail.setL6Target(0);
            createDetail.setF6Target(0);
            createDetail.setApId(createDataRequest.getApId());
            createDetail.setIndicatorCode(createDataRequest.getIndicatorCode());
            createDetail.setUnitId(createDataRequest.getUnitId());
            createDetail.setYear(createDataRequest.getYear());
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

            if (10 <= createDataRequest.getMonth() && createDataRequest.getMonth() <= 12
                    || 1 <= createDataRequest.getMonth() && createDataRequest.getMonth() <= 3) {

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
            BeanUtil.copyProperties(oldDetail, updateDetail);

            if (10 <= createDataRequest.getMonth() && createDataRequest.getMonth() <= 12
                    || 1 <= createDataRequest.getMonth() && createDataRequest.getMonth() <= 3) {
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

            updateDetail.setFyAchieved(createDataRequest.getActualAchieve() + oldDetail.getFyAchieved());
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

        for (MisIndicator misIndicator : misIndicators) {
            if (misIndicator.getBoyNumber() == null) {
                misIndicator.setBoyNumber(0);
            }
            if (misIndicator.getGirlNumber() == null) {
                misIndicator.setGirlNumber(0);
            }
            if (misIndicator.getMaleNumber() == null) {
                misIndicator.setMaleNumber(0);
            }
            if (misIndicator.getFemaleNumber() == null) {
                misIndicator.setFemaleNumber(0);
            }
            if (misIndicator.getMvc() == null) {
                misIndicator.setMvc(0);
            }
            if (misIndicator.getRc() == null) {
                misIndicator.setRc(0);
            }
            if (misIndicator.getD1() == null) {
                misIndicator.setD1(0);
            }
            if (misIndicator.getD2() == null) {
                misIndicator.setD2(0);
            }
            if (misIndicator.getD3() == null) {
                misIndicator.setD3(0);
            }
        }
        return response(misIndicators);
    }

    @Override
    public ResponseEntity<?> approvedRequest(Integer request, Boolean isApproved) {
        return response(true);
    }

    @Override
    @Transactional
    public ResponseEntity<?> createTarget(CreateTargetRequest createTargetRequest) {

        validate(createTargetRequest.getUsername(), createTargetRequest.getApId(), createTargetRequest.getIndicatorCode());

        Optional<String> apName = misRepository.findApNameByApId(createTargetRequest.getApId());
        if (apName.isEmpty()) {
            throw new BusinessException(ExceptionCode.ERROR_AP_ID_NOT_FOUND);
        }

        ApIndicator apIndicator = new ApIndicator();
        apIndicator.setIndicatorCode(createTargetRequest.getIndicatorCode());
        apIndicator.setFy(createTargetRequest.getYear());
        apIndicator.setApId(createTargetRequest.getApId());
        apIndicator.setApName(apName.get());

        apIndicator.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        apIndicator.setCreatedBy(createTargetRequest.getUsername());
        apIndicator.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        apIndicator.setUpdatedBy(createTargetRequest.getUsername());

        Optional<ApIndicator> apIndicatorOld = apIndicatorRepository.findApIndicatorById(apIndicator);

        try {
            //Target 1 month
            if (createTargetRequest.getTargetType() == 1) {
                misRepository.createTarget(createTargetRequest);
                if (apIndicatorOld.isEmpty()) {
                    apIndicator.setTarget(createTargetRequest.getTarget());
                    if (10 <= createTargetRequest.getMonth() && createTargetRequest.getMonth() <= 12
                            && 1 <= createTargetRequest.getMonth() && createTargetRequest.getMonth() <= 3) {
                        apIndicator.setF6Target(createTargetRequest.getTarget());
                    } else {
                        apIndicator.setL6Target(createTargetRequest.getTarget());
                    }
                    apIndicatorRepository.createDataApIndicator(apIndicator);
                } else {
                    apIndicator.setId(apIndicatorOld.get().getId());
                    apIndicator.setTarget(apIndicatorOld.get().getTarget() + createTargetRequest.getTarget());
                    if (10 <= createTargetRequest.getMonth() && createTargetRequest.getMonth() <= 12
                            && 1 <= createTargetRequest.getMonth() && createTargetRequest.getMonth() <= 3) {
                        apIndicator.setF6Target(apIndicatorOld.get().getF6Target() + createTargetRequest.getTarget());
                    } else {
                        apIndicator.setL6Target(apIndicatorOld.get().getL6Target() + createTargetRequest.getTarget());
                    }
                    apIndicatorRepository.updateDataApIndicator(apIndicator);
                }
            }

            //Target f6 month
            if (createTargetRequest.getTargetType() == 2) {
                for (int i = 1; i <= 3; i++) {
                    createTargetRequest.setMonth(i);
                    misRepository.createTarget(createTargetRequest);
                }
                for (int i = 10; i <= 12; i++) {
                    createTargetRequest.setMonth(i);
                    misRepository.createTarget(createTargetRequest);
                }
                if (apIndicatorOld.isEmpty()) {
                    apIndicator.setTarget(createTargetRequest.getTarget());
                    apIndicator.setF6Target(createTargetRequest.getTarget());
                    apIndicatorRepository.createDataApIndicator(apIndicator);
                } else {
                    apIndicator.setId(apIndicatorOld.get().getId());
                    if (apIndicatorOld.get().getF6Target() != null || apIndicatorOld.get().getF6Target() != 0) {
                        throw new BusinessException(ExceptionCode.ERROR_AP_HAD_TARGET_F6_MONTH);
                    }
                    apIndicator.setTarget(createTargetRequest.getTarget() + apIndicatorOld.get().getTarget());
                    apIndicator.setF6Target(createTargetRequest.getTarget());
                    apIndicatorRepository.updateDataApIndicator(apIndicator);
                }
            }

            // Target l6 month
            if (createTargetRequest.getTargetType() == 3) {
                for (int i = 4; i <= 9; i++) {
                    createTargetRequest.setMonth(i);
                    misRepository.createTarget(createTargetRequest);
                }
                if (apIndicatorOld.isEmpty()) {
                    apIndicator.setTarget(createTargetRequest.getTarget());
                    apIndicator.setL6Target(createTargetRequest.getTarget());
                    apIndicatorRepository.createDataApIndicator(apIndicator);
                } else {
                    apIndicator.setId(apIndicatorOld.get().getId());
                    if (apIndicatorOld.get().getL6Target() != null || apIndicatorOld.get().getL6Target() != 0) {
                        throw new BusinessException(ExceptionCode.ERROR_AP_HAD_TARGET_L6_MONTH);
                    }
                    apIndicator.setTarget(apIndicatorOld.get().getTarget() + createTargetRequest.getTarget());
                    apIndicator.setL6Target(createTargetRequest.getTarget());
                    apIndicatorRepository.updateDataApIndicator(apIndicator);
                }
            }

            if (createTargetRequest.getTargetType() == 4) {
                for (int i = 1; i <= 12; i++) {
                    createTargetRequest.setMonth(i);
                    misRepository.createTarget(createTargetRequest);
                }
                if (apIndicatorOld.isEmpty()) {
                    apIndicator.setTarget(createTargetRequest.getTarget());
                    apIndicatorRepository.createDataApIndicator(apIndicator);
                }
            }
        } catch (Exception e) {
            throw new BusinessException(ExceptionCode.INTERNAL_SERVER_ERROR);
        }
        return response(RESPONSE_CODE_SUCCESS);
    }
}
