package com.mss.exam.portal.repository.exam;

import com.mss.exam.portal.entity.exam.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
