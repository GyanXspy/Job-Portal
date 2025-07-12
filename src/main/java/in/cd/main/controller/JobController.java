package in.cd.main.controller;

import in.cd.main.entity.Job;
import in.cd.main.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class JobController {
    
    private final JobService jobService;
    
    // Public endpoints for job portal
    @GetMapping("/jobs")
    public String jobsPage(Model model, 
                          @RequestParam(required = false) String search,
                          @RequestParam(required = false) String department,
                          @RequestParam(required = false) String location,
                          @RequestParam(required = false) String employmentType,
                          @RequestParam(required = false) String experienceLevel) {
        
        List<Job> jobs;
        if (search != null && !search.trim().isEmpty()) {
            jobs = jobService.searchJobs(search);
        } else if (department != null || location != null || employmentType != null || experienceLevel != null) {
            jobs = jobService.filterJobs(department, location, employmentType, experienceLevel);
        } else {
            jobs = jobService.getJobsForFrontend();
        }
        
        model.addAttribute("jobs", jobs);
        model.addAttribute("departments", jobService.getDepartments());
        model.addAttribute("locations", jobService.getLocations());
        model.addAttribute("employmentTypes", jobService.getEmploymentTypes());
        model.addAttribute("experienceLevels", jobService.getExperienceLevels());
        model.addAttribute("search", search);
        model.addAttribute("selectedDepartment", department);
        model.addAttribute("selectedLocation", location);
        model.addAttribute("selectedEmploymentType", employmentType);
        model.addAttribute("selectedExperienceLevel", experienceLevel);
        
        return "jobs";
    }
    
    @GetMapping("/job/{id}")
    public String jobDetail(@PathVariable Long id, Model model) {
        return jobService.getJobById(id)
                .map(job -> {
                    model.addAttribute("job", job);
                    return "job-detail";
                })
                .orElse("redirect:/jobs");
    }
    
    // Admin endpoints for job management
    @GetMapping("/admin/jobs")
    public String adminJobs(Model model) {
        List<Job> jobs = jobService.getAllJobs();
        model.addAttribute("jobs", jobs);
        return "admin/jobs";
    }
    
    @GetMapping("/admin/jobs/create")
    public String createJobForm(Model model) {
        model.addAttribute("job", new Job());
        model.addAttribute("departments", getDefaultDepartments());
        model.addAttribute("employmentTypes", getDefaultEmploymentTypes());
        model.addAttribute("experienceLevels", getDefaultExperienceLevels());
        return "admin/job-form";
    }
    
    @PostMapping("/admin/jobs/create")
    public String createJob(@ModelAttribute Job job, RedirectAttributes redirectAttributes) {
        try {
            job.setCreatedBy("Admin"); // In a real app, get from security context
            jobService.createJob(job);
            redirectAttributes.addFlashAttribute("success", "Job created successfully!");
            return "redirect:/admin/jobs";
        } catch (Exception e) {
            log.error("Error creating job", e);
            redirectAttributes.addFlashAttribute("error", "Error creating job: " + e.getMessage());
            return "redirect:/admin/jobs/create";
        }
    }
    
    @GetMapping("/admin/jobs/{id}/edit")
    public String editJobForm(@PathVariable Long id, Model model) {
        return jobService.getJobById(id)
                .map(job -> {
                    model.addAttribute("job", job);
                    model.addAttribute("departments", getDefaultDepartments());
                    model.addAttribute("employmentTypes", getDefaultEmploymentTypes());
                    model.addAttribute("experienceLevels", getDefaultExperienceLevels());
                    return "admin/job-form";
                })
                .orElse("redirect:/admin/jobs");
    }
    
    @PostMapping("/admin/jobs/{id}/edit")
    public String updateJob(@PathVariable Long id, @ModelAttribute Job job, RedirectAttributes redirectAttributes) {
        try {
            jobService.updateJob(id, job);
            redirectAttributes.addFlashAttribute("success", "Job updated successfully!");
            return "redirect:/admin/jobs";
        } catch (Exception e) {
            log.error("Error updating job", e);
            redirectAttributes.addFlashAttribute("error", "Error updating job: " + e.getMessage());
            return "redirect:/admin/jobs/" + id + "/edit";
        }
    }
    
    @PostMapping("/admin/jobs/{id}/toggle-status")
    @ResponseBody
    public String toggleJobStatus(@PathVariable Long id) {
        boolean success = jobService.toggleJobStatus(id);
        return success ? "Status updated successfully" : "Error updating status";
    }
    
    @DeleteMapping("/admin/jobs/{id}")
    @ResponseBody
    public String deleteJob(@PathVariable Long id) {
        boolean deleted = jobService.deleteJob(id);
        return deleted ? "Job deleted successfully" : "Error deleting job";
    }
    
    // Helper methods for form options
    private List<String> getDefaultDepartments() {
        return List.of(
            "Engineering",
            "Data Science",
            "Product Management",
            "Design",
            "Marketing",
            "Sales",
            "Operations",
            "Finance",
            "Human Resources",
            "Legal"
        );
    }
    
    private List<String> getDefaultEmploymentTypes() {
        return List.of("FULL_TIME", "PART_TIME", "CONTRACT", "INTERNSHIP");
    }
    
    private List<String> getDefaultExperienceLevels() {
        return List.of("ENTRY", "MID", "SENIOR", "LEAD");
    }
} 