package com.example.postgresql.model;

/**
 * The Class EmployeeTaxDetail.
 */
public class EmployeeTaxDetail {

  /** The emp id. */
  private String empId;

  /** The tax amount. */
  private Long taxAmount;

  /**
   * Gets the emp id.
   *
   * @return the emp id
   */
  public String getEmpId() {
    return empId;
  }

  /**
   * Sets the emp id.
   *
   * @param empId the new emp id
   */
  public void setEmpId(String empId) {
    this.empId = empId;
  }

  /**
   * Gets the tax amount.
   *
   * @return the tax amount
   */
  public Long getTaxAmount() {
    return taxAmount;
  }

  /**
   * Sets the tax amount.
   *
   * @param taxAmount the new tax amount
   */
  public void setTaxAmount(Long taxAmount) {
    this.taxAmount = taxAmount;
  }

}
