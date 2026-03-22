package com.mss.exam.portal.service.geo;

import com.mss.exam.portal.dto.geo.DistrictDto;
import com.mss.exam.portal.dto.geo.DivisionDto;
import com.mss.exam.portal.dto.geo.UpazilaDto;
import com.mss.exam.portal.repository.geo.DistrictRepository;
import com.mss.exam.portal.repository.geo.DivisionRepository;
import com.mss.exam.portal.repository.geo.UpazilaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GeoService {

    private final DivisionRepository divisionRepository;
    private final DistrictRepository districtRepository;
    private final UpazilaRepository upazilaRepository;

    @Autowired
    public GeoService(DivisionRepository divisionRepository,
                      DistrictRepository districtRepository,
                      UpazilaRepository upazilaRepository) {
        this.divisionRepository = divisionRepository;
        this.districtRepository = districtRepository;
        this.upazilaRepository = upazilaRepository;
    }

    public List<DivisionDto> findAllDivisions() {
        return divisionRepository.findAll().stream()
                .map(DivisionDto::from)
                .toList();
    }

    public List<DistrictDto> findDistrictsByDivisionId(Long divisionId) {
        return districtRepository.findByDivisionDivisionId(divisionId).stream()
                .map(DistrictDto::from)
                .toList();
    }

    public List<UpazilaDto> findUpazilasByDistrictId(Long districtId) {
        return upazilaRepository.findByDistrictDistrictId(districtId).stream()
                .map(UpazilaDto::from)
                .toList();
    }
}
