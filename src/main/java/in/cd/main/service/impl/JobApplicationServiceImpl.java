package in.cd.main.service.impl;

import in.cd.main.entity.JobApplication;
import in.cd.main.repository.JobApplicationRepository;
import in.cd.main.service.EmailService;
import in.cd.main.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobApplicationServiceImpl implements JobApplicationService {
    
    private final JobApplicationRepository jobApplicationRepository;
    private final JavaMailSender mailSender;
    private final EmailService emailService;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    private static final String UPLOAD_DIR = "uploads/resumes/";
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("pdf", "doc", "docx");
    
    @Override
    public JobApplication submitApplication(JobApplication application, MultipartFile resume) {
        try {
            // Validate resume file
            if (resume == null || resume.isEmpty()) {
                throw new IllegalArgumentException("Resume file is required");
            }
            
            String originalFilename = resume.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            
            if (!ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
                throw new IllegalArgumentException("Only PDF, DOC, and DOCX files are allowed");
            }
            
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Generate unique filename
            String uniqueFilename = UUID.randomUUID().toString() + "." + fileExtension;
            Path filePath = uploadPath.resolve(uniqueFilename);
            
            // Save file
            Files.copy(resume.getInputStream(), filePath);
            
            // Set application properties
            application.setResumeFileName(originalFilename);
            application.setResumeFilePath(filePath.toString());
            application.setAppliedAt(LocalDateTime.now());
            application.setStatus("PENDING");
            application.setEmailSent(false);
            
            // Save application
            JobApplication savedApplication = jobApplicationRepository.save(application);
            
            // Send confirmation email
            sendConfirmationEmail(savedApplication);
            
            log.info("Job application submitted successfully for email: {}", application.getEmail());
            return savedApplication;
            
        } catch (IOException e) {
            log.error("Error saving resume file", e);
            throw new RuntimeException("Error saving resume file", e);
        }
    }
    
    @Override
    public List<JobApplication> getAllApplications() {
        return jobApplicationRepository.findAll();
    }
    
    @Override
    public Optional<JobApplication> getApplicationById(Long id) {
        return jobApplicationRepository.findById(id);
    }
    
    @Override
    public Optional<JobApplication> getApplicationByEmail(String email) {
        return jobApplicationRepository.findByEmail(email);
    }
    
    @Override
    public List<JobApplication> getApplicationsByStatus(String status) {
        return jobApplicationRepository.findByStatus(status);
    }
    
    @Override
    public List<JobApplication> getApplicationsByPosition(String position) {
        return jobApplicationRepository.findByAppliedPosition(position);
    }
    
    @Override
    public JobApplication updateApplicationStatus(Long id, String status) {
        Optional<JobApplication> optional = jobApplicationRepository.findById(id);
        if (optional.isPresent()) {
            JobApplication application = optional.get();
            String oldStatus = application.getStatus();
            application.setStatus(status);
            JobApplication savedApplication = jobApplicationRepository.save(application);
            
            // Send status update email if status changed
            if (!oldStatus.equals(status)) {
                sendStatusUpdateEmail(savedApplication, status);
            }
            
            return savedApplication;
        }
        throw new RuntimeException("Application not found with id: " + id);
    }
    
    @Override
    public void sendConfirmationEmail(JobApplication application) {
        try {
            // Check if email is configured
            if (fromEmail == null || fromEmail.equals("your-email@gmail.com")) {
                log.info("Email not configured, skipping confirmation email for: {}", application.getEmail());
                // Still mark as sent to avoid retry attempts
                application.setEmailSent(true);
                jobApplicationRepository.save(application);
                return;
            }
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(application.getEmail());
            message.setSubject("Application Received - Candlesty AI Trading Platform");
            
            String emailContent = String.format(
                "Dear %s,\n\n" +
                "Thank you for your interest in joining Candlesty's AI-powered trading platform team!\n\n" +
                "We have successfully received your application for the position of: %s\n\n" +
                "Application Details:\n" +
                "- Name: %s\n" +
                "- Email: %s\n" +
                "- Education: %s\n" +
                "- Experience Level: %s\n" +
                "- Applied Date: %s\n\n" +
                "Our HR team will review your application and get back to you within 5-7 business days.\n\n" +
                "If you have any questions, please don't hesitate to reach out to us.\n\n" +
                "Best regards,\n" +
                "Candlesty HR Team\n" +
                "AI-Powered Trading Solutions",
                application.getName(),
                application.getAppliedPosition(),
                application.getName(),
                application.getEmail(),
                application.getEducation(),
                application.getExperienceLevel(),
                application.getAppliedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm"))
            );
            
            message.setText(emailContent);
            mailSender.send(message);
            
            // Update email sent status
            application.setEmailSent(true);
            jobApplicationRepository.save(application);
            
            log.info("Confirmation email sent successfully to: {}", application.getEmail());
            
        } catch (Exception e) {
            log.error("Error sending confirmation email to: {}", application.getEmail(), e);
            // Don't throw exception, just log the error
            // Still mark as sent to avoid retry attempts
            application.setEmailSent(true);
            jobApplicationRepository.save(application);
        }
    }
    
    @Override
    public void sendStatusUpdateEmail(JobApplication application, String status) {
        try {
            switch (status.toUpperCase()) {
                case "ACCEPTED":
                    emailService.sendAcceptanceEmail(application);
                    break;
                case "REJECTED":
                    emailService.sendRejectionEmail(application);
                    break;
                case "PENDING":
                    emailService.sendPendingEmail(application);
                    break;
                case "REVIEWED":
                    emailService.sendReviewedEmail(application);
                    break;
                default:
                    log.warn("Unknown status: {}, no email sent", status);
            }
        } catch (Exception e) {
            log.error("Error sending status update email for application {} with status {}", 
                     application.getId(), status, e);
        }
    }
    
    @Override
    public List<JobApplication> getPendingEmailConfirmations() {
        return jobApplicationRepository.findByEmailSent(false);
    }
    
    @Override
    public boolean deleteApplication(Long id) {
        Optional<JobApplication> optional = jobApplicationRepository.findById(id);
        if (optional.isPresent()) {
            JobApplication application = optional.get();
            
            // Delete resume file
            try {
                Path filePath = Paths.get(application.getResumeFilePath());
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                log.error("Error deleting resume file", e);
            }
            
            jobApplicationRepository.delete(application);
            return true;
        }
        return false;
    }
    
    @Override
    public List<String> getAvailablePositions() {
        return Arrays.asList(
            "AI Trading Algorithm Developer",
            "Quantitative Analyst",
            "Machine Learning Engineer",
            "Full Stack Developer",
            "DevOps Engineer",
            "Data Scientist",
            "Product Manager",
            "UI/UX Designer",
            "Business Analyst",
            "Marketing Specialist"
        );
    }
    
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
    
    public Resource loadResumeAsResource(String filename) {
        try {
            Path file = Paths.get(UPLOAD_DIR).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
} 