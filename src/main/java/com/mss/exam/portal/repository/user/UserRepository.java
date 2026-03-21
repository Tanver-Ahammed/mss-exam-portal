package com.mss.exam.portal.repository.user;

import com.mss.exam.portal.dto.user.UserDto;
import com.mss.exam.portal.dto.user.UserFilter;
import com.mss.exam.portal.entity.user.User;
import com.mss.exam.portal.repository.MssRepository;
import com.mss.exam.portal.specification.user.UserSpecification;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MssRepository<User, Long> {

    default Page<UserDto> filterUser(UserFilter filter, Pageable pageable) {

        Specification<User> spec = Specification
                .where(UserSpecification.equalDivisionId(filter.divisionId()))
                .and(UserSpecification.equalDistrictId(filter.districtId()))
                .and(UserSpecification.equalUpazilaId(filter.upazilaId()))
                .and(UserSpecification.statusIn(filter.status()))
                .and(UserSpecification.selectUserDto());

        if (StringUtils.isNotBlank(filter.omniSearch())) {
            Specification<User> omniSearchSpec = Specification
                    .where(UserSpecification.likeName(filter.omniSearch()))
                    .or(UserSpecification.likeNameLocal(filter.omniSearch()))
                    .or(UserSpecification.equalMobileNumber(filter.omniSearch()))
                    .or(UserSpecification.equalEmail(filter.omniSearch()));

            spec = spec.and(omniSearchSpec);
        }

        if (pageable.isPaged()) {
            return findAll(spec, pageable, UserDto.class);
        } else {
            return new PageImpl<>(findAll(spec, pageable.getSort(), UserDto.class));
        }
    }

}
