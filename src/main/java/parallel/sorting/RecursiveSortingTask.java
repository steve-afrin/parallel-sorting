package parallel.sorting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Describes how to divide large data sets into smaller units of work and then creates new
 * tasks for that work to happen on different threads in the <b>ForkJoinPool</b> instance
 * managed by the caller.
 */
public class RecursiveSortingTask extends RecursiveTask<List<String>> {
  private static final Logger LOG = LoggerFactory.getLogger(RecursiveSortingTask.class);

  private static final int THRESHOLD = 10;

  private final Collection<String> values;

  public RecursiveSortingTask(final Collection<String> initialValues) {
    this.values = initialValues;
  }

  @Override
  protected List<String> compute() {
    if (this.values.size() > THRESHOLD) {
      LOG.debug("Divide and conquer: {} items exceeds threshold of {}", this.values.size(), THRESHOLD);
      return ForkJoinTask.invokeAll(createSubtasks())
          /* This is the merging part of results returning from instances of RecursiveSortingTask with smaller
           * data sets. */
          .stream()
          .flatMap(sortingTask -> sortingTask.join().stream())
          .sorted()
          .collect(Collectors.toList());
    } else {
      // This is the actual work for sorting data sets that are smaller than the THRESHOLD size.
      LOG.debug("Sorting {} items: {}", this.values.size(), this.values);
      List<String> newValues = this.values.stream()
          .sorted()
          .collect(Collectors.toList());
      LOG.debug("The sorted subset of data is: {}", newValues);
      return newValues;
    }
  }

  /**
   * Called from the {@link #compute()} method, this method is the <em>recursive</em> part
   * of this class that takes the current data set for this instance and divides it into
   * two new {@link RecursiveSortingTask} instances with smaller data sets.
   * @return a {@link Collection} with two {@link RecursiveSortingTask} instances that each
   * contain half the work of the larger data set contained in this instance
   */
  private Collection<RecursiveSortingTask> createSubtasks() {
    List<RecursiveSortingTask> dividedTasks = new ArrayList<>(2);

    List<String> valuesList = new ArrayList<>(this.values);
    final int midpoint = (valuesList.size() / 2);
    final List<String> firstHalf = valuesList.subList(0, midpoint);
    final List<String> secondHalf = valuesList.subList(midpoint, valuesList.size());
    LOG.debug("First half of divided items: {}", firstHalf);
    LOG.debug("Second half of divided items: {}", secondHalf);

    dividedTasks.add(new RecursiveSortingTask(firstHalf));
    dividedTasks.add(new RecursiveSortingTask(secondHalf));
    return dividedTasks;
  }
}
