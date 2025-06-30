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
import java.time.LocalDate;

@Entity
@Table(name = "payment", schema = "domify")
@Data
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(0)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String status;

    @NotNull
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lease_id", nullable = false, foreignKey = @ForeignKey(name = "fk_payment_lease"))
    private Lease lease;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = false, foreignKey = @ForeignKey(name = "fk_payment_method"))
    private PaymentMethod paymentMethod;

    public Payment(BigDecimal amount, String status, LocalDate paymentDate, Lease lease, PaymentMethod paymentMethod) {
        this.amount = amount;
        this.status = status;
        this.paymentDate = paymentDate;
        this.lease = lease;
        this.paymentMethod = paymentMethod;
    }
}