package in.cd.main.service.impl;

import in.cd.main.entity.Job;
import in.cd.main.repository.JobRepository;
import in.cd.main.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobServiceImpl implements JobService {
    
    private final JobRepository jobRepository;
    
    @Override
    public Job createJob(Job job) {
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());
        job.setStatus("ACTIVE");
        return jobRepository.save(job);
    }
    
    @Override
    public Job updateJob(Long id, Job job) {
        Optional<Job> existingJob = jobRepository.findById(id);
        if (existingJob.isPresent()) {
            Job updatedJob = existingJob.get();
            updatedJob.setTitle(job.getTitle());
            updatedJob.setDescription(job.getDescription());
            updatedJob.setRequirements(job.getRequirements());
            updatedJob.setResponsibilities(job.getResponsibilities());
            updatedJob.setLocation(job.getLocation());
            updatedJob.setEmploymentType(job.getEmploymentType());
            updatedJob.setExperienceLevel(job.getExperienceLevel());
            updatedJob.setDepartment(job.getDepartment());
            updatedJob.setOpenings(job.getOpenings());
            updatedJob.setSalaryRange(job.getSalaryRange());
            updatedJob.setBenefits(job.getBenefits());
            updatedJob.setApplicationDeadline(job.getApplicationDeadline());
            updatedJob.setAdditionalInfo(job.getAdditionalInfo());
            updatedJob.setUpdatedAt(LocalDateTime.now());
            return jobRepository.save(updatedJob);
        }
        throw new RuntimeException("Job not found with id: " + id);
    }
    
    @Override
    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }
    
    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
    
    @Override
    public List<Job> getActiveJobs() {
        return jobRepository.findByStatusOrderByCreatedAtDesc("ACTIVE");
    }
    
    @Override
    public List<Job> getJobsByStatus(String status) {
        return jobRepository.findByStatus(status);
    }
    
    @Override
    public List<Job> getJobsByDepartment(String department) {
        return jobRepository.findByDepartment(department);
    }
    
    @Override
    public List<Job> getJobsByEmploymentType(String employmentType) {
        return jobRepository.findByEmploymentType(employmentType);
    }
    
    @Override
    public List<Job> getJobsByExperienceLevel(String experienceLevel) {
        return jobRepository.findByExperienceLevel(experienceLevel);
    }
    
    @Override
    public List<Job> searchJobs(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getActiveJobs();
        }
        
        List<Job> titleResults = jobRepository.findByTitleContainingIgnoreCase(keyword);
        List<Job> locationResults = jobRepository.findByLocationContainingIgnoreCase(keyword);
        
        // Combine and remove duplicates
        titleResults.addAll(locationResults);
        return titleResults.stream()
                .distinct()
                .filter(job -> "ACTIVE".equals(job.getStatus()))
                .toList();
    }
    
    @Override
    public List<Job> filterJobs(String department, String location, String employmentType, String experienceLevel) {
        List<Job> activeJobs = getActiveJobs();
        
        return activeJobs.stream()
                .filter(job -> department == null || department.isEmpty() || department.equals(job.getDepartment()))
                .filter(job -> location == null || location.isEmpty() || job.getLocation().toLowerCase().contains(location.toLowerCase()))
                .filter(job -> employmentType == null || employmentType.isEmpty() || employmentType.equals(job.getEmploymentType()))
                .filter(job -> experienceLevel == null || experienceLevel.isEmpty() || experienceLevel.equals(job.getExperienceLevel()))
                .toList();
    }
    
    @Override
    public boolean deleteJob(Long id) {
        Optional<Job> job = jobRepository.findById(id);
        if (job.isPresent()) {
            jobRepository.delete(job.get());
            return true;
        }
        return false;
    }
    
    @Override
    public boolean toggleJobStatus(Long id) {
        Optional<Job> job = jobRepository.findById(id);
        if (job.isPresent()) {
            Job updatedJob = job.get();
            String newStatus = "ACTIVE".equals(updatedJob.getStatus()) ? "INACTIVE" : "ACTIVE";
            updatedJob.setStatus(newStatus);
            updatedJob.setUpdatedAt(LocalDateTime.now());
            jobRepository.save(updatedJob);
            return true;
        }
        return false;
    }
    
    @Override
    public List<String> getDepartments() {
        return jobRepository.findDistinctActiveDepartments();
    }
    
    @Override
    public List<String> getLocations() {
        return jobRepository.findDistinctActiveLocations();
    }
    
    @Override
    public List<String> getEmploymentTypes() {
        return jobRepository.findDistinctActiveEmploymentTypes();
    }
    
    @Override
    public List<String> getExperienceLevels() {
        return jobRepository.findDistinctActiveExperienceLevels();
    }
    
    @Override
    public List<Job> getJobsForFrontend() {
        return jobRepository.findActiveJobsWithValidDeadline(LocalDateTime.now());
    }
} 