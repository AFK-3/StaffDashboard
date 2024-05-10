package id.ac.ui.cs.advprog.staffdashboard.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.staffdashboard.model.PurchaseRequest;
import id.ac.ui.cs.advprog.staffdashboard.model.TopupRequest;
import id.ac.ui.cs.advprog.staffdashboard.repository.TopupRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@Service
public class TopUpRequestServiceImpl extends RequestServiceImpl<TopupRequest> {

    private final String paymentUrl = "http://localhost:8082";

    @Autowired
    public void topUpRequestService(TopupRequestRepository topUpRepository) {
        this.requestRepository = topUpRepository;
    }

    @Override
    public TopupRequest updateStatus(TopupRequest request, String verdict) {
        request.setPaymentStatus(verdict);
        return requestRepository.save(request);
    }

    @Override
    public Collection<TopupRequest> collectRequest(String token) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
        ResponseEntity<String> response = restTemplate.exchange(paymentUrl+"/payment-request/get-all", HttpMethod.GET, httpEntity, String.class);

        String responseBody = response.getBody();
        JSONObject outerObject= new JSONObject(responseBody);
        JSONArray paymentResponse= outerObject.getJSONArray("paymentsRequest");



        try {
            for (int i=0; i<paymentResponse.length(); i++){
                JSONObject process = paymentResponse.getJSONObject(i);
                TopupRequest tempTopUpRequest = objectMapper.readValue(process.toString(), TopupRequest.class);
                requestRepository.save(tempTopUpRequest);
            }

            return this.requestRepository.findAll();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Internal Error");
        }
        return null;
    }
}