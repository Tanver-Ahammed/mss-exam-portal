package com.mss.exam.portal.repository.geo;

import com.mss.exam.portal.entity.geo.District;
import com.mss.exam.portal.repository.MssRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends MssRepository<District, Long> {
    List<District> findByDivisionDivisionId(Long divisionId);
}
