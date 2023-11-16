package com.example.postgresql.model;

import java.io.Serializable;

/**
 * The Class EmployeeDetail.
 */
public class EmployeeDetail implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The emp id. */
  private String EMP_ID;

  /** The salary. */
  private Long EMP_SALARY;

  /**
   * Gets the emp id.
   *
   * @return the emp id
   */
  public String getEmpId() {
    return EMP_ID;
  }

  /**
   * Sets the emp id.
   *
   * @param empId the new emp id
   */
  public void setEmpId(String empId) {
    this.EMP_ID = empId;
  }

  /**
   * Gets the salary.
   *
   * @return the salary
   */
  public Long getEmpSalary() {
    return EMP_SALARY;
  }

  /**
   * Sets the salary.
   *
   * @param empSalary the new emp salary
   */
  public void setEmpSalary(Long empSalary) {
    this.EMP_SALARY = empSalary;
  }

}
