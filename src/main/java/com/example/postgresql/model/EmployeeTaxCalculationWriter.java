package com.example.postgresql.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * The Class EmployeeTaxCalculationWriter.
 */
@Component
public class EmployeeTaxCalculationWriter extends AbstractItemWriter<EmployeeTaxDetail> {

  /** The febp data source provider. */
  @Autowired
  @Qualifier("febpDataSource")
  private DataSource febpDBSrc;

  /** The Constant LOGGER. */
  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeTaxCalculationWriter.class.getName());

  /** The Constant INSERT_REPORT_HISTORY_QUERY. */
  private static final String INSERT_EMP_TAX_QUERY = "INSERT INTO FEBP_EMP_TAX_DETAIL_TEST " + "(ENTITY_ID, EMP_ID, EMP_TAX) VALUES (?, ?, ?)";

  /**
   * Execute write.
   *
   * @param chunk the chunk
   * @throws Exception the exception
   */
  @Override
  protected void executeWrite(Chunk<? extends EmployeeTaxDetail> chunk) throws Exception {
    saveToTaxDetail(chunk);

  }

  /**
   * Save to tax detail.
   *
   * @param chunk the chunk
   */
  private void saveToTaxDetail(Chunk<? extends EmployeeTaxDetail> chunk) {
	  System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		System.out.println("Inside saveToTaxDetail()");

    List<EmployeeTaxDetail> employeeTaxDetailList = (List<EmployeeTaxDetail>) chunk.getItems();
    JdbcTemplate jdbcTemplate = new JdbcTemplate(febpDBSrc);
    jdbcTemplate.batchUpdate(INSERT_EMP_TAX_QUERY, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        EmployeeTaxDetail empTaxDetail = employeeTaxDetailList.get(i);
        ps.setString(1, UUID.randomUUID().toString());
        ps.setString(2, empTaxDetail.getEmpId());
        ps.setLong(3, empTaxDetail.getTaxAmount());
    	System.out.println(empTaxDetail.getEmpId());  	  

      }

      @Override
      public int getBatchSize() {
        return employeeTaxDetailList.size();
      }
    });
	System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");  	  

  }

}
