package com.example.postgresql.model;

import java.io.Serializable;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * The Class ApplicationJobDataResDTO.
 */
public class ApplicationJobDataResDTO implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;
  /** The app job status. */
  @Size(max = 100)
  @Schema(title = "Status of the Application Job.", example = "COMPLETED, STARTING, STARTED, STOPPING, STOPPED, FAILED, ABANDONED, UNKNOWN")
  private String appJobStatus;

  @Schema(title = "Job After Restart Execution ID", example = "1881")
  private String appAfterRestartJobExecutionId;
  
  @Schema(title = "Job After Restart Summary")
  private String appAfterRestartJobSummery;
  
  
  
  /**
   * Gets the app job status.
   *
   * @return the app job status
   */
  public String getAppJobStatus() {
    return appJobStatus;
  }

  /**
   * Sets the app job status.
   *
   * @param appJobStatus the new app job status
   */
  public void setAppJobStatus(String appJobStatus) {
    this.appJobStatus = appJobStatus;
  }

  public String getAppAfterRestartJobExecutionId() {
		return appAfterRestartJobExecutionId;
  }
	
  public void setAppAfterRestartJobExecutionId(String appAfterRestartJobExecutionId) {
		this.appAfterRestartJobExecutionId = appAfterRestartJobExecutionId;
  }
	
	public String getAppAfterRestartJobSummery() {
		return appAfterRestartJobSummery;
	}
	
	public void setAppAfterRestartJobSummery(String appAfterRestartJobSummery) {
		this.appAfterRestartJobSummery = appAfterRestartJobSummery;
	}
}
