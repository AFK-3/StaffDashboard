package id.ac.ui.cs.advprog.staffdashboard.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "purchase_requests")
public class PurchaseRequest {

    @Id
    @Column(name = "transactionId")
    private String transactionId;

    @Column(name = "username")
    private String username;

    @Column(name = "status")
    private String status;

    @Column(name = "deliveryLocation")
    private String deliveryLocation;

    @ElementCollection
    @CollectionTable(name = "listingsTransaction", joinColumns = @JoinColumn(name = "transactionId"))
    @MapKeyColumn(name = "listingId")
    @Column(name = "quantity")
    private Map<String,Integer> listings;

    @Column(name = "totalPrice")
    private long totalPrice;
}
