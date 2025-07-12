package in.cd.main.repository;

import in.cd.main.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    
    Optional<JobApplication> findByEmail(String email);
    
    List<JobApplication> findByStatus(String status);
    
    List<JobApplication> findByAppliedPosition(String position);
    
    List<JobApplication> findByEmailSent(boolean emailSent);
    
    boolean existsByEmail(String email);
} 