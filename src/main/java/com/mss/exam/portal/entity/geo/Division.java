package com.mss.exam.portal.entity.geo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "DIVISIONS",
        uniqueConstraints = {
                @UniqueConstraint(name = "UQ_DIVISIONS_NAME", columnNames = "NAME"),
                @UniqueConstraint(name = "UQ_DIVISIONS_NAME_LOCAL", columnNames = "NAME_LOCAL")
        },
        indexes = {
                @Index(name = "IDX_DIVISIONS_NAME_LOCAL", columnList = "NAME_LOCAL")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Division {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DIVISION_ID", updatable = false, nullable = false)
    private Long divisionId;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "NAME_LOCAL", nullable = false, length = 100)
    private String nameLocal;

    @Column(name = "URL", length = 255)
    private String url;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
