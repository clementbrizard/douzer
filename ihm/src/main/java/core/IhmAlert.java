package core;

import java.util.Optional;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Screen;

public class IhmAlert {

  /**
  *  Show a Warning Alert without Header Text.
  * @param title the title of the alert.
  * @param text the text of the alert.
  * @param kind the kind of alert take value : "warning" or "information" or "critical".
  */
  public static void showAlert(String title, String text, String kind) {
    
    Alert alert = null;

    switch (kind) {
      case "warning":
        alert = new Alert(Alert.AlertType.WARNING);
        break;
      case "information":
        alert = new Alert(Alert.AlertType.INFORMATION);
        break;
      case "critical":
        alert = new Alert(Alert.AlertType.ERROR);
        break;
      default:
        alert = new Alert(Alert.AlertType.INFORMATION);
    }
    
    if (alert != null) {
      alert.setTitle(kind + ": " + title);
      alert.setHeaderText(null);
      alert.setContentText(text);
      alert.showAndWait();
      
      Rectangle2D primScreenBounds = Screen.getPrimary().getBounds();
      alert.setX((primScreenBounds.getWidth() - alert.getWidth()) / 2);
      alert.setY((primScreenBounds.getHeight() - alert.getHeight()) / 2);
    }
  }

  /**
  * show confirmation return a boolean.
  * @param title the title of the confirmation alert.
  * @param text the text of the confirmation alert.
  * @return the user choice.
  */
  public static boolean showConfirmation(String title, String text) {

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(title);
    alert.setHeaderText(text);
    alert.setContentText(null);

    // option != null.
    Optional<ButtonType> option = alert.showAndWait();
    
    Rectangle2D primScreenBounds = Screen.getPrimary().getBounds();
    alert.setX((primScreenBounds.getWidth() - alert.getWidth()) / 2);
    alert.setY((primScreenBounds.getHeight() - alert.getHeight()) / 2);

    if (option.get() == ButtonType.OK) { 
      return true; 
    }
    return false;
  }
}