package id.ac.ui.cs.advprog.staffdashboard.service;

import id.ac.ui.cs.advprog.staffdashboard.model.PurchaseRequest;
import id.ac.ui.cs.advprog.staffdashboard.model.TopupRequest;

import java.util.List;
import java.util.UUID;

public interface StaffDashboardService {
    public List<TopupRequest> findAllTopUps();
    public List<PurchaseRequest> findAllPurchases();
    public TopupRequest update(TopupRequest topupRequest, String verdict);
    public PurchaseRequest update(PurchaseRequest purchaseRequest, String verdict);
    public TopupRequest findTopUpById(UUID topupId);
    public PurchaseRequest findPurchaseById(UUID transactionId);
}
