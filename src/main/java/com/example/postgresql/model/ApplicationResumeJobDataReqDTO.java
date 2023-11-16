package com.example.postgresql.model;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public class ApplicationResumeJobDataReqDTO implements Serializable {
	
	public ApplicationResumeJobDataReqDTO() {
		  
	}

	public ApplicationResumeJobDataReqDTO(@Size(max = 256) String appResumeJobName) {
		super();
		this.appResumeJobName = appResumeJobName;
	}

	/** The Constant serialVersionUID. */
	  private static final long serialVersionUID = 1L;
	
	  /** The job bean class name. */
	  @Size(max = 256)
	  @Schema(title = "Job Name, must be a valid Job Name", example = "FERA_CONSOLIDATED_STATEMENT")
	  private String appResumeJobName;
	
	/**
	 * @return the appResumeJobName
	 */
	public String getAppResumeJobName() {
		return appResumeJobName;
	}
	
	/**
	 * @param appResumeJobName the appResumeJobName to set
	 */
	public void setAppResumeJobName(String appResumeJobName) {
		this.appResumeJobName = appResumeJobName;
	}
}
