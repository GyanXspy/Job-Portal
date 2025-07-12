package in.cd.main.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;
    
    @Column(columnDefinition = "TEXT")
    private String requirements;
    
    @Column(columnDefinition = "TEXT")
    private String responsibilities;
    
    @Column(nullable = false)
    private String location;
    
    @Column(nullable = false)
    private String employmentType; // FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP
    
    @Column(nullable = false)
    private String experienceLevel; // ENTRY, MID, SENIOR, LEAD
    
    @Column(nullable = false)
    private String department;
    
    @Column(nullable = false)
    private String status = "ACTIVE"; // ACTIVE, INACTIVE, CLOSED
    
    @Column(nullable = false)
    private Integer openings = 1;
    
    @Column(nullable = false)
    private String salaryRange;
    
    @Column(columnDefinition = "TEXT")
    private String benefits;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(nullable = false)
    private String createdBy;
    
    @Column
    private LocalDateTime applicationDeadline;
    
    @Column(columnDefinition = "TEXT")
    private String additionalInfo;
} 