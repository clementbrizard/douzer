package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.apache.logging.log4j.core.util.ArrayUtils;

/**
 * Side view of my playlists.
 */
public class MyPlaylistsController implements Controller {

  private MainController mainController;
  @FXML
  private ListView<String> lvPlaylists;

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
  }

  public MainController getMainController() {
    return mainController;
  }

  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  public void init() {
    this.updatePlaylists();
    lvPlaylists.getSelectionModel().select(0);

    lvPlaylists.getSelectionModel().selectedItemProperty()
        .addListener(new ChangeListener<String>() {
          @Override
          public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            // Your action here
            MyPlaylistsController.this.getMainController().getCentralFrameController()
                .setCentralContentMyMusics();

            if (lvPlaylists.getSelectionModel().getSelectedIndices().get(0) == 0) {
              // Reset central frame to myMusics
              MyPlaylistsController.this.getMainController().getCentralFrameController().getMyMusicsController().reset();
            } else {

              MyPlaylistsController.this.getMainController().getCentralFrameController().getMyMusicsController().setTopText(newValue);

            }
          }
        });
  }

  /**
   * Creates the empty "Mes morceaux" playlist and adds all playlist names from Data.
   */
  private void updatePlaylists() {
    ArrayList<String> mesMorceaux = new ArrayList();
    mesMorceaux.add("Mes morceaux");
    mesMorceaux.add("coucou");
  /*
    ArrayList<String> playlistsFromData = this.getMainController().getApplication().getPlaylists()
                                              .map(p -> p.getName())
                                              .collect(Collectors.toList());

    List<String> totalPlaylists = new ArrayList();
    totalPlaylists.addAll(mesMorceaux);
    totalPlaylists.addAll(playlistsFromData);
*/
    lvPlaylists.setItems(FXCollections.observableList(mesMorceaux));
  }
}
