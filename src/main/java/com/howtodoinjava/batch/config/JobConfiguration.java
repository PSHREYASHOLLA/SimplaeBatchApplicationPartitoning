package com.howtodoinjava.batch.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.support.JdbcTransactionManager;

import com.howtodoinjava.batch.mapper.CustomerRowMapper;
import com.howtodoinjava.batch.model.Customer;
import com.howtodoinjava.batch.partitioner.ColumnRangePartitioner;

@Configuration
public class JobConfiguration 
{
	 /** The job builder factory. */
	  @Autowired
	  @Qualifier("transactionManager")
	  private JdbcTransactionManager transactionManager;
	/** The job repository. */
	  @Autowired
	  @Qualifier("febpBatchJobRepository")
	  private JobRepository jobRepository;

	  /** The febp data source provider. */
	  @Autowired
	  @Qualifier("febpDataSource")
	  private DataSource febpDBSrc;

	@Bean
	public ColumnRangePartitioner partitioner() 
	{
		ColumnRangePartitioner columnRangePartitioner = new ColumnRangePartitioner();
		columnRangePartitioner.setColumn("id");
		columnRangePartitioner.setDataSource(febpDBSrc);
		columnRangePartitioner.setTable("customer");
		return columnRangePartitioner;
	}

	@Bean
	@StepScope
	public JdbcPagingItemReader<Customer> pagingItemReader(
			@Value("#{stepExecutionContext['minValue']}") Long minValue,
			@Value("#{stepExecutionContext['maxValue']}") Long maxValue) 
	{
		System.out.println("reading " + minValue + " to " + maxValue);

		Map<String, Order> sortKeys = new HashMap<>();
		sortKeys.put("id", Order.ASCENDING);
		
		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("id, firstName, lastName, birthdate");
		queryProvider.setFromClause("from customer");
		queryProvider.setWhereClause("where id >= " + minValue + " and id < " + maxValue);
		queryProvider.setSortKeys(sortKeys);
		
		JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();
		reader.setDataSource(this.febpDBSrc);
		reader.setFetchSize(1000);
		reader.setRowMapper(new CustomerRowMapper());
		reader.setQueryProvider(queryProvider);
		
		return reader;
	}
	
	
	@Bean
	@StepScope
	public JdbcBatchItemWriter<Customer> customerItemWriter()
	{
		JdbcBatchItemWriter<Customer> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(febpDBSrc);
		itemWriter.setSql("INSERT INTO NEW_CUSTOMER VALUES (:id, :firstName, :lastName, :birthdate)");

		itemWriter.setItemSqlParameterSourceProvider
			(new BeanPropertyItemSqlParameterSourceProvider<>());
		itemWriter.afterPropertiesSet();
		
		return itemWriter;
	}
	
	// Master
	@Bean
	public Step step1() 
	{
		return new StepBuilder("step1", jobRepository)
				.partitioner(slaveStep().getName(), partitioner())
				.step(slaveStep())
				.gridSize(4)
				.taskExecutor(new SimpleAsyncTaskExecutor())
				.build();
	}
	
	// slave step
	@Bean
	public Step slaveStep() 
	{
		return new StepBuilder("slaveStep", jobRepository)
				.<Customer, Customer>chunk(1000,transactionManager)
				.reader(pagingItemReader(null, null))
				.writer(customerItemWriter())
				.build();
	}
	
	@Bean(name="FEBP_PARTITIONER")
	  public Job getBatchJob() throws Exception {
	    Job febpTaxCalculationJob = new JobBuilder("FEBP_PARTITIONER", jobRepository).incrementer(new RunIdIncrementer()).start(step1()).build();
	    return febpTaxCalculationJob;
	}
}