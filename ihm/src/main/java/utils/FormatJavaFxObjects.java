package utils;

import java.time.LocalDate;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

public abstract class FormatJavaFxObjects {
  /**
   * Called in initialization,
   * restricts the date picker.
   * @param datePicker date picker object
   * @param minDate local date object
   * @param maxDate local date object
   */
  public static void restrictDatePicker(DatePicker datePicker, LocalDate minDate, LocalDate maxDate) {
    final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
      @Override
      public DateCell call(final DatePicker datePicker) {
        return new DateCell() {
          @Override
          public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);
            if (item.isBefore(minDate)) {
              setDisable(true);
            } else if (item.isAfter(maxDate)) {
              setDisable(true);
            }
          }
        };
      }
    };
    datePicker.setDayCellFactory(dayCellFactory);
  }


}
