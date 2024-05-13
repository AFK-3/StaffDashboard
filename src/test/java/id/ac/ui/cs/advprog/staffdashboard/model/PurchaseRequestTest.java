package id.ac.ui.cs.advprog.staffdashboard.model;

import id.ac.ui.cs.advprog.staffdashboard.model.Enum.RequestStatus;
import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PurchaseRequestTest {

    @Test
    public void testPurchaseRequestProperties() {
        // Create a sample PurchaseRequest instance
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setTransactionId("12345");
        purchaseRequest.setUsername("john_doe");
        purchaseRequest.setStatus(RequestStatus.WAITING_RESPONSE.getStatus());
        purchaseRequest.setDeliveryLocation("Home");

        // Create a sample listings map
        Map<String, Integer> listings = new HashMap<>();
        listings.put("product1", 2);
        listings.put("product2", 1);
        purchaseRequest.setListings(listings);

        purchaseRequest.setTotalPrice(150L);

        // Verify the properties of the PurchaseRequest
        assertEquals("12345", purchaseRequest.getTransactionId());
        assertEquals("john_doe", purchaseRequest.getUsername());
        assertEquals("WAITING_RESPONSE", purchaseRequest.getStatus());
        assertEquals("Home", purchaseRequest.getDeliveryLocation());
        assertEquals(listings, purchaseRequest.getListings());
        assertEquals(150L, purchaseRequest.getTotalPrice());
    }

}

