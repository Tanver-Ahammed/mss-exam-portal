package com.mss.exam.portal.service;

import com.mss.exam.portal.dto.user.UserDto;
import com.mss.exam.portal.dto.user.UserFilter;
import com.mss.exam.portal.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public long count() {
        return userRepository.count();
    }

    public Page<UserDto> findAll(UserFilter userFilter, Pageable pageable) {
        return userRepository.filterUser(userFilter, pageable);
    }

}
