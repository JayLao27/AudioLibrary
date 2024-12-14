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

/**
 * Controller for the Library Selector scene, responsible for displaying and managing songs
 * that can be added to a playlist. This class interacts with the database to retrieve songs
 * from the user's library that are not yet in the specified playlist, and displays them
 * dynamically in a flow pane.
 * <p>
 * It provides functionality to reload the song list whenever the scene is initialized or
 * the playlist is changed, and it handles the UI updates by loading FXML templates for
 * each song and applying mouse effects for interactivity.
 * </p>
 *
 * Implements the {@link SceneWithHomeContext} interface for managing navigation.
 */
public class LibrarySelectorScene implements SceneWithHomeContext {
    private HomeScene homeScene;

    @FXML
    private FlowPane libraryFlowPane;

    private int playlistID;

    /**
     * Sets the playlist ID for this scene and reloads the song list to reflect songs
     * that are not yet in the specified playlist.
     *
     * @param playlistID The ID of the playlist to filter songs.
     */
    public void setPlaylistID(int playlistID) {
        this.playlistID = playlistID;
        reloadList();
    }

    /**
     * Sets the {@link HomeScene} instance to enable navigation from the artist page.
     *
     * @param homeScene The home scene instance for navigation purposes.
     */
    @Override
    public void setHomeScene(HomeScene homeScene) {
        this.homeScene = homeScene;
        System.out.println("HomeScene set on LibraryScene: " + (homeScene != null));
    }

    /**
     * Initializes the scene by loading the song list from the database.
     * This method is called automatically when the scene is loaded.
     */
    @FXML
    private void initialize() {
        reloadList(); // Load songs on initialization
    }

    /**
     * Reloads the song list by querying the database for songs in the user's library
     * that are not in the current playlist. The results are displayed in the FlowPane.
     */
    private void reloadSongList() {
        libraryFlowPane.getChildren().clear(); // Clear previous songs

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

                    // Add the song to the flow pane
                    libraryFlowPane.getChildren().add(songList);
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reloads the song list. This method is a wrapper around {@link #reloadSongList()}
     * to be called from external sources for reloading the list of songs.
     */
    public void reloadList() {
        reloadSongList();
    }
}
