package in.cd.main.controller;

import in.cd.main.entity.JobApplication;
import in.cd.main.entity.Job;
import in.cd.main.service.JobApplicationService;
import in.cd.main.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class JobApplicationController {
    
    private final JobApplicationService jobApplicationService;
    private final JobService jobService;
    
    @GetMapping("/")
    public String home(Model model) {
        List<Job> activeJobs = jobService.getJobsForFrontend();
        model.addAttribute("jobs", activeJobs);
        return "index";
    }
    
    @GetMapping("/apply")
    public String applyForm(Model model) {
        List<Job> activeJobs = jobService.getJobsForFrontend();
        model.addAttribute("availablePositions", activeJobs.stream().map(Job::getTitle).toList());
        model.addAttribute("jobApplication", new JobApplication());
        return "apply";
    }
    
    @PostMapping("/apply")
    public String submitApplication(@ModelAttribute JobApplication jobApplication,
                                   @RequestParam("resume") MultipartFile resume,
                                   RedirectAttributes redirectAttributes) {
        try {
            jobApplicationService.submitApplication(jobApplication, resume);
            redirectAttributes.addFlashAttribute("success", 
                "Your application has been submitted successfully! Check your email for confirmation.");
            return "redirect:/apply";
        } catch (Exception e) {
            log.error("Error submitting application", e);
            redirectAttributes.addFlashAttribute("error", 
                "Error submitting application: " + e.getMessage());
            return "redirect:/apply";
        }
    }
    

    
    @GetMapping("/admin/applications")
    public String adminApplications(Model model) {
        List<JobApplication> applications = jobApplicationService.getAllApplications();
        log.info("Found {} applications in database", applications.size());
        for (JobApplication app : applications) {
            log.info("Application: {} - {} - {}", app.getName(), app.getEmail(), app.getAppliedPosition());
            String path = app.getResumeFilePath();
            if (path != null) {
                String fileName = path.replaceAll("\\\\", "/");
                int lastSlash = fileName.lastIndexOf("/");
                if (lastSlash != -1) {
                    fileName = fileName.substring(lastSlash + 1);
                }
                app.setResumeStoredFileName(fileName);
            }
        }
        model.addAttribute("applications", applications);
        return "admin/applications";
    }
    
    @PostMapping("/admin/application/{id}/status")
    @ResponseBody
    public ResponseEntity<String> updateApplicationStatus(@PathVariable Long id, 
                                                            @RequestParam String status) {
        try {
            log.info("Updating application status - ID: {}, Status: {}", id, status);
            JobApplication updatedApplication = jobApplicationService.updateApplicationStatus(id, status);
            log.info("Status updated successfully for application: {}", updatedApplication.getEmail());
            return ResponseEntity.ok("Status updated successfully");
        } catch (Exception e) {
            log.error("Error updating application status - ID: {}, Status: {}", id, status, e);
            return ResponseEntity.badRequest().body("Error updating status: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/admin/application/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteApplication(@PathVariable Long id) {
        boolean deleted = jobApplicationService.deleteApplication(id);
        if (deleted) {
            return ResponseEntity.ok("Application deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("Application not found");
        }
    }
    
    @GetMapping("/admin/resume/{filename:.+}")
    public ResponseEntity<Resource> downloadResume(@PathVariable String filename) {
        try {
            Resource resource = ((in.cd.main.service.impl.JobApplicationServiceImpl) jobApplicationService)
                .loadResumeAsResource(filename);
            
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                    "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
    
    @GetMapping("/admin/test")
    public String testPage() {
        return "admin/test";
    }
    
    // Test endpoint for debugging
    @PostMapping("/admin/test-email/{id}")
    @ResponseBody
    public ResponseEntity<String> testEmail(@PathVariable Long id, @RequestParam String status) {
        try {
            log.info("Testing email for application ID: {} with status: {}", id, status);
            Optional<JobApplication> application = jobApplicationService.getApplicationById(id);
            if (application.isPresent()) {
                jobApplicationService.sendStatusUpdateEmail(application.get(), status);
                return ResponseEntity.ok("Test email sent successfully");
            } else {
                return ResponseEntity.badRequest().body("Application not found");
            }
        } catch (Exception e) {
            log.error("Error testing email", e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
} 