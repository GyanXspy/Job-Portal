package in.cd.main.service;

import in.cd.main.entity.JobApplication;

public interface EmailService {
    
    /**
     * Send acceptance email with aptitude test and technical round information
     */
    void sendAcceptanceEmail(JobApplication application);
    
    /**
     * Send rejection email with sorry message
     */
    void sendRejectionEmail(JobApplication application);
    
    /**
     * Send pending status update email
     */
    void sendPendingEmail(JobApplication application);
    
    /**
     * Send reviewed status update email
     */
    void sendReviewedEmail(JobApplication application);
} 