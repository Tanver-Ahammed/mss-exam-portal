package com.mss.exam.portal.repo.course;

import com.mss.exam.portal.entity.course.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseCategoryRepository extends JpaRepository<CourseCategory, UUID> {

    Optional<CourseCategory> findByName(String name);

    List<CourseCategory> findByActiveTrueOrderByDisplayOrderAsc();

    List<CourseCategory> findByActiveTrueAndDeletedFalseOrderByDisplayOrderAsc();
}
