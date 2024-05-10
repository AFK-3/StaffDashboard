package id.ac.ui.cs.advprog.staffdashboard.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.staffdashboard.model.PurchaseRequest;
import id.ac.ui.cs.advprog.staffdashboard.model.TopupRequest;
import id.ac.ui.cs.advprog.staffdashboard.service.RequestServiceImpl;
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
    private RequestServiceImpl<TopupRequest> topupService;
    private  RequestServiceImpl<PurchaseRequest> purchaseService;
    private ObjectMapper objectMapper;


    @Autowired
    public StaffDashboardController(RequestServiceImpl<TopupRequest> topupService,RequestServiceImpl<PurchaseRequest> purchaseService,
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

            if(!topupService.authenticateStaff(token)){
                return ResponseEntity.status(500).body("Failed to process payment requests.");
            }



            TopupRequest updatedTopUp = topupService.updateStatus(topupService.findById(topupId), Status);
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
        Collection<TopupRequest> updatedTopUp = topupService.collectRequest(token);
        String topUpJson = objectMapper.writeValueAsString(updatedTopUp);
        return ResponseEntity.ok(topUpJson);
    }

    @GetMapping("/get-from-buy")
    public ResponseEntity<String> getFromBuy(@RequestHeader("Authorization") String token) throws Exception {
        Collection<PurchaseRequest> updatedPurchase = purchaseService.collectRequest(token);
        String purchaseJson= objectMapper.writeValueAsString(updatedPurchase);
        return ResponseEntity.ok(purchaseJson);

    }










}


