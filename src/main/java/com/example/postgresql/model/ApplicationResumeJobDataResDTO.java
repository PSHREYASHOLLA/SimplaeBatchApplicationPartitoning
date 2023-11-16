package com.example.postgresql.model;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;

public class ApplicationResumeJobDataResDTO implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Schema(title = "Job Restart Execution ID", example = "1881")
	private String appResumeJobExecutionId;

	/**
	 * @return the appRestartJobExecutionId
	 */
	public String getAppRestartJobExecutionId() {
		return appResumeJobExecutionId;
	}

	/**
	 * @param appRestartJobExecutionId the appRestartJobExecutionId to set
	 */
	public void setAppRestartJobExecutionId(String appRestartJobExecutionId) {
		this.appResumeJobExecutionId = appRestartJobExecutionId;
	}
}
