package parallel.sorting;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.fail;

class SorterTest {
  private static final String SOURCE_TEST_DATA_FILENAME = "src/test/resources/sorter-test-data.txt";

  @Test
  void readValues() {
    Sorter sorter = new Sorter();
    List<String> expectedValues = Stream.of("Data element 1", "Data element 2", "Data element 3",
        "Data element 4 # trailing comment included in data",
        "Data element 5 // alternative tailing comment included in data")
        .collect(Collectors.toList());
    List<String> actualValues = null;

    try {
      actualValues = sorter.readValues(SOURCE_TEST_DATA_FILENAME);
    } catch (IOException ex) {
      fail(ex);
    }

    assertEquals(5, actualValues.size());
    assertEquals(expectedValues, actualValues);
    assertNotSame(expectedValues, actualValues);
  }
}