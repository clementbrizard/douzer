package utils;

/**
 * Format a number adding a "0" before if it is a digit.
 * Example : "3" in input will return "03".
 * Especially useful when displaying durations.
 */
public abstract class FormatDigit {

  public static String run(String toFormat) {
    String formatted = toFormat;

    // If toFormat is a digit, we want to add a "0" before
    // it for better display
    if (Integer.parseInt(toFormat) < 10) {
      formatted = "0" + formatted;
    }

    return formatted;
  }
}
