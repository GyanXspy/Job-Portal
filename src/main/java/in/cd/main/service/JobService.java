package in.cd.main.service;

import in.cd.main.entity.Job;

import java.util.List;
import java.util.Optional;

public interface JobService {
    
    Job createJob(Job job);
    
    Job updateJob(Long id, Job job);
    
    Optional<Job> getJobById(Long id);
    
    List<Job> getAllJobs();
    
    List<Job> getActiveJobs();
    
    List<Job> getJobsByStatus(String status);
    
    List<Job> getJobsByDepartment(String department);
    
    List<Job> getJobsByEmploymentType(String employmentType);
    
    List<Job> getJobsByExperienceLevel(String experienceLevel);
    
    List<Job> searchJobs(String keyword);
    
    List<Job> filterJobs(String department, String location, String employmentType, String experienceLevel);
    
    boolean deleteJob(Long id);
    
    boolean toggleJobStatus(Long id);
    
    List<String> getDepartments();
    
    List<String> getLocations();
    
    List<String> getEmploymentTypes();
    
    List<String> getExperienceLevels();
    
    List<Job> getJobsForFrontend();
} 