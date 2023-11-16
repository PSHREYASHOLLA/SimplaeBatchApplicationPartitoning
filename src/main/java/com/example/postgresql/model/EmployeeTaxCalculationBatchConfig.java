package com.example.postgresql.model;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.support.JdbcTransactionManager;

/**
 * The Class EmployeeTaxCalculationBatchConfig.
 */
//@Component("FEBP_EMP_TAX_CALCULATION")
@Configuration
public class EmployeeTaxCalculationBatchConfig {

  /** The job builder factory. */
  @Autowired
  @Qualifier("transactionManager")
  private JdbcTransactionManager transactionManager;

  /** The job repository. */
  @Autowired
  @Qualifier("febpBatchJobRepository")
  private JobRepository jobRepository;

  /** The app context. */
  @Autowired
  private ApplicationContext appContext;

  /** The job registry. */
  @Autowired
  @Qualifier("febpBatchJobRegistry")
  private JobRegistry jobRegistry;

  /**
   * Gets the batch job.
   *
   * @param tenantCode            the tenant code
   * @param applicationJobDataDTO the application job data DTO
   * @return the batch job
   * @throws Exception the exception
   */
  //@Bean(name="FEBP_EMP_TAX_CALCULATION")
  public Job getBatchJob() throws Exception {
	System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	System.out.println("Inside getBatchJob()");
	System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    
	ItemProcessor<EmployeeDetail, EmployeeTaxDetail> itemProcessor = appContext.getBean(EmployeeTaxCalculationProcessor.class);
    ItemWriter<EmployeeTaxDetail> itemWriter = appContext.getBean(EmployeeTaxCalculationWriter.class);
    JdbcPagingItemReader<EmployeeDetail> pagingItemReader = appContext.getBean(JdbcPagingItemReader.class);
//    Step step = new StepBuilder("FEBP_EMP_TAX_CALCULATION_STEP", jobRepository)
//        .<EmployeeDetail, EmployeeTaxDetail>chunk(5, transactionManager)
//        .reader(reader.getPagingItemReader()).processor(itemProcessor).writer(itemWriter).taskExecutor(actStmntTaskExecutor()).build();
    
    Step step = new StepBuilder("FEBP_EMP_TAX_CALCULATION_STEP", jobRepository)
          .<EmployeeDetail, EmployeeTaxDetail>chunk(5, transactionManager)
          .reader(pagingItemReader).processor(itemProcessor).writer(itemWriter).build();
    Job febpTaxCalculationJob = new JobBuilder("FEBP_EMP_TAX_CALCULATION", jobRepository).incrementer(new RunIdIncrementer()).start(step).build();
    return febpTaxCalculationJob;
  }

  /**
   * Act stmnt task executor.
   *
   * @return the simple async task executor
   */
  @Bean
  public SimpleAsyncTaskExecutor actStmntTaskExecutor() {
	  System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		System.out.println("Inside actStmntTaskExecutor()");
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");    
	  
    SimpleAsyncTaskExecutor acctStmtTaskExecuter = new SimpleAsyncTaskExecutor();
    acctStmtTaskExecuter.setConcurrencyLimit(1);
    acctStmtTaskExecuter.setThreadPriority(Thread.MAX_PRIORITY);
    acctStmtTaskExecuter.setThreadNamePrefix("FEBP_TAX_CALCULATION_GEN");
    return acctStmtTaskExecuter;
  }

}
