package parallel.sorting;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sorter {
  private static final Logger LOG = LoggerFactory.getLogger(Sorter.class);

  private static final int INSUFFICIENT_ARGUMENTS = 1;
  private static final int FILE_NOT_FOUND         = 2;
  private static final int SOURCE_FILE_READ_ERROR = 3;

  private Sorter() {}

  /**
   * Just a stupid, simple method to simply read data from an external file and initialize the data set
   * to be sorted.
   *
   * @param sourceFilename a String value specifying the location in the local filesystem of a regular text
   *                       file containing data to be alphabetically sorted
   * @return a {@link List} of String data to be alphabetically sorted
   * @throws IOException when there's any kind of problem accessing or reading the specified data file
   */
  private List<String> readValues(final String sourceFilename) throws IOException {
    try (BufferedReader inFile = new BufferedReader(new FileReader(sourceFilename))) {
      return inFile.lines()
          .map(String::trim)
          .filter(IS_VALID_LINE)
          .collect(Collectors.toList());
    }
  }

  public List<String> parallelSort(final Collection<String> values) {
    // This is the creation of the default ForkJoinPool to manage all concurrency threads for parallel processing.
    ForkJoinPool commonPool = ForkJoinPool.commonPool();
    return commonPool.invoke(new RecursiveSortingTask(values));
  }

  /**
   * Main entry point for this application.
   *
   * @param args the only mandatory parameter is the name of a regular text file in the local filesystem that
   *             contains textual data to be alphabetically sorted
   */
  public static void main(final String[] args) {
    if (args.length == 0) {
      printUsage();
      System.exit(INSUFFICIENT_ARGUMENTS);
    }

    Sorter sorter = new Sorter();

    final String sourceFilename = args[0];
    Collection<String> initialValues = null;
    try {
      initialValues = sorter.readValues(sourceFilename);
    } catch(FileNotFoundException ex) {
      LOG.error("Could not find file '{}' in the local filesystem", sourceFilename, ex);
      System.exit(FILE_NOT_FOUND);
    } catch(IOException ex) {
      LOG.error("Some unexpected error encountered while trying to read file '{}' in the local filesystem",
          sourceFilename, ex);
      System.exit(SOURCE_FILE_READ_ERROR);
    }

    final Collection<String> sortedValues = sorter.parallelSort(initialValues);

    LOG.debug("The initial (unsorted) values are: {} ", initialValues);
    LOG.info("The sorted values are: {}", sortedValues);
  }

  private static void printUsage() {
    LOG.error("Usage: java {} {filename}", Sorter.class.getName());
    LOG.error("where {filename} is the name of a file in the local filesystem that contain String values to be "
        + "sorted");
  }

  private static final Predicate<String> IS_VALID_LINE = s -> !s.isEmpty() && s.charAt(0) != '#' && s.charAt(0) != '/';
}
