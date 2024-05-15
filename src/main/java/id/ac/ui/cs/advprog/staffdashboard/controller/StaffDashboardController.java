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
import java.util.UUID;

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
                return ResponseEntity.status(500).body("Unauthorized");}

            topupService.collectRequest(token);
            TopupRequest findRequest= topupService.findById(topupId);

            if(findRequest!=null){
                TopupRequest updatedTopUp = topupService.updateStatusProcess(topupService.findById(topupId), Status, token);
                String topUpJson = objectMapper.writeValueAsString(updatedTopUp);
                return ResponseEntity.ok(topUpJson);}
            else{
                return ResponseEntity.status(500).body("Reuqst not found");
            }

        } catch(Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body("Failed to process payment requests.");
        }
    }

    @PostMapping("/updatePurchase/{purchaseId}/{Status}")
    public ResponseEntity<String> updatePurchaseRequest(@RequestHeader("Authorization") String token, @PathVariable String purchaseId, @PathVariable String Status) {
        try{

            //if(!purchaseService.authenticateStaff(token)){
               // return ResponseEntity.status(500).body("Unauthorized");}
            purchaseService.collectRequest(token);
            PurchaseRequest findRequest= purchaseService.findById(purchaseId);
            System.out.println(findRequest);
            if(findRequest!=null){
                PurchaseRequest updatedPurchase = purchaseService.updateStatusProcess(findRequest, Status, token);
                String topUpJson = objectMapper.writeValueAsString(updatedPurchase);
                return ResponseEntity.ok(topUpJson);}
            else{
                return ResponseEntity.status(500).body("Request not found");
            }
        } catch(Exception e) {
            e.printStackTrace();
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

    @DeleteMapping("/delete-all")
    public String deleteAllPurchaseRequests() {
        topupService.deleteAll();
        purchaseService.deleteAll();
        return "Deletion started";
    }


}


