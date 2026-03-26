package com.mss.exam.portal.repository;

import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@NoRepositoryBean
public interface MssRepository<ENTITY, ID extends Serializable>
        extends JpaRepository<ENTITY, ID>, JpaSpecificationExecutor<ENTITY> {

    public <Q> Q getResult(Function<EntityManager, Q> function);

    public <Q> List<Q> findAll(Specification<ENTITY> specification, Class<Q> clazz);

    public <Q> List<Q> findAll(Specification<ENTITY> specification, Sort sort, Class<Q> clazz);

    public <Q> Page<Q> findAll(Specification<ENTITY> specification, Pageable pageable,
                               Class<Q> clazz);

    public <Q> Page<Q> findAll(Specification<ENTITY> specification, Pageable pageable,
                               Supplier<Long> totalSupplier, Class<Q> clazz);

    public Optional<ENTITY> findFirst(Specification<ENTITY> specification);

    public <Q> Optional<Q> findFirst(Specification<ENTITY> specification, Class<Q> clazz);
}
