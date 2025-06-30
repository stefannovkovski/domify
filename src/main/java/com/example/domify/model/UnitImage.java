package com.example.domify.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "unitimage", schema = "domify")
@Data
@NoArgsConstructor
public class UnitImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 500)
    @Column(nullable = false, length = 500)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false, foreignKey = @ForeignKey(name = "fk_unitimage_unit"))
    private Unit unit;

    public UnitImage(String image, Unit unit) {
        this.image = image;
        this.unit = unit;
    }
}