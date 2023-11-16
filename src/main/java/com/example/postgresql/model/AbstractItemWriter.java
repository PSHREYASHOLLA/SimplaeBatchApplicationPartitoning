package com.example.postgresql.model;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.lang.NonNull;

/**
 * The Class AbstractItemWriter.
 *
 * @param <T> the generic type
 */
public abstract class AbstractItemWriter<T> implements ItemWriter<T> {

  /** The parameters. */
  private JobParameters parameters;

  /**
   * Write.
   *
   * @param chunk the chunk
   * @throws Exception the exception
   */
  @Override
  public void write(@NonNull Chunk<? extends T> chunk) throws Exception {
    
    executeWrite(chunk);
  }

  /**
   * Execute write.
   *
   * @param chunk the chunk
   * @throws Exception the exception
   */
  protected abstract void executeWrite(@NonNull Chunk<? extends T> chunk) throws Exception;

  /**
   * Before step.
   *
   * @param stepExecution the step execution
   */
  @BeforeStep
  public void beforeStep(final StepExecution stepExecution) {
    parameters = stepExecution.getJobExecution().getJobParameters();
  }

  /**
   * Gets the parameters.
   *
   * @return the parameters
   */
  public JobParameters getParameters() {
    return parameters;
  }
}
