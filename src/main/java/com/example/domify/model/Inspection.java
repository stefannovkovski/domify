package com.example.domify.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "inspection", schema = "domify")
@Data
@NoArgsConstructor
public class Inspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "inspection_date", nullable = false)
    private LocalDate inspectionDate;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lease_id", nullable = false, foreignKey = @ForeignKey(name = "fk_inspection_lease"))
    private Lease lease;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "landlord_id", nullable = false, foreignKey = @ForeignKey(name = "fk_inspection_landlord"))
    private LandlordProfile landlord;

    public Inspection(LocalDate inspectionDate, String notes, Lease lease, LandlordProfile landlord) {
        this.inspectionDate = inspectionDate;
        this.notes = notes;
        this.lease = lease;
        this.landlord = landlord;
    }
}