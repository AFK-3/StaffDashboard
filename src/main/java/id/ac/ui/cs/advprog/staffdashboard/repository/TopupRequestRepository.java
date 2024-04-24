package id.ac.ui.cs.advprog.staffdashboard.repository;

import id.ac.ui.cs.advprog.staffdashboard.model.TopupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class TopupRequestRepository {

    private Map<String, TopupRequest> topupRequestMap = new HashMap<>();

    public TopupRequest put(TopupRequest tRequest) {
        topupRequestMap.put(tRequest.getId().toString(), tRequest);
        return tRequest;
    }

    public TopupRequest findTopupById(String topupId) {
        return topupRequestMap.get(topupId);
    }

    public Collection<TopupRequest> findAllTopUps() {
        return topupRequestMap.values();
    }

    public TopupRequest deleteTopup(String topupId) {
        if (topupRequestMap.containsKey(topupId)) {
            return topupRequestMap.remove(topupId);
        } else {
            return null;
        }
    }
}
