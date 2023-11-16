package com.example.postgresql.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.postgresql.model.ApplicationJobDataReqDTO;
import com.example.postgresql.model.ApplicationJobDataResDTO;
import com.example.postgresql.model.FebpApplicationJobService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/batch")
public class BatchController {
	/** The application job data service. */
	  @Autowired
	  private FebpApplicationJobService applicationJobService;
	  @PostMapping(value = "/b2e/trigger-or-resume-application-batch-job", produces = {"application/json"},
		      consumes = {"application/json"})
		  public ResponseEntity<ApplicationJobDataResDTO> triggerOrResumeApplicationBatchJobByB2E(@Valid @RequestBody ApplicationJobDataReqDTO applicationJobDataDTO) {
		    ResponseEntity<ApplicationJobDataResDTO> result = applicationJobService.triggerOrResumeApplicationBatchJob(applicationJobDataDTO);
		    return result;
		  }
}
