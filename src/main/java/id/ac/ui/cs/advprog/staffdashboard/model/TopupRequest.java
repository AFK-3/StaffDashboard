package id.ac.ui.cs.advprog.staffdashboard.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "topup_requests")
public class TopupRequest {

    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "payment_amount")
    private int paymentAmount;

    @Column(name = "payment_request_time")
    private Long paymentRequestTime;

    @Column(name = "payment_response_time")
    private Long paymentResponseTime;

    @Column(name = "buyer_username")
    private String buyerUsername;
}
