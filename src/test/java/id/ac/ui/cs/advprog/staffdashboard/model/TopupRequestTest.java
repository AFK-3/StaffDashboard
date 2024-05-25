package id.ac.ui.cs.advprog.staffdashboard.model;

import id.ac.ui.cs.advprog.staffdashboard.model.Enum.RequestStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TopupRequestTest {

    @Test
    public void testTopupRequestProperties() {
        // Create a sample UUID for the ID field
        String id="Hewwo";

        // Create a sample TopupRequest instance
        TopupRequest topupRequest = new TopupRequest();
        topupRequest.setId(id);
        topupRequest.setPaymentStatus(RequestStatus.WAITING_RESPONSE.getStatus());
        topupRequest.setPaymentAmount(100);
        topupRequest.setPaymentRequestTime(System.currentTimeMillis());
        topupRequest.setPaymentResponseTime(null); // Payment response time not set
        topupRequest.setBuyerUsername("alice");
        topupRequest.getPaymentResponseTime();

        // Verify the properties of the TopupRequest
        assertEquals(id, topupRequest.getId());
        assertTrue(RequestStatus.contains(topupRequest.getPaymentStatus()));
        assertFalse(RequestStatus.contains("Pending"));
        assertEquals(100, topupRequest.getPaymentAmount());
        // Verify that paymentResponseTime is null
        assertNull(topupRequest.getPaymentResponseTime());
        assertEquals("alice", topupRequest.getBuyerUsername());
    }
}
