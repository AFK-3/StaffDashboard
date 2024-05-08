package id.ac.ui.cs.advprog.staffdashboard.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "purchase_requests")
public class PurchaseRequest {

    @Id
    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "username")
    private String username;

    @Column(name = "status")
    private String status;

    @Column(name = "delivery_location")
    private String deliveryLocation;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "purchaseRequest")
    private List<Listing> listings;

    @Column(name = "total_price")
    private long totalPrice;
}
