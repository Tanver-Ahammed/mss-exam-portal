package com.mss.exam.portal.dto.geo;

import com.mss.exam.portal.entity.geo.Upazila;

public record UpazilaDto(
        Long upazilaId,
        Long districtId,
        String name,
        String nameLocal
) {
    public static UpazilaDto from(Upazila upazila) {
        return new UpazilaDto(
                upazila.getUpazilaId(),
                upazila.getDistrict().getDistrictId(),
                upazila.getName(),
                upazila.getNameLocal()
        );
    }
}
