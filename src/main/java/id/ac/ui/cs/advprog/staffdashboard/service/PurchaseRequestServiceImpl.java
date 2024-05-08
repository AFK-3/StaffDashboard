package id.ac.ui.cs.advprog.staffdashboard.service;

import id.ac.ui.cs.advprog.staffdashboard.model.PurchaseRequest;
import id.ac.ui.cs.advprog.staffdashboard.repository.PurchaseRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class PurchaseRequestServiceImpl implements StaffDashboardService<PurchaseRequest> {

    @Autowired
    private PurchaseRequestRepository purchaseRequestRepository;

    @Override
    public PurchaseRequest add(PurchaseRequest request) {
        return purchaseRequestRepository.save(request);
    }
    @Override
    public Collection<PurchaseRequest> findAll() {
        return purchaseRequestRepository.findAll();
    }

    @Override
    public PurchaseRequest update(PurchaseRequest request, String verdict) {
        request.setStatus(verdict);
        return purchaseRequestRepository.save(request);
    }

    @Override
    public PurchaseRequest findById(String id) {

        return purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Purchase request not found with id: " + id));
    }


    @Override
    public void deleteById(String id){
        purchaseRequestRepository.deleteById(id);
    }
}
