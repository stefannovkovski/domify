package com.example.domify.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterestedId implements Serializable {

    private Long listingId;
    private Long tenantProfileId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InterestedId that)) return false;
        return Objects.equals(listingId, that.listingId) &&
                Objects.equals(tenantProfileId, that.tenantProfileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listingId, tenantProfileId);
    }
}
