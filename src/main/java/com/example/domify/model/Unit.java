package com.example.domify.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "unit", schema = "domify")
@Data
@NoArgsConstructor
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "unit_number", nullable = false, length = 50)
    private String unitNumber;

    @NotNull
    @Column(nullable = false)
    private Integer floor;

    @Min(0)
    @Column(nullable = false)
    private Integer bedrooms;

    @Min(0)
    @Column(nullable = false)
    private Integer bathrooms;

    @Min(0)
    @Column(name = "area_sq_m", nullable = false, precision = 8, scale = 2)
    private BigDecimal areaSqM;

    @Column(name = "rent_amount", precision = 10, scale = 2)
    private BigDecimal rentAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, foreignKey = @ForeignKey(name = "fk_unit_property"))
    private Property property;

    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UnitImage> images;
    public Unit(String unitNumber, Integer floor, Integer bedrooms, Integer bathrooms, BigDecimal areaSqM, BigDecimal rentAmount, Property property) {
        this.unitNumber = unitNumber;
        this.floor = floor;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.areaSqM = areaSqM;
        this.rentAmount = rentAmount;
        this.property = property;
        this.images = new ArrayList<>();
    }
}