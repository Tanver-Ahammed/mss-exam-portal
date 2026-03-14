package com.mss.exam.portal.repository.user;

import com.mss.exam.portal.entity.user.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFileRepository extends JpaRepository<UserFile, Long> {
}
