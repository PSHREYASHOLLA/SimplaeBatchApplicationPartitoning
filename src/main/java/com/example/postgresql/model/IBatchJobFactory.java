package com.example.postgresql.model;

import org.springframework.batch.core.Job;

/**
 * A factory for creating IBatchJob objects.
 */
public interface IBatchJobFactory {

  /**
   * Gets the batch job.
   *
   * @param tenantCode            the tenant code
   * @param applicationJobDataDTO the application job data DTO
   * @return the batch job
   * @throws Exception the exception
   */
  Job getBatchJob(String tenantCode, ApplicationJobDataReqDTO applicationJobDataDTO) throws Exception;

}
