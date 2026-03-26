package com.mss.exam.portal.specification.user;

import com.mss.exam.portal.entity.enums.UserStatus;
import com.mss.exam.portal.entity.geo.District;
import com.mss.exam.portal.entity.geo.District_;
import com.mss.exam.portal.entity.geo.Division;
import com.mss.exam.portal.entity.geo.Division_;
import com.mss.exam.portal.entity.geo.Upazila;
import com.mss.exam.portal.entity.geo.Upazila_;
import com.mss.exam.portal.entity.user.User;
import com.mss.exam.portal.entity.user.User_;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.SingularAttribute;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSpecification {

    private static String getLikeExpression(String value) {
        return '%' + value + '%';
    }

    private static <T> Join<User, T> getOrCreateJoin(Root<User> root,
                                                     SingularAttribute<User, T> attribute,
                                                     JoinType joinType) {
        return root.getJoins().stream()
                .filter(j -> j.getAttribute().equals(attribute))
                .map(j -> (Join<User, T>) j)
                .findFirst()
                .orElseGet(() -> root.join(attribute, joinType));
    }

    private static Specification<User> empty() {
        return (root, query, builder) -> builder.conjunction();
    }

    public static Specification<User> equalUserId(Long userId) {
        if (Objects.isNull(userId)) return empty();
        return (root, query, builder) ->
                builder.equal(root.get(User_.userId), userId);
    }

    public static Specification<User> likeName(String name) {
        if (StringUtils.isBlank(name)) return empty();
        return (root, query, builder) ->
                builder.like(builder.lower(root.get(User_.name)),
                        getLikeExpression(name.trim().toLowerCase()));
    }

    public static Specification<User> likeNameLocal(String nameLocal) {
        if (StringUtils.isBlank(nameLocal)) return empty();
        return (root, query, builder) ->
                builder.like(root.get(User_.nameLocal),
                        getLikeExpression(nameLocal.trim()));
    }

    public static Specification<User> equalMobileNumber(String mobileNumber) {
        if (StringUtils.isBlank(mobileNumber)) return empty();
        return (root, query, builder) ->
                builder.equal(root.get(User_.phone), mobileNumber);
    }

    public static Specification<User> equalEmail(String email) {
        if (StringUtils.isBlank(email)) return empty();
        return (root, query, builder) ->
                builder.equal(root.get(User_.email), email);
    }

    public static Specification<User> equalUsername(String username) {
        if (StringUtils.isBlank(username)) return empty();
        return (root, query, builder) ->
                builder.equal(root.get(User_.username), username);
    }

    public static Specification<User> equalDivisionId(Long divisionId) {
        if (Objects.isNull(divisionId)) return empty();
        return (root, query, builder) ->
                builder.equal(
                        getOrCreateJoin(root, User_.division, JoinType.LEFT)
                                .get(Division_.divisionId),
                        divisionId);
    }

    public static Specification<User> equalDistrictId(Long districtId) {
        if (Objects.isNull(districtId)) return empty();
        return (root, query, builder) ->
                builder.equal(
                        getOrCreateJoin(root, User_.district, JoinType.LEFT)
                                .get(District_.districtId),
                        districtId);
    }

    public static Specification<User> equalUpazilaId(Long upazilaId) {
        if (Objects.isNull(upazilaId)) return empty();
        return (root, query, builder) ->
                builder.equal(
                        getOrCreateJoin(root, User_.upazila, JoinType.LEFT)
                                .get(Upazila_.upazilaId),
                        upazilaId);
    }

    public static Specification<User> statusIn(UserStatus status) {
        if (Objects.isNull(status)) return empty();
        return (root, query, builder) ->
                builder.equal(root.get(User_.status), status);
    }

    public static Specification<User> selectUserDto() {
        return (root, query, builder) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                Join<User, Division> divisionJoin = getOrCreateJoin(root, User_.division, JoinType.LEFT);
                Join<User, District> districtJoin = getOrCreateJoin(root, User_.district, JoinType.LEFT);
                Join<User, Upazila> upazilaJoin = getOrCreateJoin(root, User_.upazila, JoinType.LEFT);

                query.distinct(true);
                query.multiselect(
                        root.get(User_.userId),
                        root.get(User_.name),
                        root.get(User_.nameLocal),
                        root.get(User_.username),
                        root.get(User_.email),
                        root.get(User_.phone),
                        root.get(User_.role),
                        root.get(User_.status),
                        divisionJoin.get(Division_.name),
                        divisionJoin.get(Division_.nameLocal),
                        districtJoin.get(District_.name),
                        districtJoin.get(District_.nameLocal),
                        upazilaJoin.get(Upazila_.name),
                        upazilaJoin.get(Upazila_.nameLocal),
                        root.get(User_.createdAt)
                );
            }
            return null;
        };
    }
}