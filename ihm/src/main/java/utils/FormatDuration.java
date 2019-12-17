package utils;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class FormatDuration {
  private static final Logger formatDurationLogger = LogManager.getLogger();

  /**
   * Format a Duration object. Useful for musics.
   * Handle the case of duration with hours.
   *
   * @param duration the Duration object
   * @return duration formatted to a human readable string
   */
  public static String run(Duration duration) {

    // Duration toString() returns ISO 8601 duration. We get it and
    // extract hour, minute and second using a Regex

    // Need to do this join because regex is too long for one line
    String regex = String.join(
        "",
        "PT((?<hour>\\d{0,2})H)?",
        "((?<minute>\\d{0,2})M)?",
        "((?<second>\\d{0,2})S?)"
    );

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(duration.toString());
    String formattedDuration = "";

    while (matcher.find()) {
      try {
        if (matcher.group("hour") != null) {
          formattedDuration += formatDigit(matcher.group("hour"));
          formattedDuration += ":";
        }

        if (matcher.group("minute") != null) {
          // We format the minutes only if there are
          // hours in the duration
          formattedDuration += (formattedDuration != "")
              ? formatDigit(matcher.group("minute"))
              : matcher.group("minute");
          formattedDuration += ":";
        }

        if (matcher.group("second") != null) {
          formattedDuration += formatDigit(matcher.group("second"));
        }
      } catch (IllegalStateException e) {
        formatDurationLogger.error("Error while formatting music {} duration", e);
        return duration.toString();
      }
    }

    return formattedDuration;
  }

  /**
   * Format a number adding a "0" before if it is a digit.
   * Example : "3" in input will return "03".
   * Especially useful when displaying durations.
   */
  private static String formatDigit(String toFormat) {
    String formatted = toFormat;

    // If toFormat is a digit, we want to add a "0" before
    // it for better display
    if (Integer.parseInt(toFormat) < 10) {
      formatted = "0" + formatted;
    }

    return formatted;
  }
}
