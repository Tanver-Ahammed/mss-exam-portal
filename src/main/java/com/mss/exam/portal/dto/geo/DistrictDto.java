package com.mss.exam.portal.dto.geo;

import com.mss.exam.portal.entity.geo.District;

public record DistrictDto(
        Long districtId,
        Long divisionId,
        String name,
        String nameLocal
) {
    public static DistrictDto from(District district) {
        return new DistrictDto(
                district.getDistrictId(),
                district.getDivision().getDivisionId(),
                district.getName(),
                district.getNameLocal()
        );
    }
}
