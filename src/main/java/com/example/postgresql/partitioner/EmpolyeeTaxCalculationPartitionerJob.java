package com.example.postgresql.partitioner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.example.postgresql.model.EmployeeDetail;
import com.example.postgresql.model.EmployeeTaxDetail;

public class EmpolyeeTaxCalculationPartitionerJob {

//	 /** The job repository. */
//	  @Autowired
//	  @Qualifier("febpBatchJobRepository")
//	  private JobRepository jobRepository;
//	  @Autowired
//	  @Qualifier("transactionManager")
//	  private JdbcTransactionManager transactionManager;
//	  
//	  /** The febp data source provider. */
//	  @Autowired
//	  @Qualifier("febpDataSource")
//	  private DataSource febpDBSrc;
//
//	  /** The Constant SELECT_CLAUSE_PARTY. */
//	  private static final String SELECT_CLAUSE_PARTY = "SELECT EMP_ID, EMP_SALARY";
//
//	  /** The Constant FROM_CLAUSE_PARTY. */
//	  private static final String FROM_CLAUSE_PARTY = "FROM FEBP_EMP_DETAIL_TEST";
//
//	  /** The Constant WHERE_CLAUSE_PARTY. */
//	  private static final String WHERE_CLAUSE_PARTY = "TAX_CALCULATION_FLAG='Y'";
//
//	  
//	@Bean(name = "partitionerJob")
//	public Job getBatchJob()
//	  throws UnexpectedInputException, MalformedURLException, ParseException {
//	    Job febpTaxCalculationJob = (Job) new JobBuilder("partitionerJob", jobRepository).start(partitionStep()).build();
//	    return febpTaxCalculationJob;
//
//	}
//	
//	@Bean
//	public Step partitionStep() 
//	  throws UnexpectedInputException, ParseException {
//	    return new StepBuilder("partitionStep", jobRepository)
//	      .partitioner("slaveStep", partitioner())
//	      .step(slaveStep())
//	      .taskExecutor(taskExecutor())
//	      .build();
//	}
//	@Bean
//    public Step slaveStep() throws UnexpectedInputException, ParseException {
//        return new StepBuilder("slaveStep", jobRepository)
//          .<EmployeeDetail, EmployeeTaxDetail>chunk(1, transactionManager)
//          .reader(getPagingItemReader())
//          .writer(itemWriter(marshaller(), null))
//          .build();
//    }
//
//    @Bean
//    public CustomMultiResourcePartitioner partitioner() {
//        CustomMultiResourcePartitioner partitioner = new CustomMultiResourcePartitioner();
//        Resource[] resources;
//        try {
//            resources = resourcePatternResolver.getResources("file:src/main/resources/input/partitioner/*.csv");
//        } catch (IOException e) {
//            throw new RuntimeException("I/O problems when resolving the input file pattern.", e);
//        }
//        partitioner.setResources(resources);
//        return partitioner;
//    }
//    @Bean
//    public TaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//        taskExecutor.setMaxPoolSize(5);
//        taskExecutor.setCorePoolSize(5);
//        taskExecutor.setQueueCapacity(5);
//        taskExecutor.afterPropertiesSet();
//        return taskExecutor;
//    }
//    @Bean
//    @StepScope
//    public JdbcPagingItemReader<EmployeeDetail> getPagingItemReader() throws Exception {
//  	  System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
//  		System.out.println("Inside getPagingItemReader()");
//  		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");  
//  	  
//      JdbcPagingItemReader<EmployeeDetail> reader = new JdbcPagingItemReader<>();
//      reader.setDataSource(febpDBSrc);
//      reader.setFetchSize(5);
//      reader.setPageSize(5);
//      reader.setRowMapper(new BeanPropertyRowMapper<>(EmployeeDetail.class));
//      // reader.setParameterValues(Collections.<String,
//      // Object>singletonMap("defaultLocale", getDefaultLocale()));
//      Map<String, Order> sortKeys = new HashMap<>();
//      sortKeys.put("EMP_ID", Order.ASCENDING);
//
//      SqlPagingQueryProviderFactoryBean factory = new SqlPagingQueryProviderFactoryBean();
//      factory.setDataSource(febpDBSrc);
//
//      factory.setSelectClause(SELECT_CLAUSE_PARTY);
//      factory.setFromClause(FROM_CLAUSE_PARTY);
//      factory.setWhereClause(WHERE_CLAUSE_PARTY);
//      factory.setSortKeys(sortKeys);
//      reader.setQueryProvider(factory.getObject());
//      reader.afterPropertiesSet();
//      return reader;
//    }
}
