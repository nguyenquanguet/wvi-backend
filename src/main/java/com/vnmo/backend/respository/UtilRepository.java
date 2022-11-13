package com.vnmo.backend.respository;

import com.vnmo.backend.domains.Util;
import com.vnmo.backend.mapper.UtilMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UtilRepository {

    private final UtilMapper utilMapper;

    public Optional<Util> findUtil(){
        return utilMapper.findUtil();
    }
}
