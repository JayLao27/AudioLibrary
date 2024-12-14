package AudioController.controllers;

import AudioController.*;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the library scene in the audio library application.
 * This scene displays a list of songs that the user has in their library.
 * Implements the {@link SceneWithHomeContext} interface for managing navigation.
 */
public class LibraryScene implements SceneWithHomeContext {

    private HomeScene homeScene;

    @FXML
    private FlowPane libraryFlowPane;

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
     * Initializes the scene by loading the song list.
     */
    @FXML
    private void initialize() {
        loadSongList();
    }

    /**
     * Loads the list of songs from the user's library and displays them in the FlowPane.
     * The songs are fetched from the LibraryAudio table in the database for the current user.
     * Each song is loaded using the LibraryListTemplateScene template.
     */
    private void loadSongList() {
        // Get the current userID from the UserSession instance
        int userID = UserSession.getInstance().getUserID();

        // Modify the query to fetch audioIDs from LibraryAudio for the given userID
        String query = "SELECT audioID FROM LibraryAudio WHERE userID = ? ORDER BY audioID ASC";

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the userID parameter in the query
            preparedStatement.setInt(1, userID);

            // Queue for storing audio IDs
            List<Integer> audioQueue = new ArrayList<>();

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    int audioID = rs.getInt("audioID");
                    audioQueue.add(audioID); // Add audioID to the queue

                    // Load the song list template
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXMLs/librarylisttemplateScene.fxml"));
                    AnchorPane songList = fxmlLoader.load();

                    LibraryListTemplateScene controller = fxmlLoader.getController();
                    System.out.println("Controller initialized: " + controller);
                    controller.setAudioID(audioID);
                    controller.setQueue(audioQueue);

                    // Add the song to the flow pane
                    libraryFlowPane.getChildren().add(songList);
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
