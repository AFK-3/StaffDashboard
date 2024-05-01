package id.ac.ui.cs.advprog.staffdashboard.service;

import id.ac.ui.cs.advprog.staffdashboard.model.PurchaseRequest;
import id.ac.ui.cs.advprog.staffdashboard.model.TopupRequest;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import java.util.List;
import java.util.UUID;

public interface StaffDashboardService<T> {
    T add(T request);
    Collection<T> findAll();
    T update(T request, String verdict);
    T findById(String id);

}
