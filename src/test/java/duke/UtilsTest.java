package duke;

import static duke.Utils.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UtilsTest {
  static MockedStatic<LocalDateTime> dateTime;
  static LocalDateTime defaultTime = LocalDateTime.of(2022, 01, 01, 12, 30);

  @BeforeAll
  public static void setup() {
    dateTime = Mockito.mockStatic(LocalDateTime.class);
    dateTime.when(LocalDateTime::now)
      .thenReturn(defaultTime);
  }

  @AfterAll
  public static void teardown() {
    dateTime.close();
  }

  // @IndicativeSentencesGeneration(
  //   separator = " ",
  //   generator = DisplayNameGenerator.ReplaceUnderscores.class
  // )
  @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
  @Nested
  class ParseDateTime_should {
    @Test
    public void parse_12_hour_times_correctly() {
      assertEquals(LocalDateTime.of(2022, 1, 1, 0, 5), parseDateTime("12:05am", null));
      assertEquals(LocalDateTime.of(2022, 1, 1, 21, 10), parseDateTime("09:10pm", null));
      assertEquals(LocalDateTime.of(2022, 1, 1, 11, 34), parseDateTime(null, "11:34am"));
    }

    @Test
    public void parse_24_hour_times_correctly() {
      assertEquals(LocalDateTime.of(2022, 1, 1, 23, 0), parseDateTime("23:00", null));
      assertEquals(LocalDateTime.of(2022, 1, 1, 23, 0), parseDateTime("2300", null));
      assertEquals(LocalDateTime.of(2022, 1, 1, 17, 0), parseDateTime("17:00", null));
      assertEquals(LocalDateTime.of(2022, 1, 1, 12, 33), parseDateTime("1233", null));

      assertThrows(DateTimeParseException.class, () -> parseDateTime("2500", null));
      assertThrows(DateTimeParseException.class, () -> parseDateTime("1269", null));

      assertThrows(DateTimeParseException.class, () -> parseDateTime(null, "17:00am"), 
        "Should throw DateTimeParseException when encountering a 24 hour time with am or pm");

      assertThrows(DateTimeParseException.class, () -> parseDateTime(null, "23:00pm"),
        "Should throw DateTimeParseException when encountering a 24 hour time with am or pm");
    }

    @Test
    public void parse_dates_correctly() {
      assertEquals(LocalDateTime.of(2022, 3, 5, 12, 30), parseDateTime("03/05", null));
      assertEquals(LocalDateTime.of(2022, 12, 31, 12, 30), parseDateTime(null, "31/12"));
      assertThrows(DateTimeParseException.class, () -> parseDateTime(null, "12/31"),
        "Should throw DateTimeParseException when encountering a wrong date format");
    }

    @Test
    public void parse_both_correctly() {
      assertEquals(LocalDateTime.of(2022, 11, 7, 0, 0), parseDateTime("07/11", "12:00am"));
      assertEquals(LocalDateTime.of(2022, 7, 4, 16, 39), parseDateTime("1639", "04/07"));
      assertEquals(parseDateTime("07/11", "12:00am"), parseDateTime("12:00am", "07/11"),
        "Order should not matter");

      assertThrows(DateTimeParseException.class, () -> parseDateTime("1400", "1231"),
        "Should throw DateTimeParseException when there's no valid date");
      assertThrows(DateTimeParseException.class, () -> parseDateTime("03/02", "01/01"),
        "Should throw DateTimeParseException when there's no valid date");
      assertThrows(DateTimeParseException.class, () -> parseDateTime("2500", "01/01"),
        "Should throw DateTimeParseException when there's one invalid component");
    }
  }
}
