package com.example.postgresql.model;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
/**
 * The Class EmployeeTaxCalculationReader.
 */

@Configuration
public class EmployeeTaxCalculationReader {

  /** The febp data source provider. */
  @Autowired
  @Qualifier("febpDataSource")
  private DataSource febpDBSrc;

  /** The Constant SELECT_CLAUSE_PARTY. */
  private static final String SELECT_CLAUSE_PARTY = "SELECT EMP_ID, EMP_SALARY";

  /** The Constant FROM_CLAUSE_PARTY. */
  private static final String FROM_CLAUSE_PARTY = "FROM FEBP_EMP_DETAIL_TEST";

  /** The Constant WHERE_CLAUSE_PARTY. */
  private static final String WHERE_CLAUSE_PARTY = "TAX_CALCULATION_FLAG='Y'";

  
  /**
   * Gets the paging item reader.
   *
   * @return the paging item reader
   * @throws Exception the exception
   */
  @Bean
  @StepScope
  public JdbcPagingItemReader<EmployeeDetail> getPagingItemReader() throws Exception {
	  System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		System.out.println("Inside getPagingItemReader()");
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");  
	  
    JdbcPagingItemReader<EmployeeDetail> reader = new JdbcPagingItemReader<>();
    reader.setDataSource(febpDBSrc);
    reader.setFetchSize(5);
    reader.setPageSize(5);
    reader.setRowMapper(new BeanPropertyRowMapper<>(EmployeeDetail.class));
    // reader.setParameterValues(Collections.<String,
    // Object>singletonMap("defaultLocale", getDefaultLocale()));
    Map<String, Order> sortKeys = new HashMap<>();
    sortKeys.put("EMP_ID", Order.ASCENDING);

    SqlPagingQueryProviderFactoryBean factory = new SqlPagingQueryProviderFactoryBean();
    factory.setDataSource(febpDBSrc);

    factory.setSelectClause(SELECT_CLAUSE_PARTY);
    factory.setFromClause(FROM_CLAUSE_PARTY);
    factory.setWhereClause(WHERE_CLAUSE_PARTY);
    factory.setSortKeys(sortKeys);
    reader.setQueryProvider(factory.getObject());
    reader.afterPropertiesSet();
    return reader;
  }
}
