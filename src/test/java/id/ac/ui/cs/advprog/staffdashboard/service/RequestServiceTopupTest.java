package id.ac.ui.cs.advprog.staffdashboard.service;


import id.ac.ui.cs.advprog.staffdashboard.model.TopupRequest;
import id.ac.ui.cs.advprog.staffdashboard.repository.PurchaseRequestRepository;
import id.ac.ui.cs.advprog.staffdashboard.repository.TopupRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
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
public class RequestServiceTopupTest {
    @Mock
    private TopupRequestRepository topupRequestRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock RequestServiceImpl<TopupRequest> topupRequestService2;


    @InjectMocks
    private RequestServiceImpl<TopupRequest> topupRequestService = new RequestServiceImpl<TopupRequest>() {
        @Override
        public TopupRequest updateStatus(TopupRequest request, String verdict, String token) {
            return null;
        }

        @Override
        public Collection<TopupRequest> collectRequest(String token) throws Exception {
            return null;
        }
    };

    @Value("${auth.url}")
    private String authUrl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        topupRequestService.setAuthUrl(authUrl);  // Assuming there's a setter or you can set it directly if it's not final
        System.out.println(authUrl);
    }

    @Test
    void testAddTopupRequest(){
        TopupRequest topupRequest = new TopupRequest();
        topupRequestService.add(topupRequest);
        verify(topupRequestRepository, times(1)).save(any(TopupRequest.class));
    }

    @Test
    void testFindAllTopupRequest() {
        TopupRequest topupRequest1 = new TopupRequest();
        topupRequest1.setId("abcd");
        TopupRequest topupRequest2 = new TopupRequest();
        topupRequest2.setId("efgh");
        Collection<TopupRequest> topupRequestList = Arrays.asList(topupRequest1, topupRequest2);

        when(topupRequestRepository.findAll()).thenReturn((List<TopupRequest>) topupRequestList);

        Collection<TopupRequest> result =  topupRequestService.findAll();

        assertEquals(topupRequestList, result);
    }

    @Test
    void testFindById() {
        TopupRequest topupRequest = new TopupRequest();
        topupRequest.setId("abcd");

        when(topupRequestRepository.findById("abcd")).thenReturn(Optional.of(topupRequest));

        TopupRequest result = topupRequestService.findById("abcd");

        assertEquals(topupRequest, result);

        when(topupRequestRepository.findById("efgh")).thenReturn(Optional.empty());

        result = topupRequestService.findById("efgh");

        assertNull(result);
    }

    @Test
    void testDeleteById() {
        doNothing().when(topupRequestRepository).deleteById("abcd");

        CompletableFuture<Void> result = topupRequestService.deleteById("abcd");

        verify(topupRequestRepository, times(1)).deleteById("abcd");

        assertNull(result);
    }

    @Test
    void testDeleteAll() {
        doNothing().when(topupRequestRepository).deleteAll();

        CompletableFuture<Void> result = topupRequestService.deleteAll();

        verify(topupRequestRepository, times(1)).deleteAll();
    }

    @Test
    void testAuthenticateStaff() throws Exception {
        String token = "Bearer valid-token";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        ResponseEntity<String> responseEntity = new ResponseEntity<>("STAFF", HttpStatus.OK);
        lenient().when(restTemplate.exchange(eq(authUrl+"/user/get-role"), eq(HttpMethod.GET), eq(entity), eq(String.class)))
                .thenReturn(responseEntity);

        boolean result = topupRequestService.authenticateStaff(token);

        assertTrue(!result);
    }

    @Test
    void testAuthenticateStaff_InvalidRole() throws Exception {
        String token = "Bearer valid-token";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        ResponseEntity<String> responseEntity = new ResponseEntity<>("USER", HttpStatus.OK);
        lenient().when(restTemplate.exchange(eq(authUrl+"user/get-role"), eq(HttpMethod.GET), eq(entity), eq(String.class)))
                .thenReturn(responseEntity);

        boolean result = topupRequestService.authenticateStaff(token);

        assertFalse(result);
    }

    @Test
    void testUpdateProcessStruct() throws Exception {
        // Mock the RequestServiceImpl object
        RequestServiceImpl<Object> randomObject = Mockito.mock(RequestServiceImpl.class, CALLS_REAL_METHODS);

        // Mock the requestRepository field
        JpaRepository<Object, String> requestRepository = mock(JpaRepository.class);
        randomObject.requestRepository = requestRepository;

        // Create a dummy request object
        Object request = new Object();
        String verdict = "ACCEPTED";
        String token = "Bearer valid-token";

        // Mock the methods to do nothing or return appropriate values


        // Call the updateStatusProcess method
        randomObject.updateStatusProcess(request, verdict, token);

        // Verify that the methods were called correctly
        verify(randomObject, times(1)).deleteAll();
        verify(randomObject, times(1)).allowToReview(request, verdict, token);
        verify(randomObject, times(1)).updateStatus(request, verdict, token);
        verify(randomObject, times(1)).collectRequest(token);
    }

    /*
    @Test
    void testUpdateStatusProcess() throws Exception {

        TopupRequest request = new TopupRequest();
        String token = "Bearer valid-token";
        String verdict = "ACCEPTED";

        TopupRequest result = topupRequestService2.updateStatusProcess(request, verdict, token);

        verify(topupRequestService2).updateStatusProcess(request, verdict, token);
    }




    @Test
    void testCollectRequest() throws Exception {
        // Given
        String token = "test-token";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);

        JSONObject process = new JSONObject();
        process.put("id", 1L);
        process.put("status", "PENDING");

        JSONArray paymentResponse = new JSONArray();
        paymentResponse.put(process);

        JSONObject outerObject = new JSONObject();
        outerObject.put("paymentsRequest", paymentResponse);

        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(outerObject.toString());
        when(restTemplate.exchange(
                paymentUrl + "/payment-request/get-all",
                HttpMethod.GET,
                httpEntity,
                String.class
        )).thenReturn(responseEntity);

        TopupRequest savedRequest = new TopupRequest();
        savedRequest.setId(1L);
        savedRequest.setStatus("PENDING");

        when(requestRepository.save(any(TopupRequest.class))).thenReturn(savedRequest);
        when(requestRepository.findAll()).thenReturn(List.of(savedRequest));

        // When
        Collection<TopupRequest> result = topupRequestService.collectRequest(token);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(savedRequest.getId(), result.iterator().next().getId());
    }*/
}
