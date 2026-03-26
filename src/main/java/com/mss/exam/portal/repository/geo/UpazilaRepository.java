package com.mss.exam.portal.repository.geo;

import com.mss.exam.portal.entity.geo.Upazila;
import com.mss.exam.portal.repository.MssRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpazilaRepository extends MssRepository<Upazila, Long> {
    List<Upazila> findByDistrictDistrictId(Long districtId);
}
