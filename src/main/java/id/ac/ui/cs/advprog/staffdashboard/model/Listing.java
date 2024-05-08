package id.ac.ui.cs.advprog.staffdashboard.model;

import jakarta.persistence.*;

@Entity
@Table(name = "listings")
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @ManyToOne
    @JoinColumn(name = "transaction_id", referencedColumnName = "transaction_id")
    private PurchaseRequest purchaseRequest;
}