package com.mss.exam.portal.entity.geo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "UPAZILAS",
        indexes = {
                @Index(name = "IDX_UPAZILAS_NAME", columnList = "NAME"),
                @Index(name = "IDX_UPAZILAS_NAME_LOCAL", columnList = "NAME_LOCAL")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Upazila {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UPAZILA_ID", updatable = false, nullable = false)
    private Long upazilaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "DISTRICT_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_UPAZILAS_DISTRICT")
    )
    private District district;

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
