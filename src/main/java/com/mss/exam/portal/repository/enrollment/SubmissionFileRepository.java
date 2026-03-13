package com.mss.exam.portal.repository.enrollment;

import com.mss.exam.portal.entity.enrollment.SubmissionFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubmissionFileRepository extends JpaRepository<SubmissionFile, UUID> {
}
