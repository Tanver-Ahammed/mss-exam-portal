package com.mss.exam.portal.repository.enrollment;

import com.mss.exam.portal.entity.enrollment.ExamAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {
}
