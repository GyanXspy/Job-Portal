package in.cd.main.service;

import in.cd.main.entity.JobApplication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface JobApplicationService {
    
    JobApplication submitApplication(JobApplication application, MultipartFile resume);
    
    List<JobApplication> getAllApplications();
    
    Optional<JobApplication> getApplicationById(Long id);
    
    Optional<JobApplication> getApplicationByEmail(String email);
    
    List<JobApplication> getApplicationsByStatus(String status);
    
    List<JobApplication> getApplicationsByPosition(String position);
    
    JobApplication updateApplicationStatus(Long id, String status);
    
    void sendConfirmationEmail(JobApplication application);
    
    void sendStatusUpdateEmail(JobApplication application, String status);
    
    List<JobApplication> getPendingEmailConfirmations();
    
    boolean deleteApplication(Long id);
    
    List<String> getAvailablePositions();
} 