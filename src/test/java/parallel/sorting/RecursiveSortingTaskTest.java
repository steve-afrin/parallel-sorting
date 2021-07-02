package parallel.sorting;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class RecursiveSortingTaskTest {
  private static final Logger LOG = LoggerFactory.getLogger(RecursiveSortingTaskTest.class);

  @Test
  void computeNoSplit() {
    Set<String> inputValues = Stream.of("Steve", "Larry", "Adam", "Mike", "Xavier", "Jake", "Nate")
      .collect(Collectors.toSet());
    RecursiveSortingTask sortingTask = new RecursiveSortingTask(inputValues);
    List<String> sortedValues = sortingTask.compute();
    LOG.info("The complete, final sorted result set is: {}", sortedValues);
    List<String> expectedValues = new ArrayList<>(new TreeSet<>(inputValues));

    assertFalse(inputValues.isEmpty());
    assertEquals(7, inputValues.size());
    assertEquals(sortedValues, expectedValues);
    assertNotSame(sortedValues, expectedValues);
  }

  @Test
  void computeSplit() {
    Set<String> inputValues = Stream.of("Steve", "Larry", "Mike", "Xavier", "Jake", "Nate", "Julie", "Evelyne",
            "Beverly", "Thomas", "River", "Charlie", "Adam", "Xavier", "Russ", "Garrett", "Harry", "David", "Fred",
            "Zeke", "Victor", "Yvonne", "Siobhan", "Jeff", "Ivan", "Kyle", "Frank", "Rob", "Erin", "Nicole", "Paul")
        .collect(Collectors.toSet());
    RecursiveSortingTask sortingTask = new RecursiveSortingTask(inputValues);
    List<String> sortedValues = sortingTask.compute();
    LOG.info("The complete, final sorted result set is: {}", sortedValues);
    List<String> expectedValues = new ArrayList<>(new TreeSet<>(inputValues));

    assertFalse(inputValues.isEmpty());
    assertEquals(30, inputValues.size());
    assertEquals(sortedValues, expectedValues);
    assertNotSame(sortedValues, expectedValues);
  }
}