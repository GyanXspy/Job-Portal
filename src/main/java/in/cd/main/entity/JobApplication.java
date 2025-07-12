package in.cd.main.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String education;
    
    @Column(nullable = false)
    private String experienceLevel;
    
    @Column(columnDefinition = "TEXT")
    private String additionalNotes;
    
    @Column(nullable = false)
    private String resumeFileName;
    
    @Column(nullable = false)
    private String resumeFilePath;
    
    @Column(nullable = false)
    private String appliedPosition;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime appliedAt;
    
    @Column(nullable = false)
    private String status = "PENDING";
    
    @Column(nullable = false)
    private boolean emailSent = false;

    @Transient
    private String resumeStoredFileName;

    public String getResumeStoredFileName() {
        return resumeStoredFileName;
    }
    public void setResumeStoredFileName(String resumeStoredFileName) {
        this.resumeStoredFileName = resumeStoredFileName;
    }
} 