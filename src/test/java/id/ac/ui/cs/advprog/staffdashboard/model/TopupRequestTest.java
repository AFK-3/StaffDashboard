package id.ac.ui.cs.advprog.staffdashboard.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

public class TopupRequestTest {

    @Test
    public void testTopupRequestProperties() {
        // Create a sample UUID for the ID field
        String id="Hewwo";

        // Create a sample TopupRequest instance
        TopupRequest topupRequest = new TopupRequest();
        topupRequest.setId(id);
        topupRequest.setPaymentStatus("Pending");
        topupRequest.setPaymentAmount(100);
        topupRequest.setPaymentRequestTime(System.currentTimeMillis());
        topupRequest.setPaymentResponseTime(null); // Payment response time not set
        topupRequest.setBuyerUsername("alice");

        // Verify the properties of the TopupRequest
        assertEquals(id, topupRequest.getId());
        assertEquals("Pending", topupRequest.getPaymentStatus());
        assertEquals(100, topupRequest.getPaymentAmount());
        // Verify that paymentResponseTime is null
        assertEquals(null, topupRequest.getPaymentResponseTime());
        assertEquals("alice", topupRequest.getBuyerUsername());
    }
}
