package com.mss.exam.portal.repository.exam;

import com.mss.exam.portal.entity.exam.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findByName(String name);

    List<Subject> findByActiveTrueOrderByDisplayOrderAsc();

    List<Subject> findByActiveTrueAndDeletedFalseOrderByDisplayOrderAsc();
}
