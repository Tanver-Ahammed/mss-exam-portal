package com.mss.exam.portal.repository.exam;

import com.mss.exam.portal.entity.exam.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
}
