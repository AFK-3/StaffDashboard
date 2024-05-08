package id.ac.ui.cs.advprog.staffdashboard.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.staffdashboard.model.PurchaseRequest;
import id.ac.ui.cs.advprog.staffdashboard.model.TopupRequest;
import id.ac.ui.cs.advprog.staffdashboard.service.StaffDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/")
public class StaffDashboardController {
    private StaffDashboardService<TopupRequest> topupService;
    private  StaffDashboardService<PurchaseRequest> purchaseService;
    private ObjectMapper objectMapper;


    @Autowired
    public StaffDashboardController(StaffDashboardService<TopupRequest> topupService,StaffDashboardService<PurchaseRequest> purchaseService,
                                    ObjectMapper objectMapper){
        this.topupService=topupService;
        this.purchaseService=purchaseService;
        this.objectMapper=objectMapper;
    }
    @GetMapping("/")
    @ResponseBody
    public String staffDashboardPage(Model model) {
        return "<h1>Testing If Im stupid or not</h1>";
    }

    @PostMapping("/updateTopup/{topupId}/{Status}")
    public ResponseEntity<String> updateTopupRequest(@RequestHeader("Authorization") String token, @PathVariable String topupId, @PathVariable String Status) {
        try{

            String userRole = getRoleFromToken(token);


            if (!userRole.equals("STAFF")) {
                throw new Exception("Non STAFF can't Update Status for Requests");
            }

            updateCurrentTopUps(token);

            TopupRequest updatedTopUp = topupService.update(topupService.findById(topupId), Status);
            String topUpJson = objectMapper.writeValueAsString(updatedTopUp);
            topupService.deleteById(topupId);
            return ResponseEntity.ok(topUpJson);

        } catch(Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body("Failed to process payment requests.");

        }
    }

    @GetMapping("/get-from-payment")
    public ResponseEntity<String> getFromPayment(@RequestHeader("Authorization") String token) throws Exception {
        updateCurrentTopUps(token);
        Collection<TopupRequest> updatedTopUp = topupService.findAll();
        String topUpJson = objectMapper.writeValueAsString(updatedTopUp);
        return ResponseEntity.ok(topUpJson);
    }

    @GetMapping("/get-from-buy")
    public ResponseEntity<String> getFromBuy(@RequestHeader("Authorization") String token) throws Exception {
        updateCurrentPurchases(token);
        Collection<PurchaseRequest> updatedPurchase = purchaseService.findAll();
        String purchaseJson= objectMapper.writeValueAsString(updatedPurchase);
        return ResponseEntity.ok(purchaseJson);

    }


    private void updateCurrentTopUps(String token) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8082/payment-request/get-all", HttpMethod.GET, httpEntity, String.class);

        String responseBody = response.getBody();
        JSONObject outerObject= new JSONObject(responseBody);
        JSONArray paymentResponse= outerObject.getJSONArray("paymentsRequest");



        try {
            for (int i=0; i<paymentResponse.length(); i++){
                JSONObject process = paymentResponse.getJSONObject(i);
                TopupRequest tempTopUpRequest = objectMapper.readValue(process.toString(), TopupRequest.class);
                topupService.add(tempTopUpRequest);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Something went Wrong");
        }
    }

    private void updateCurrentPurchases(String token) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8083/transaction", HttpMethod.GET, httpEntity, String.class);

        String responseBody = response.getBody();
        JSONArray transactionResponse =new JSONArray(responseBody);



        try {
            for (int i=0; i<transactionResponse.length(); i++){
                JSONObject process = transactionResponse.getJSONObject(i);
                PurchaseRequest tempPurchaseRequest = objectMapper.readValue(process.toString(), PurchaseRequest.class);
                purchaseService.add(tempPurchaseRequest);
                System.out.println(tempPurchaseRequest.toString());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Something went Wrong");
        }
    }
    private String getUsernameFromToken(String token) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/user/get-username", HttpMethod.GET, httpEntity, String.class);

        return response.getBody();
    }

    private String getRoleFromToken(String token) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/user/get-role", HttpMethod.GET, httpEntity, String.class);

        return response.getBody();
    }

}


