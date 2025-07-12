package in.cd.main.service.impl;

import in.cd.main.entity.JobApplication;
import in.cd.main.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    
    private final JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Override
    public void sendAcceptanceEmail(JobApplication application) {
        try {
            if (fromEmail == null || fromEmail.equals("your-email@gmail.com")) {
                log.info("Email not configured, skipping acceptance email for: {}", application.getEmail());
                return;
            }
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(application.getEmail());
            message.setSubject("Congratulations! Your Application Has Been Accepted - Candlesty");
            
            String emailContent = String.format(
                "Dear %s,\n\n" +
                "🎉 Congratulations! We are pleased to inform you that your application for the position of %s at Candlesty has been ACCEPTED!\n\n" +
                "Your application stood out among many others, and we believe you would be a great addition to our AI-powered trading platform team.\n\n" +
                "📋 Next Steps - Interview Process:\n\n" +
                "1. **Aptitude Test** (Online)\n" +
                "   - Duration: 60 minutes\n" +
                "   - Topics: Logical reasoning, Quantitative analysis, Problem solving\n" +
                "   - You will receive a separate email with test details within 24 hours\n\n" +
                "2. **Technical Round** (Video Call)\n" +
                "   - Duration: 45-60 minutes\n" +
                "   - Topics: Technical skills, Coding challenges, System design\n" +
                "   - Scheduled after successful aptitude test completion\n\n" +
                "3. **Final Interview** (In-person/Virtual)\n" +
                "   - Duration: 30-45 minutes\n" +
                "   - Topics: Culture fit, Career goals, Final questions\n\n" +
                "📧 Important Information:\n" +
                "- You will receive detailed instructions for each round via email\n" +
                "- Please check your email regularly and respond promptly\n" +
                "- If you have any questions, reply to this email\n\n" +
                "Application Details:\n" +
                "- Position: %s\n" +
                "- Applied Date: %s\n" +
                "- Experience Level: %s\n\n" +
                "We are excited to move forward with your application and look forward to meeting you!\n\n" +
                "Best regards,\n" +
                "Candlesty HR Team\n" +
                "AI-Powered Trading Solutions\n" +
                "Email: hr@candlesty.com\n" +
                "Phone: +1 (555) 123-4567",
                application.getName(),
                application.getAppliedPosition(),
                application.getAppliedPosition(),
                application.getAppliedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                application.getExperienceLevel()
            );
            
            message.setText(emailContent);
            mailSender.send(message);
            
            log.info("Acceptance email sent successfully to: {}", application.getEmail());
            
        } catch (Exception e) {
            log.error("Error sending acceptance email to: {}", application.getEmail(), e);
        }
    }
    
    @Override
    public void sendRejectionEmail(JobApplication application) {
        try {
            if (fromEmail == null || fromEmail.equals("your-email@gmail.com")) {
                log.info("Email not configured, skipping rejection email for: {}", application.getEmail());
                return;
            }
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(application.getEmail());
            message.setSubject("Application Status Update - Candlesty");
            
            String emailContent = String.format(
                "Dear %s,\n\n" +
                "Thank you for your interest in joining the Candlesty team and for taking the time to apply for the %s position.\n\n" +
                "After careful consideration of your application, we regret to inform you that we are unable to move forward with your application at this time.\n\n" +
                "Please know that this decision was not an easy one, and we received many qualified applications for this position. While your background and experience are impressive, we have decided to pursue other candidates whose qualifications more closely match our current needs.\n\n" +
                "We appreciate the time and effort you invested in your application and encourage you to:\n" +
                "• Keep an eye on our careers page for future opportunities\n" +
                "• Follow us on LinkedIn for company updates\n" +
                "• Consider applying again in the future as your experience grows\n\n" +
                "We wish you the very best in your future endeavors and hope our paths may cross again.\n\n" +
                "Application Details:\n" +
                "- Position: %s\n" +
                "- Applied Date: %s\n" +
                "- Experience Level: %s\n\n" +
                "Thank you again for your interest in Candlesty.\n\n" +
                "Best regards,\n" +
                "Candlesty HR Team\n" +
                "AI-Powered Trading Solutions\n" +
                "Email: hr@candlesty.com\n" +
                "Website: https://candlesty.com",
                application.getName(),
                application.getAppliedPosition(),
                application.getAppliedPosition(),
                application.getAppliedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                application.getExperienceLevel()
            );
            
            message.setText(emailContent);
            mailSender.send(message);
            
            log.info("Rejection email sent successfully to: {}", application.getEmail());
            
        } catch (Exception e) {
            log.error("Error sending rejection email to: {}", application.getEmail(), e);
        }
    }
    
    @Override
    public void sendPendingEmail(JobApplication application) {
        try {
            if (fromEmail == null || fromEmail.equals("your-email@gmail.com")) {
                log.info("Email not configured, skipping pending email for: {}", application.getEmail());
                return;
            }
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(application.getEmail());
            message.setSubject("Application Status Update - Under Review - Candlesty");
            
            String emailContent = String.format(
                "Dear %s,\n\n" +
                "Thank you for your application for the %s position at Candlesty.\n\n" +
                "We wanted to let you know that your application is currently under review by our HR team. We are carefully evaluating all applications to ensure we find the best fit for our AI-powered trading platform.\n\n" +
                "📋 What happens next:\n" +
                "• Our team will review your application thoroughly\n" +
                "• You will receive an update within 5-7 business days\n" +
                "• If selected, you'll be invited for the next round of interviews\n\n" +
                "Application Details:\n" +
                "- Position: %s\n" +
                "- Applied Date: %s\n" +
                "- Experience Level: %s\n" +
                "- Status: Under Review\n\n" +
                "We appreciate your patience during this process. If you have any questions, please don't hesitate to reach out to us.\n\n" +
                "Best regards,\n" +
                "Candlesty HR Team\n" +
                "AI-Powered Trading Solutions\n" +
                "Email: hr@candlesty.com",
                application.getName(),
                application.getAppliedPosition(),
                application.getAppliedPosition(),
                application.getAppliedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                application.getExperienceLevel()
            );
            
            message.setText(emailContent);
            mailSender.send(message);
            
            log.info("Pending status email sent successfully to: {}", application.getEmail());
            
        } catch (Exception e) {
            log.error("Error sending pending email to: {}", application.getEmail(), e);
        }
    }
    
    @Override
    public void sendReviewedEmail(JobApplication application) {
        try {
            if (fromEmail == null || fromEmail.equals("your-email@gmail.com")) {
                log.info("Email not configured, skipping reviewed email for: {}", application.getEmail());
                return;
            }
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(application.getEmail());
            message.setSubject("Application Reviewed - Next Steps - Candlesty");
            
            String emailContent = String.format(
                "Dear %s,\n\n" +
                "Thank you for your application for the %s position at Candlesty.\n\n" +
                "We are pleased to inform you that your application has been reviewed by our HR team. We were impressed by your background and experience.\n\n" +
                "📋 Current Status:\n" +
                "Your application is currently being evaluated by our technical team for the next phase of the hiring process.\n\n" +
                "⏰ What to expect:\n" +
                "• You will receive a decision within 2-3 business days\n" +
                "• If selected, you'll be invited for technical interviews\n" +
                "• We'll keep you updated throughout the process\n\n" +
                "Application Details:\n" +
                "- Position: %s\n" +
                "- Applied Date: %s\n" +
                "- Experience Level: %s\n" +
                "- Status: Reviewed\n\n" +
                "We appreciate your interest in joining our AI-powered trading platform team and will be in touch soon!\n\n" +
                "Best regards,\n" +
                "Candlesty HR Team\n" +
                "AI-Powered Trading Solutions\n" +
                "Email: hr@candlesty.com",
                application.getName(),
                application.getAppliedPosition(),
                application.getAppliedPosition(),
                application.getAppliedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                application.getExperienceLevel()
            );
            
            message.setText(emailContent);
            mailSender.send(message);
            
            log.info("Reviewed status email sent successfully to: {}", application.getEmail());
            
        } catch (Exception e) {
            log.error("Error sending reviewed email to: {}", application.getEmail(), e);
        }
    }
} 