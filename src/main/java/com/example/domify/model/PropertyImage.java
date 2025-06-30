package com.example.domify.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "propertyimage", schema = "domify")
@Data
@NoArgsConstructor
public class PropertyImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 500)
    @Column(nullable = false, length = 500)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, foreignKey = @ForeignKey(name = "fk_propertyimage_property"))
    private Property property;

    public PropertyImage(String image, Property property) {
        this.image = image;
        this.property = property;
    }
}
