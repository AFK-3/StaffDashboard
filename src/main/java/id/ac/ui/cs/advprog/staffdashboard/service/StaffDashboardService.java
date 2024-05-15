package id.ac.ui.cs.advprog.staffdashboard.service;

import id.ac.ui.cs.advprog.staffdashboard.model.PurchaseRequest;
import id.ac.ui.cs.advprog.staffdashboard.model.TopupRequest;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface StaffDashboardService<T> {
    T add(T request);
    Collection<T> findAll();
    T findById(String id);
    void deleteById(String id);
    CompletableFuture<Void> deleteAll();
    Boolean authenticateStaff(String token) throws Exception;
}
