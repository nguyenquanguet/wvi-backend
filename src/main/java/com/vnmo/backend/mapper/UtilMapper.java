package com.vnmo.backend.mapper;

import com.vnmo.backend.domains.Util;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UtilMapper {

     Optional<Util> findUtil();
}
