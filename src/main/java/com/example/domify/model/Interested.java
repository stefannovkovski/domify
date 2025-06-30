package com.example.domify.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "interested", schema = "domify")
@Data
@NoArgsConstructor
public class Interested {

    @EmbeddedId
    private InterestedId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("listingId")
    @JoinColumn(name = "listing_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_interested_listing"))
    private Listing listing;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tenantProfileId")
    @JoinColumn(name = "tenant_profile_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_interested_tenant"))
    private TenantProfile tenantProfile;

    public Interested(Listing listing, TenantProfile tenantProfile) {
        this.listing = listing;
        this.tenantProfile = tenantProfile;
        this.id = new InterestedId(listing.getId(), tenantProfile.getId());
    }
}