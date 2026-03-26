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
        name = "DISTRICTS",
        indexes = {
                @Index(name = "IDX_DISTRICTS_NAME", columnList = "NAME"),
                @Index(name = "IDX_DISTRICTS_NAME_LOCAL", columnList = "NAME_LOCAL")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DISTRICT_ID", updatable = false, nullable = false)
    private Long districtId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "DIVISION_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_DISTRICTS_DIVISION")
    )
    private Division division;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "NAME_LOCAL", nullable = false, length = 100)
    private String nameLocal;

    @Column(name = "LAT", precision = 10)
    private Double lat;

    @Column(name = "LON", precision = 10)
    private Double lon;

    @Column(name = "URL", length = 255)
    private String url;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
