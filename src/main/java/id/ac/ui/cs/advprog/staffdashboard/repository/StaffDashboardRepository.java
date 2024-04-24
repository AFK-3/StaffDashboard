package id.ac.ui.cs.advprog.staffdashboard.service;

import id.ac.ui.cs.advprog.staffdashboard.model.PurchaseRequest;
import id.ac.ui.cs.advprog.staffdashboard.model.TopupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class StaffDashboardRepository {
    private Map<String, TopupRequest> topupRequestMap = new HashMap<>();
    private Map<String, PurchaseRequest> purchaseRequestMap = new HashMap<>();

    public TopupRequest put(TopupRequest tRequest) {
        topupRequestMap.put(tRequest.getId().toString(), tRequest);
        return tRequest;
    }
    public PurchaseRequest put(PurchaseRequest pRequest) {
        purchaseRequestMap.put(pRequest.getTransactionId().toString(), pRequest);
        return pRequest;
    }

    public TopupRequest findTopupById(String topupId){
        return topupRequestMap.get(topupId);
    }
    public PurchaseRequest findPurchaseById(String purchaseId){
        return purchaseRequestMap.get(purchaseId);
    }

    public Collection<TopupRequest> findAllTopUps(){
        return topupRequestMap.values();
    }

    public Collection<PurchaseRequest> findAllPurchases(){
        return purchaseRequestMap.values();
    }

    public TopupRequest deleteTopup(String topupId) {
        if (topupRequestMap.containsKey(topupId)) {
            return topupRequestMap.remove(topupId);
        }
        else {
            return null;
        }
    }

    public PurchaseRequest deletePurchase(String purchaseId) {
        if (purchaseRequestMap.containsKey(purchaseId)) {
            return purchaseRequestMap.remove(purchaseId);
        }
        else {
            return null;
        }
    }

}
