package id.ac.ui.cs.advprog.staffdashboard.service;

import id.ac.ui.cs.advprog.staffdashboard.model.PurchaseRequest;
import id.ac.ui.cs.advprog.staffdashboard.repository.PurchaseRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestServicePurchaseTest {
    @Mock
    private PurchaseRequestRepository purchaseRequestRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RequestServiceImpl<PurchaseRequest> purchaseRequestService2;

    @InjectMocks
    private RequestServiceImpl<PurchaseRequest> purchaseRequestService = new RequestServiceImpl<PurchaseRequest>() {
        @Override
        public PurchaseRequest updateStatus(PurchaseRequest request, String verdict, String token) {
            return null;
        }

        @Override
        public Collection<PurchaseRequest> collectRequest(String token) throws Exception {
            return null;
        }
    };

    @Value("${auth.url}")
    private String authUrl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        purchaseRequestService.setAuthUrl(authUrl);  // Assuming there's a setter or you can set it directly if it's not final
        System.out.println(authUrl);
    }

    @Test
    void testAddPurchaseRequest() {
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequestService.add(purchaseRequest);
        verify(purchaseRequestRepository, times(1)).save(any(PurchaseRequest.class));
    }

    @Test
    void testFindAllPurchaseRequest() {
        PurchaseRequest purchaseRequest1 = new PurchaseRequest();
        purchaseRequest1.setTransactionId("abcd");
        PurchaseRequest purchaseRequest2 = new PurchaseRequest();
        purchaseRequest2.setTransactionId("efgh");
        Collection<PurchaseRequest> purchaseRequestList = Arrays.asList(purchaseRequest1, purchaseRequest2);

        when(purchaseRequestRepository.findAll()).thenReturn((List<PurchaseRequest>) purchaseRequestList);

        Collection<PurchaseRequest> result = purchaseRequestService.findAll();

        assertEquals(purchaseRequestList, result);
    }

    @Test
    void testFindById() {
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setTransactionId("abcd");

        when(purchaseRequestRepository.findById("abcd")).thenReturn(Optional.of(purchaseRequest));

        PurchaseRequest result = purchaseRequestService.findById("abcd");

        assertEquals(purchaseRequest, result);

        when(purchaseRequestRepository.findById("efgh")).thenReturn(Optional.empty());

        result = purchaseRequestService.findById("efgh");

        assertNull(result);
    }

    @Test
    void testDeleteById() {
        doNothing().when(purchaseRequestRepository).deleteById("abcd");

        CompletableFuture<Void> result = purchaseRequestService.deleteById("abcd");

        verify(purchaseRequestRepository, times(1)).deleteById("abcd");

        assertNull(result);
    }

    @Test
    void testDeleteAll() {
        doNothing().when(purchaseRequestRepository).deleteAll();

        CompletableFuture<Void> result = purchaseRequestService.deleteAll();

        verify(purchaseRequestRepository, times(1)).deleteAll();
    }

    @Test
    void testAuthenticateStaff() throws Exception {
        String token = "Bearer valid-token";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        ResponseEntity<String> responseEntity = new ResponseEntity<>("STAFF", HttpStatus.OK);
        lenient().when(restTemplate.exchange(eq(authUrl + "/user/get-role"), eq(HttpMethod.GET), eq(entity), eq(String.class)))
                .thenReturn(responseEntity);

        boolean result = purchaseRequestService.authenticateStaff(token);

        assertFalse(result);
    }

    @Test
    void testAuthenticateStaff_InvalidRole() throws Exception {
        String token = "Bearer valid-token";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        ResponseEntity<String> responseEntity = new ResponseEntity<>("USER", HttpStatus.OK);
        lenient().when(restTemplate.exchange(eq(authUrl + "/user/get-role"), eq(HttpMethod.GET), eq(entity), eq(String.class)))
                .thenReturn(responseEntity);

        boolean result = purchaseRequestService.authenticateStaff(token);

        assertFalse(result);
    }
}
