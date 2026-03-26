package com.mss.exam.portal.repository.geo;

import com.mss.exam.portal.entity.geo.Division;
import com.mss.exam.portal.repository.MssRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DivisionRepository extends MssRepository<Division, Long> {
}
