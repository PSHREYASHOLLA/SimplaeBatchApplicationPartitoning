package com.example.postgresql.model;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class AbstractItemWriter.
 *
 * @param <I> the generic type
 * @param <O> the generic type
 */
public abstract class AbstractItemProcessor<I, O> implements ItemProcessor<I, O> {

  /** The parameters. */
  private JobParameters parameters;

  /**
   * Process.
   *
   * @param item the item
   * @return the o
   * @throws Exception the exception
   */
  @Override
  public O process(I item) throws Exception {
    return executeProcess(item);
  }

  /**
   * Execute write.
   *
   * @param item the item
   * @return the o
   * @throws Exception the exception
   */
  protected abstract O executeProcess(I item) throws Exception;

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
