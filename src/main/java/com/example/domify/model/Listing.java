package com.example.domify.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "listing", schema = "domify")
@Data
@NoArgsConstructor
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String title;

    @NotNull
    @Column(name = "available_from", nullable = false)
    private LocalDate availableFrom;

    @NotNull
    @Column(name = "available_to", nullable = false)
    private LocalDate availableTo;

    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String status = "available";

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false, foreignKey = @ForeignKey(name = "fk_listing_unit"))
    private Unit unit;

    public Listing(String title, LocalDate availableFrom, LocalDate availableTo, String status, String description, Unit unit) {
        this.title = title;
        this.availableFrom = availableFrom;
        this.availableTo = availableTo;
        this.status = status;
        this.description = description;
        this.unit = unit;
    }
}