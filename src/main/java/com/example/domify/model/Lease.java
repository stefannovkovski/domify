package com.example.domify.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "lease", schema = "domify")
@Data
@NoArgsConstructor
public class Lease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Min(0)
    @Column(name = "rent_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal rentAmount;

    @Min(0)
    @Column(name = "deposit_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal depositAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id", nullable = false, foreignKey = @ForeignKey(name = "fk_lease_listing"))
    private Listing listing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false, foreignKey = @ForeignKey(name = "fk_lease_tenant"))
    private TenantProfile tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "landlord_id", nullable = false, foreignKey = @ForeignKey(name = "fk_lease_landlord"))
    private LandlordProfile landlord;

    public Lease(LocalDate startDate, LocalDate endDate, BigDecimal rentAmount, BigDecimal depositAmount, Listing listing, TenantProfile tenant, LandlordProfile landlord) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.rentAmount = rentAmount;
        this.depositAmount = depositAmount;
        this.listing = listing;
        this.tenant = tenant;
        this.landlord = landlord;
    }
}