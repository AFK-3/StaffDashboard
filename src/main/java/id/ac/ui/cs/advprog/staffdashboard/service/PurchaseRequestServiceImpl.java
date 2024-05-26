package id.ac.ui.cs.advprog.staffdashboard.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.staffdashboard.model.Enum.RequestStatus;
import id.ac.ui.cs.advprog.staffdashboard.model.PurchaseRequest;
import id.ac.ui.cs.advprog.staffdashboard.repository.PurchaseRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class PurchaseRequestServiceImpl extends RequestServiceImpl<PurchaseRequest> {

    public static String buyUrl;
    public static String reviewRatingUrl;
    public static String sellUrl;

    @Value("${buy.url}")
    public void setTransactionUrl(String url){
        buyUrl=url;
    }

    @Value("${review.url}")
    public void setReviewRatingUrl(String url){
        reviewRatingUrl=url;
    }

    @Value("${sell.url}")
    public void setSellUrl(String url) {sellUrl=url;}

    @Autowired
    public void PurchaseRequestService(PurchaseRequestRepository purchaseRepository) {
        this.requestRepository = purchaseRepository;
    }

    @Override
    public PurchaseRequest updateStatus(PurchaseRequest request, String verdict,String token) {
        RestTemplate restTemplate= new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("status", verdict);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(String.format("%s/transaction/%s",buyUrl,request.getTransactionId()),
                HttpMethod.PUT, httpEntity, String.class);

        System.out.println(response.getBody());
        this.requestRepository.deleteById(request.getTransactionId());
        return  request;
    }

    @Override
    public Collection<PurchaseRequest> collectRequest(String token) throws Exception {
        RestTemplate restTemplate= new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
        ResponseEntity<String> response = restTemplate.exchange(buyUrl+"/transaction", HttpMethod.GET, httpEntity, String.class);

        String responseBody = response.getBody();
        JSONArray transactionResponse =new JSONArray(responseBody);



        try {
            for (int i=0; i<transactionResponse.length(); i++){
                JSONObject process = transactionResponse.getJSONObject(i);
                PurchaseRequest tempPurchaseRequest = objectMapper.readValue(process.toString(), PurchaseRequest.class);
                this.requestRepository.save(tempPurchaseRequest);
            }
            return requestRepository.findAll();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Internal Error");
        }

        return null;
    }

    @Override
    public void allowToReview(PurchaseRequest p, String verdict, String token) {
        if(!verdict.equals("SUCCESS")){
            return;
        }
        RestTemplate restTemplate= new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<String> listingIds = new ArrayList<>(p.getListings().keySet());

        // Prepare the request body as a Map
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("listingId", listingIds);
        requestBody.put("username", p.getUsername());

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        try {
            // Send the POST request
            ResponseEntity<Void> response = restTemplate.exchange(
                    reviewRatingUrl + "/review/allowUserToReview",
                    HttpMethod.POST,
                    requestEntity,
                    Void.class
            );
            System.out.println(response.getBody());
        } catch (Exception e) {
            System.err.println("Error occurred while sending allow to review request: " + e.getMessage());

        }
    }

    @Override
    public void createOrder(PurchaseRequest p, String verdict, String token) {
        if (!verdict.equals("SUCCESS")) {
            return;
        }
        /*
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<Map<String, Object>> listings = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : p.getListings().entrySet()) {
            String listingId = entry.getKey();
            Integer quantity = entry.getValue();

            // Fetch listing details
            String listingUrl = String.format(sellUrl +"/listing/get-by-id/%s", listingId);
            ResponseEntity<Map> listingResponse = restTemplate.getForEntity(listingUrl, Map.class);

            if (listingResponse.getStatusCode() == HttpStatus.OK && listingResponse.getBody() != null) {
                Map<String, Object> listingDetails = listingResponse.getBody();

                // Add listing to order
                Map<String, Object> listing = new HashMap<>();
                listing.put("id", listingId);
                listing.put("name", listingDetails.get("name"));
                listing.put("quantity", quantity);

                listings.add(listing);
            }
        }

        if (!listings.isEmpty()) {
            Map<String, Object> order = new HashMap<>();
            order.put("listings", listings);
            order.put("authorUsername", p.getUsername());

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(order, headers);
            ResponseEntity<String> response = restTemplate.postForEntity("http://ip/order/create", requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Order created successfully");
            } else {
                System.out.println("Failed to create order: " + response.getBody());
            }
        } */

    }
}
