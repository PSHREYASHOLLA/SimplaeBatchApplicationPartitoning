package com.example.postgresql.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * The Class FebpApplicationJobServiceImpl.
 */
@Service
public class FebpApplicationJobServiceImpl implements FebpApplicationJobService {

  /** The LOGGER. */
  private static final Logger LOGGER = LoggerFactory.getLogger(FebpApplicationJobServiceImpl.class);

  /** The app context. */
  @Autowired
  private ApplicationContext appContext;

  /** The job launcher. */
  @Autowired
  @Qualifier("febpBatchJobLauncher")
  private JobLauncher jobLauncher;

  /** The job explorer. */
  @Autowired
  @Qualifier("febpBatchJobExplorer")
  private JobExplorer jobExplorer;
  
  @Autowired
  @Qualifier ("febpBatchJobRegistry")
  private JobRegistry jobRegistry;

  /**
   * Trigger or resume application batch job.
   *
   * @param applicationJobDataDTO the application job data DTO
   * @return the response entity
   */
  @Override
  public ResponseEntity<ApplicationJobDataResDTO> triggerOrResumeApplicationBatchJob(ApplicationJobDataReqDTO applicationJobDataDTO) {
    ApplicationJobDataResDTO applicationJobDataResDTO = new ApplicationJobDataResDTO();
    if (applicationJobDataDTO.getAppRestartJobExecutionId() == null || applicationJobDataDTO.getAppRestartJobExecutionId().isEmpty()) {
      BatchStatus appJobStatus = triggerApplicationBatchJob(applicationJobDataDTO);
      
      applicationJobDataResDTO.setAppJobStatus(appJobStatus != null ? appJobStatus.toString() : null);
    } else {
      BatchStatus appJobResumeStatus = null;
      try {
        appJobResumeStatus = resumeApplicationBatchJob(applicationJobDataDTO.getAppRestartJobExecutionId(), applicationJobDataResDTO,applicationJobDataDTO);
      } catch (Exception e) {
        LOGGER.error("Error during resume FEBP Batch Job:  ", e);
      }
      applicationJobDataResDTO.setAppJobStatus(appJobResumeStatus != null ? appJobResumeStatus.toString() : null);

    }
    return new ResponseEntity<>(applicationJobDataResDTO, HttpStatus.OK);
  }

  /**
   * Trigger application batch job.
   *
   * @param applicationJobDataDTO the application job data DTO
   * @return the batch status
   */
  private BatchStatus triggerApplicationBatchJob(ApplicationJobDataReqDTO applicationJobDataDTO) {
    JobExecution jobExecution = null;
    try {
		Job appJob= (Job)appContext.getBean(applicationJobDataDTO.getAppJobBeanName());
//      IBatchJobFactory batchJobFactory = (IBatchJobFactory) appContext.getBean(applicationJobDataDTO.getAppJobBeanName());
//      Job appJob = batchJobFactory.getBatchJob("NotUsed", applicationJobDataDTO);
      if (!jobRegistry.getJobNames().contains(appJob.getName())) {
    	  ReferenceJobFactory referenceJobFactory = new ReferenceJobFactory(appJob);
          jobRegistry.register(referenceJobFactory);
        }
      Map<String, Object> appJobParamsMap = applicationJobDataDTO.getAppJobParamsMap();
      JobParameters appJobParams = configureJobParameters(appJobParamsMap);
      jobExecution = jobLauncher.run(appJob, appJobParams);
      return BatchStatus.STARTED;
    } catch (final JobExecutionAlreadyRunningException e) {
      LOGGER.error("JobExecutionAlreadyRunningException: ", e);
    } catch (final JobRestartException e) {
      LOGGER.error("JobRestartException: ", e);
    } catch (final JobInstanceAlreadyCompleteException e) {
      LOGGER.error("JobInstanceAlreadyCompleteException: ", e);
    } catch (final JobParametersInvalidException e) {
      LOGGER.error("JobParametersInvalidException: ", e);
    } catch (final NoSuchBeanDefinitionException e) {
      LOGGER.error("No Job bean name defined with {}", applicationJobDataDTO.getAppJobBeanName());
    } catch (final Exception e) {
      LOGGER.error("Exception: ", e);
    }
    return BatchStatus.FAILED;
  }

  /**
   * Configure job parameters.
   *
   * @param appJobParamsMap the app job params map
   * @return the job parameters
   */
  private JobParameters configureJobParameters(Map<String, Object> appJobParamsMap) {
    JobParametersBuilder jobParameterBuilder = new JobParametersBuilder();
    if (appJobParamsMap != null && !appJobParamsMap.isEmpty()) {
      for (Map.Entry<String, Object> entry : appJobParamsMap.entrySet()) {
        String key = entry.getKey();
        Object value = entry.getValue();
        if (value instanceof String strValue) {
          jobParameterBuilder.addString(key, strValue);
        } else if (value instanceof Long lngValue) {
          jobParameterBuilder.addLong(key, lngValue);
        } else if (value instanceof Date dtValue) {
          jobParameterBuilder.addDate(key, dtValue);
        } else if (value instanceof Double dblValue) {
          jobParameterBuilder.addDouble(key, dblValue);
        } else if (value instanceof Map) {
          jobParameterBuilder.addJobParameter(key, value.toString(), String.class);
        }
      }
    }
    jobParameterBuilder.addString("time", String.valueOf(System.currentTimeMillis()));
    return jobParameterBuilder.toJobParameters();
  }

  private BatchStatus resumeApplicationBatchJob(String executionId, ApplicationJobDataResDTO applicationJobDataResDTO,ApplicationJobDataReqDTO applicationJobDataDTO) {
    Long failedBatchExecutionId = Long.parseLong(executionId);
    JobOperator jobOperator = (JobOperator) appContext.getBean("febpBatchJobOperator");
    try {
      Long restartId = jobOperator.restart(failedBatchExecutionId);
      String jobRestartSummary = jobOperator.getSummary(failedBatchExecutionId);
      LOGGER.info("SUMMARY AFTER RESTART: " + jobRestartSummary);
      applicationJobDataResDTO.setAppAfterRestartJobExecutionId(String.valueOf(restartId));
      applicationJobDataResDTO.setAppAfterRestartJobSummery(jobRestartSummary);
      return BatchStatus.STARTED;
    } catch (JobInstanceAlreadyCompleteException | NoSuchJobExecutionException | NoSuchJobException | JobRestartException | JobParametersInvalidException e) {
      LOGGER.error("Error while resuming FEBP batch job with execution ID : " + failedBatchExecutionId, e);

    }
    return BatchStatus.FAILED;
  }
}
