package com.example.postgresql.model;

import org.springframework.http.ResponseEntity;

/**
 * The Interface FebpApplicationJobService.
 */
public interface FebpApplicationJobService {

  /**
   * Trigger or resume application batch job.
   *
   * @param applicationJobDataDTO the application job data DTO
   * @return the response entity
   */
  ResponseEntity<ApplicationJobDataResDTO> triggerOrResumeApplicationBatchJob(ApplicationJobDataReqDTO applicationJobDataDTO);
  
  
}
