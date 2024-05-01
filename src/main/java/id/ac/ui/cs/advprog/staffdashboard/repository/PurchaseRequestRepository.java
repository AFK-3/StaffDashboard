package id.ac.ui.cs.advprog.staffdashboard.repository;

import id.ac.ui.cs.advprog.staffdashboard.model.PurchaseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class PurchaseRequestRepository {

    private Map<String, PurchaseRequest> purchaseRequestMap = new HashMap<>();

    public PurchaseRequest put(PurchaseRequest pRequest) {
        purchaseRequestMap.put(pRequest.getTransactionId(), pRequest);
        return pRequest;
    }

    public PurchaseRequest findPurchaseById(String purchaseId) {
        return purchaseRequestMap.get(purchaseId);
    }

    public Collection<PurchaseRequest> findAllPurchases() {
        return purchaseRequestMap.values();
    }

    public PurchaseRequest deletePurchase(String purchaseId) {
        if (purchaseRequestMap.containsKey(purchaseId)) {
            return purchaseRequestMap.remove(purchaseId);
        } else {
            return null;
        }
    }
}
