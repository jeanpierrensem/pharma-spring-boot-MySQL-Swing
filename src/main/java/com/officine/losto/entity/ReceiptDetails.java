package com.officine.losto.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.*;

import java.time.*;

/**
 * Réception liée à une ligne de commande. Colonne H2 <code>DATE</code> (TIMESTAMP(6)).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
@Entity
@Table(name = "RECEIPT_DETAILS")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public final class ReceiptDetails extends AbstractEntity {

    @Column(name = "DATE", nullable = false)
    private LocalDateTime date;

    @Column(name = "MISSING_QUANTITY", nullable = false)
    private Integer missingQuantity;

    @Column(name = "OBSERVATION")
    private String observation;

    @Column(name = "RECEIVED_QUANTITY", nullable = false)
    private Integer receivedQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDERS_DETAILS_ID")
    private OrdersDetails ordersDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private AppUser user;
}
