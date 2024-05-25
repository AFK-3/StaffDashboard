package id.ac.ui.cs.advprog.staffdashboard.service;

import java.util.Collection;

import java.util.concurrent.CompletableFuture;

public interface StaffDashboardService<T> {
    T add(T request);
    Collection<T> findAll();
    T findById(String id);
    CompletableFuture<Void> deleteById(String id);
    CompletableFuture<Void> deleteAll();
    Boolean authenticateStaff(String token) throws Exception;
}
