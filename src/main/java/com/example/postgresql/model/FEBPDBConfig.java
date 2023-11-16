package com.example.postgresql.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

/**
 * The Class FEBPDBConfig.
 */
@Configuration
public class FEBPDBConfig {
	

	 @Value("${spring.datasource.hikari.schema}")
	  private String dbSchemaName;
	 @Value("${spring.datasource.username}")
	  private String dbUserName;
	 @Value("${spring.datasource.password}")
	  private String dbPassword;
	 @Value("${spring.datasource.url}")
	  private String dbUrl;

  @Primary
  @Bean(name = "febpDataSource")

  public DataSource batchDataSource() {
    DataSource febpDBSrc = DataSourceBuilder.create().username(dbUserName).password(dbPassword).url(dbUrl).build();
    if (febpDBSrc != null && febpDBSrc instanceof HikariDataSource) {
      @SuppressWarnings("resource")
      HikariDataSource hikariDatsource = (HikariDataSource) febpDBSrc;
      hikariDatsource.setSchema(dbSchemaName);
    }
    return febpDBSrc;
  }
 
  @Bean(name = "transactionManager")
  public JdbcTransactionManager batchTransactionManager(@Qualifier("febpDataSource") DataSource dataSource) {
    return new JdbcTransactionManager(dataSource);
  }

  @Bean(name = "febpBatchJobRepository")
  public JobRepository jobRepository(@Qualifier("febpDataSource") DataSource batchDataSource,
      @Qualifier("transactionManager") JdbcTransactionManager batchTransactionManager) throws Exception {
    JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
    factory.setDataSource(batchDataSource);
    factory.setTransactionManager(batchTransactionManager);
    factory.afterPropertiesSet();
    return factory.getObject();
  }

  @Bean(name = "febpBatchJobLauncher")
  public JobLauncher jobLauncher(@Qualifier("febpBatchJobRepository") JobRepository jobRepository) throws Exception {
    TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
    jobLauncher.setJobRepository(jobRepository);
    jobLauncher.afterPropertiesSet();
    return jobLauncher;
  }

  @Bean (name = "febpBatchJobExplorer")
  public JobExplorer jobExplorer(@Qualifier("febpDataSource") DataSource dataSource,@Qualifier("transactionManager")JdbcTransactionManager batchTransactionManager) throws Exception {
      final JobExplorerFactoryBean bean = new JobExplorerFactoryBean();
      bean.setDataSource(dataSource);
      bean.setTransactionManager(batchTransactionManager);
      bean.setTablePrefix("BATCH_");
      bean.setJdbcOperations(new JdbcTemplate(dataSource));
      bean.afterPropertiesSet();
      return bean.getObject();
  }
  
  @Bean (name ="febpBatchJobRegistry")
  public JobRegistry jobRegistry() throws Exception {
  	return new MapJobRegistry();
  }
  
  @Bean
  public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(@Qualifier("febpBatchJobRegistry") JobRegistry jobRegistry) {
      JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
      postProcessor.setJobRegistry(jobRegistry);
      return postProcessor;
  }
  
  @Bean (name = "febpBatchJobOperator")
  public JobOperator jobOperator(@Qualifier("febpBatchJobLauncher") JobLauncher jobLauncher, @Qualifier("febpBatchJobRepository") JobRepository jobRepository,
		  @Qualifier("febpBatchJobRegistry") JobRegistry jobRegistry, @Qualifier("febpBatchJobExplorer") JobExplorer jobExplorer) {
      final SimpleJobOperator jobOperator = new SimpleJobOperator();
      jobOperator.setJobLauncher(jobLauncher);
      jobOperator.setJobRepository(jobRepository);
      jobOperator.setJobRegistry(jobRegistry);
      jobOperator.setJobExplorer(jobExplorer);
      return jobOperator;
  }
}
