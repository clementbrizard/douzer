package controllers;

import datamodel.Playlist;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.core.util.ArrayUtils;

/**
 * Side view of my playlists.
 */
public class MyPlaylistsController implements Controller {

  private MainController mainController;
  @FXML
  private ListView<String> lvPlaylists;

  private ContextMenu contextMenu;

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

  /**
   * Shows the existing playlists, adds contextMenu.
   */
  public void init() {
    this.updatePlaylists();
    this.resetSelection();

    // Add context menu
    contextMenu = new ContextMenu();

    MenuItem menuDeletePlaylist = new MenuItem("Supprimer");
    menuDeletePlaylist.setOnAction(event -> {
        String playlistToDeleteName = MyPlaylistsController.this.getSelectedPlaylist();
        Playlist playlistToDelete = MyPlaylistsController.this.mainController
            .getApplication().getIhmCore().getDataForIhm().getCurrentUser()
            .getPlaylistByName(playlistToDeleteName);

        MyPlaylistsController.this.mainController.getApplication().getIhmCore()
            .getDataForIhm().getCurrentUser().removePlaylist(playlistToDelete);

        MyPlaylistsController.this.updatePlaylists();
        MyPlaylistsController.this.resetSelection();
        MyPlaylistsController.this.mainController.getCentralFrameController()
            .getMyMusicsController().reset();
    });

    contextMenu.getItems().add(menuDeletePlaylist);
  }

  public int getSelectedIndex() {
    return lvPlaylists.getSelectionModel().getSelectedIndex();
  }

  public void resetSelection() {
    lvPlaylists.getSelectionModel().select(0);
  }

  public String getSelectedPlaylist() {
    return lvPlaylists.getSelectionModel().getSelectedItem();
  }

  @FXML
  private void addPlaylist() {
    TextInputDialog dialog = new TextInputDialog("");

    dialog.setTitle("Nom de la playlist");
    dialog.setHeaderText("Enter le nom de la playlist :");
    dialog.setContentText("");

    Optional<String> result = dialog.showAndWait();

    result.ifPresent(name -> {
      name = name.trim();
      if(!name.isEmpty()) {
        this.mainController.getApplication().getIhmCore().getDataForIhm().getCurrentUser().addPlaylist(name);
      }
    });

    this.updatePlaylists();
  }

  @FXML
  private void handleListClickedEvent(MouseEvent click) {

    if (click.getButton().equals(MouseButton.PRIMARY)) {
      MyPlaylistsController.this.mainController.getCentralFrameController()
          .setCentralContentMyMusics();

      if (lvPlaylists.getSelectionModel().getSelectedIndices().get(0) == 0) {
        // Reset central frame to myMusics
        MyPlaylistsController.this.mainController.getCentralFrameController().getMyMusicsController().reset();
      } else {

        MyPlaylistsController.this.mainController.getCentralFrameController().getMyMusicsController()
            .showPlaylist(MyPlaylistsController.this.getSelectedPlaylist());
      }
    } else if (click.getButton().equals(MouseButton.SECONDARY)) {
        contextMenu.show(lvPlaylists, click.getScreenX(), click.getScreenY());
    }
  }
  /**
   * Creates the empty "Mes morceaux" playlist and adds all playlist names from Data.
   */
  private void updatePlaylists() {
    List<String> mesMorceaux = new ArrayList();
    mesMorceaux.add("Mes morceaux");

    List<String> playlistsFromData = this.mainController.getApplication().getIhmCore().getDataForIhm().getCurrentUser().getPlaylists().stream().map(p -> p.getName()).collect(Collectors.toList());

    List<String> totalPlaylists = new ArrayList();
    totalPlaylists.addAll(mesMorceaux);
    totalPlaylists.addAll(playlistsFromData);

    lvPlaylists.setItems(FXCollections.observableList(totalPlaylists));
  }


}
