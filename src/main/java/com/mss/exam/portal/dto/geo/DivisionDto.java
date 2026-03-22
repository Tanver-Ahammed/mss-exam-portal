package com.mss.exam.portal.dto.geo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mss.exam.portal.entity.geo.Division;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public record DivisionDto(
        Long divisionId,
        String name,
        String nameLocal
) {
    public static DivisionDto from(Division division) {
        return new DivisionDto(
                division.getDivisionId(),
                division.getName(),
                division.getNameLocal()
        );
    }

    @JsonIgnore
    public String getLocalizedName() {
        return isLocaleBengali() ? nameLocal : name;
    }

    private boolean isLocaleBengali() {
        return !Locale.ENGLISH.getLanguage().equals(LocaleContextHolder.getLocale().getLanguage());
    }

}
