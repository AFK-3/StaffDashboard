package id.ac.ui.cs.advprog.staffdashboard.repository;

import id.ac.ui.cs.advprog.staffdashboard.model.TopupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public interface TopupRequestRepository extends JpaRepository<TopupRequest,String> {

}
