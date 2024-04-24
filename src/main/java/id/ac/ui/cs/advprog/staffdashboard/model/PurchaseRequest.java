package id.ac.ui.cs.advprog.staffdashboard.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter @Getter
public class PurchaseRequest {
    private String transactionId;
    private String username;
    private String status;
    private String deliveryLocation;
    private Map<String,Integer> listings;
    private long totalPrice;
}
