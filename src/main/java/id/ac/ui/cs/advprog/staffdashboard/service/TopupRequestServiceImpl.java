package id.ac.ui.cs.advprog.staffdashboard.service;

import id.ac.ui.cs.advprog.staffdashboard.model.PurchaseRequest;
import id.ac.ui.cs.advprog.staffdashboard.model.TopupRequest;
import id.ac.ui.cs.advprog.staffdashboard.model.Enum.RequestStatus;
import id.ac.ui.cs.advprog.staffdashboard.repository.TopupRequestRepository;
import id.ac.ui.cs.advprog.staffdashboard.service.StaffDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class TopupRequestServiceImpl implements StaffDashboardService<TopupRequest> {

    @Autowired
    private TopupRequestRepository topupRequestRepository;

    @Override
    public TopupRequest add(TopupRequest request) {
        return topupRequestRepository.put(request);
    }
    @Override
    public Collection<TopupRequest> findAll() {
        return topupRequestRepository.findAllTopUps();
    }

    @Override
    public TopupRequest update(TopupRequest request, String verdict) {
        request.setPaymentStatus(verdict);
        return topupRequestRepository.put(request);
    }

    @Override
    public TopupRequest findById(String id) {
        return topupRequestRepository.findTopupById(id);
    }

    @Override
    public void deleteById(String id) {
        topupRequestRepository.deleteTopup(id);
    }
}
