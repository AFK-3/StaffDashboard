package id.ac.ui.cs.advprog.staffdashboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.staffdashboard.model.PurchaseRequest;
import id.ac.ui.cs.advprog.staffdashboard.model.TopupRequest;
import id.ac.ui.cs.advprog.staffdashboard.service.RequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StaffDashboardController.class)
public class StaffDashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestServiceImpl<TopupRequest> topupService;

    @MockBean
    private RequestServiceImpl<PurchaseRequest> purchaseService;

    @Autowired
    private ObjectMapper objectMapper;

    private TopupRequest topupRequest;
    private PurchaseRequest purchaseRequest;

    @BeforeEach
    void setUp() {
        topupRequest = new TopupRequest();
        topupRequest.setId("topup123");

        purchaseRequest = new PurchaseRequest();
        purchaseRequest.setTransactionId("purchase123");
    }

    @Test
    void testStaffDashboardPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("<h1>Nothing Here Yet</h1>"));
    }

    @Test
    void testUpdateTopupRequest() throws Exception {
        Mockito.when(topupService.authenticateStaff(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(topupService.findById("topup123")).thenReturn(topupRequest);
        Mockito.when(topupService.updateStatusProcess(ArgumentMatchers.any(TopupRequest.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(topupRequest);

        mockMvc.perform(post("/updateTopup/topup123/ACCEPTED")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateTopupRequestFailAuthenticate() throws Exception {
        Mockito.when(topupService.authenticateStaff(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(topupService.findById("topup123")).thenReturn(topupRequest);
        Mockito.when(topupService.updateStatusProcess(ArgumentMatchers.any(TopupRequest.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(topupRequest);

        mockMvc.perform(post("/updateTopup/topup123/ACCEPTED")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdatePurchaseRequest() throws Exception {
        Mockito.when(purchaseService.authenticateStaff(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(purchaseService.findById("purchase123")).thenReturn(purchaseRequest);
        Mockito.when(purchaseService.updateStatusProcess(ArgumentMatchers.any(PurchaseRequest.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(purchaseRequest);

        mockMvc.perform(post("/updatePurchase/purchase123/SUCCESS")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdatePurchaseRequestFailAuthenticate() throws Exception {
        Mockito.when(purchaseService.authenticateStaff(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(purchaseService.findById("purchase123")).thenReturn(purchaseRequest);
        Mockito.when(purchaseService.updateStatusProcess(ArgumentMatchers.any(PurchaseRequest.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(purchaseRequest);

        mockMvc.perform(post("/updatePurchase/purchase123/SUCCESS")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetFromPayment() throws Exception {
        Mockito.when(topupService.authenticateStaff(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(topupService.collectRequest(ArgumentMatchers.anyString())).thenReturn(Collections.singletonList(topupRequest));

        mockMvc.perform(get("/get-from-payment")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk());

    }

    @Test
    void testGetFromBuy() throws Exception {
        Mockito.when(purchaseService.authenticateStaff(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(purchaseService.collectRequest(ArgumentMatchers.anyString())).thenReturn(Collections.singletonList(purchaseRequest));

        mockMvc.perform(get("/get-from-buy")
                        .header("Authorization", "Bearer token"));

    }

    @Test
    void testDeleteAllPurchaseRequests() throws Exception {
        mockMvc.perform(delete("/delete-all"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deletion started"));

        Mockito.verify(topupService, Mockito.times(1)).deleteAll();
        Mockito.verify(purchaseService, Mockito.times(1)).deleteAll();
    }
}
