package id.ac.ui.cs.advprog.staffdashboard.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.staffdashboard.model.PurchaseRequest;
import id.ac.ui.cs.advprog.staffdashboard.repository.PurchaseRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@Service
public class PurchaseRequestServiceImpl extends RequestServiceImpl<PurchaseRequest> {

    private final String buyUrl = "http://localhost:8083";
    private  final String reviewRatingUrl= "http://localhost:8084";

    @Autowired
    public void PurchaseRequestService(PurchaseRequestRepository purchaseRepository) {
        this.requestRepository = purchaseRepository;
    }

    @Override
    public PurchaseRequest updateStatus(PurchaseRequest request, String verdict,String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("status", verdict);

        ResponseEntity<String> response = restTemplate.exchange(String.format("%s/transaction/%s",buyUrl,request.getTransactionId().toString()),
                HttpMethod.PATCH, httpEntity, String.class,params);

        System.out.println(response.getBody());
        requestRepository.delete(request);
        return  request;
    }

    @Override
    public Collection<PurchaseRequest> collectRequest(String token) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
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
                requestRepository.save(tempPurchaseRequest);
            }
            return requestRepository.findAll();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Internal Error");
        }

        return null;
    }

}
