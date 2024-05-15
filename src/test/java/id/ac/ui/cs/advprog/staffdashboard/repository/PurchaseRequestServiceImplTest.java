package id.ac.ui.cs.advprog.staffdashboard.repository;

import id.ac.ui.cs.advprog.staffdashboard.model.PurchaseRequest;
import id.ac.ui.cs.advprog.staffdashboard.repository.PurchaseRequestRepository;
import id.ac.ui.cs.advprog.staffdashboard.service.RequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PurchaseRequestServiceImplTest {

    @Mock
    private PurchaseRequestRepository purchaseRequestRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RequestServiceImpl<PurchaseRequest> purchaseRequestService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateStatus() {
        // Prepare test data
        PurchaseRequest request = new PurchaseRequest();
        request.setTransactionId(UUID.randomUUID().toString());
        String verdict = "APPROVED";
        String token = "mock-token";

        // Mock restTemplate.exchange() method
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(String.class),
                anyMap()
        )).thenReturn(new ResponseEntity<>("Success", HttpStatus.OK));

        // Mock repository delete method
        doNothing().when(purchaseRequestRepository).delete(request);

        // Call the method to be tested
        PurchaseRequest updatedRequest = purchaseRequestService.updateStatus(request, verdict, token);

        // Verify interactions
        verify(restTemplate).exchange(
                anyString(),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(String.class),
                anyMap()
        );
        verify(purchaseRequestRepository).delete(request);

        // Assert the returned object
        assertSame(request, updatedRequest); // Ensure same instance is returned
    }

    @Test
    public void testAllowToReview() {
        // Prepare test data
        PurchaseRequest request = new PurchaseRequest();
        request.setUsername("john_doe");
        request.setListings(new HashMap<>()); // Set empty listings for simplicity
        String verdict = "ALLOW";
        String token = "mock-token";

        // Mock restTemplate.exchange() method
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Void.class)
        )).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Call the method to be tested
        purchaseRequestService.allowToReview(request, verdict, token);

        // Verify interactions
        verify(restTemplate).exchange(
                eq("http://localhost:8084/allowUserToReview"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Void.class)
        );
    }
}

