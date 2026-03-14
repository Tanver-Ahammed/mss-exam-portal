package com.mss.exam.portal.repository.enrollment;

import com.mss.exam.portal.entity.enrollment.SubmissionFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionFileRepository extends JpaRepository<SubmissionFile, Long> {
}
