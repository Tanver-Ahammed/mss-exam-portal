package com.mss.exam.portal.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class MssRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
        implements MssRepository<T, ID> {

    private final EntityManager entityManager;

    public MssRepositoryImpl(JpaEntityInformation<T, ID> entityInformation,
                             EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public <Q> Q getResult(Function<EntityManager, Q> function) {
        return function.apply(entityManager);
    }

    @Override
    public <Q> List<Q> findAll(Specification<T> specification, Class<Q> clazz) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Q> query = builder.createQuery(clazz);
        Root<T> root = query.from(getDomainClass());

        applySpecification(specification, root, query, builder);

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public <Q> List<Q> findAll(Specification<T> specification, Sort sort, Class<Q> clazz) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Q> query = builder.createQuery(clazz);
        Root<T> root = query.from(getDomainClass());

        applySpecification(specification, root, query, builder);

        applySort(sort, root, query, builder);

        TypedQuery<Q> typedQuery = entityManager.createQuery(query);

        return typedQuery.getResultList();
    }

    // Ref : https://stackoverflow.com/a/52540647
    @Override
    public <Q> Page<Q> findAll(Specification<T> specification, Pageable pageable, Class<Q> clazz) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Q> query = builder.createQuery(clazz);
        Root<T> root = query.from(getDomainClass());

        applySpecification(specification, root, query, builder);

        // Ref : https://stackoverflow.com/a/61906871
        if (pageable != null) {
            applySort(pageable.getSort(), root, query, builder);
        }

        TypedQuery<Q> typedQuery = entityManager.createQuery(query);

        applyPaging(typedQuery, pageable);

        List<Q> list = typedQuery.getResultList();

        // Prepare the count result
        Long total = list.size() < pageable.getPageSize() ? list.size() : count(specification);

        return new PageImpl<>(list, pageable, total);
    }

    @Override
    public <Q> Page<Q> findAll(Specification<T> specification, Pageable pageable, Supplier<Long> totalSupplier, Class<Q> clazz) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Q> query = builder.createQuery(clazz);
        Root<T> root = query.from(getDomainClass());

        applySpecification(specification, root, query, builder);

        // Ref : https://stackoverflow.com/a/61906871
        if (pageable != null) {
            applySort(pageable.getSort(), root, query, builder);
        }

        TypedQuery<Q> typedQuery = entityManager.createQuery(query);

        applyPaging(typedQuery, pageable);

        List<Q> list = typedQuery.getResultList();

        // Prepare the count result
        Long total = list.size() < pageable.getPageSize() ? list.size() : totalSupplier.get();

        return new PageImpl<>(list, pageable, total);
    }

    @Override
    public <Q> Optional<Q> findFirst(Specification<T> specification, Class<Q> clazz) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Q> query = builder.createQuery(clazz);
        Root<T> root = query.from(getDomainClass());

        applySpecification(specification, root, query, builder);

        TypedQuery<Q> typedQuery = entityManager.createQuery(query).setMaxResults(1);

        List<Q> result = typedQuery.getResultList();

        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public Optional<T> findFirst(Specification<T> specification) {
        return findFirst(specification, getDomainClass());
    }

    private <Q> void applySpecification(Specification<T> specification, Root<T> root,
                                        CriteriaQuery<Q> query, CriteriaBuilder builder) {
        if (specification == null)
            return;

        Predicate predicate = specification.toPredicate(root, query, builder);
        if (predicate != null) {
            query.where(predicate);
        }
    }

    private <Q> void applySort(Sort sort, Root<T> root, CriteriaQuery<Q> query,
                               CriteriaBuilder builder) {
        if (sort != null && sort.isSorted()) {
            query.orderBy(QueryUtils.toOrders(sort, root, builder));
        }
    }

    private <Q> void applyPaging(TypedQuery<Q> typedQuery, Pageable pageable) {
        if (pageable != null && pageable.isPaged()) {
            typedQuery.setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize());
        }
    }
}
