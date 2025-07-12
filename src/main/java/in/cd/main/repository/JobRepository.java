package in.cd.main.repository;

import in.cd.main.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    
    List<Job> findByStatus(String status);
    
    List<Job> findByStatusOrderByCreatedAtDesc(String status);
    
    List<Job> findByDepartment(String department);
    
    List<Job> findByEmploymentType(String employmentType);
    
    List<Job> findByExperienceLevel(String experienceLevel);
    
    List<Job> findByLocationContainingIgnoreCase(String location);
    
    List<Job> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' AND (j.applicationDeadline IS NULL OR j.applicationDeadline > ?1)")
    List<Job> findActiveJobsWithValidDeadline(LocalDateTime now);
    
    @Query("SELECT DISTINCT j.department FROM Job j WHERE j.status = 'ACTIVE'")
    List<String> findDistinctActiveDepartments();
    
    @Query("SELECT DISTINCT j.location FROM Job j WHERE j.status = 'ACTIVE'")
    List<String> findDistinctActiveLocations();
    
    @Query("SELECT DISTINCT j.employmentType FROM Job j WHERE j.status = 'ACTIVE'")
    List<String> findDistinctActiveEmploymentTypes();
    
    @Query("SELECT DISTINCT j.experienceLevel FROM Job j WHERE j.status = 'ACTIVE'")
    List<String> findDistinctActiveExperienceLevels();
} 