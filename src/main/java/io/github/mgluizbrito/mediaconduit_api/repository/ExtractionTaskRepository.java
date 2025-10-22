package io.github.mgluizbrito.mediaconduit_api.repository;

import io.github.mgluizbrito.mediaconduit_api.model.ExtractionTask;
import io.github.mgluizbrito.mediaconduit_api.model.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExtractionTaskRepository extends JpaRepository<ExtractionTask, UUID> {
    
    Optional<ExtractionTask> findByJobId(UUID jobId);
    
    List<ExtractionTask> findByStatus(JobStatus status);
    
    Optional<ExtractionTask> findByMediaIdentifier(String mediaIdentifier);
    
    List<ExtractionTask> findByStatusIn(List<JobStatus> statuses);
}
