package com.mss.exam.portal.controller.geo;

import com.mss.exam.portal.Routes;
import com.mss.exam.portal.dto.geo.DistrictDto;
import com.mss.exam.portal.dto.geo.DivisionDto;
import com.mss.exam.portal.dto.geo.UpazilaDto;
import com.mss.exam.portal.service.geo.GeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GeoController {

    private final GeoService geoService;

    @Autowired
    public GeoController(GeoService geoService) {
        this.geoService = geoService;
    }

    @GetMapping(Routes.API_DIVISIONS)
    public ResponseEntity<List<DivisionDto>> getAllDivisions() {
        return ResponseEntity.ok(geoService.findAllDivisions());
    }

    @GetMapping(Routes.API_DISTRICTS)
    public ResponseEntity<List<DistrictDto>> getDistrictsByDivision(@PathVariable Long divisionId) {
        return ResponseEntity.ok(geoService.findDistrictsByDivisionId(divisionId));
    }

    @GetMapping(Routes.API_UPAZILAS)
    public ResponseEntity<List<UpazilaDto>> getUpazilasByDistrict(@PathVariable Long districtId) {
        return ResponseEntity.ok(geoService.findUpazilasByDistrictId(districtId));
    }
}
