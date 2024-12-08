package AudioController.controllers;

import AudioController.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibrarySelectorScene implements SceneWithHomeContext {
    private HomeScene homeScene;

    @FXML
    private FlowPane libraryFlowPane;

    private int playlistID;

    public void setPlaylistID(int playlistID) {
        this.playlistID = playlistID;
        reloadList();
    }

    @Override
    public void setHomeScene(HomeScene homeScene) {
        this.homeScene = homeScene;
        System.out.println("HomeScene set on LibraryScene: " + (homeScene != null));
    }

    @FXML
    private void initialize() {
        reloadList(); // Load songs on initialization
    }

    private void reloadSongList() {
        libraryFlowPane.getChildren().clear();
        int userID = UserSession.getInstance().getUserID();

        // SQL query to fetch songs in LibraryAudio that are not in PlaylistAudio
        String query = "SELECT audioID FROM LibraryAudio " +
                "WHERE userID = ? AND audioID NOT IN (SELECT audioID FROM PlaylistAudio WHERE playlistID = ?) " +
                "ORDER BY audioID ASC";

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the userID and playlistID parameters in the query
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, playlistID);

            // Queue for storing audio IDs
            List<Integer> audioQueue = new ArrayList<>();

            try (ResultSet rs = preparedStatement.executeQuery()) {
                libraryFlowPane.getChildren().clear(); // Clear the previous songs in the FlowPane

                while (rs.next()) {
                    int audioID = rs.getInt("audioID");
                    audioQueue.add(audioID); // Add audioID to the queue

                    // Load the song list template
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXMLs/librarylistselectortemplateScene.fxml"));
                    AnchorPane songList = fxmlLoader.load();

                    LibraryListSelectorTemplateScene controller = fxmlLoader.getController();
                    System.out.println("Controller initialized: " + controller);
                    controller.setAudioAndPlaylistID(audioID, playlistID, this); // Pass the current scene reference

                    MouseEffects.addMouseEffects(songList);

                    songList.setOnMouseClicked(event -> {
                        System.out.println("Redirecting to song...");

                        if (homeScene != null) {
                            // Play the clicked song
                            AudioPlayer.getInstance().setQueue(audioQueue); // Set the entire queue
                            AudioPlayer.getInstance().playAudio(audioID); // Start playback with the clicked song
                            homeScene.loadCurrentSong(audioID); // Update homeScene with the current song
                        } else {
                            System.out.println("HomeScene is null!");
                        }
                    });

                    // Add the song to the flow pane
                    libraryFlowPane.getChildren().add(songList);
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadList() {
        reloadSongList();
    }
}
