package com.example.domify.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address", schema = "domify")
@Data
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 250)
    private String street;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String number;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String municipality;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String city;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String country;

    public Address(String street, String number, String municipality, String city, String country) {
        this.street = street;
        this.number = number;
        this.municipality = municipality;
        this.city = city;
        this.country = country;
    }
}
