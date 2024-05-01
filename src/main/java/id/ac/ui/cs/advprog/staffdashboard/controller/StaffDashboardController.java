package id.ac.ui.cs.advprog.staffdashboard.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.staffdashboard.model.PurchaseRequest;
import id.ac.ui.cs.advprog.staffdashboard.model.TopupRequest;
import id.ac.ui.cs.advprog.staffdashboard.service.StaffDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
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

            HashMap<String, String> user = getUserFromAuth(token);
            if (!user.get("type").equals("STAFF")) {
                throw new Exception("Non STAFF can't Update Status for Requests");
            }

            updateCurrentTopUps(token);

            TopupRequest updatedTopUp = topupService.update(topupService.findById(topupId), Status);
            String topUpJson = objectMapper.writeValueAsString(updatedTopUp);
            return ResponseEntity.ok(topUpJson);

        } catch(Exception e) {
            return ResponseEntity.status(500).body("Failed to process payment requests.");

        }
    }

    @GetMapping("/get-from-payment")
    public ResponseEntity<String> testGet(@RequestHeader("Authorization") String token) throws Exception {
        updateCurrentTopUps(token);
        Collection<TopupRequest> updatedTopUp = topupService.findAll();
        String topUpJson = objectMapper.writeValueAsString(updatedTopUp);
        return ResponseEntity.ok(topUpJson);
    }

    private void updateCurrentTopUps(String token) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8082/payment-request/get-all", HttpMethod.GET, httpEntity, String.class);

        String responseBody = response.getBody();

        try {
            List<TopupRequest> topupRequestList = objectMapper.readValue(
                    responseBody,
                    new TypeReference<List<TopupRequest>>() {}
            );
            for (TopupRequest tprq : topupRequestList ) {
                topupService.add(tprq);
                System.out.println(tprq);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Something went Wrong");
        }
    }
    private HashMap<String, String> getUserFromAuth(String token) throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/api/auth/get-user", HttpMethod.GET, httpEntity, String.class);


        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.getBody(), new TypeReference<HashMap<String, String>>() {});
    }

}


