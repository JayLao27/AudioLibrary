package AudioController.controllers;

import AudioController.DatabaseConnection;
import AudioController.MouseEffects;
import AudioController.SceneWithHomeContext; // Make sure to import this if needed
import AudioController.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.*;

/**
 * Controller for managing the playlist scene in the application.
 * Handles displaying the user's playlists and adding new playlists.
 * Provides user interaction for viewing and creating playlists.
 * Implements the {@link SceneWithHomeContext} interface for managing navigation.
 */
public class PlaylistScene implements SceneWithHomeContext {

    @FXML
    private FlowPane playlistsFlowPane;

    private HomeScene homeScene; // Reference to the HomeScene

    /**
     * Sets the {@link HomeScene} instance to enable navigation from the artist page.
     *
     * @param homeScene The home scene instance for navigation purposes.
     */
    public void setHomeScene(HomeScene homeScene) {
        this.homeScene = homeScene;
    }

    /**
     * Initializes the PlaylistScene by retrieving the user's playlists
     * from the database and displaying them in the FlowPane.
     */
    @FXML
    private void initialize() {
        // Retrieve the userID from the UserSession
        int userID = UserSession.getInstance().getUserID();

        String query = "SELECT playlistID FROM Playlists WHERE UserID = ? ORDER BY playlistID ASC";

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the userID in the PreparedStatement
            preparedStatement.setInt(1, userID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    int playlistID = rs.getInt("playlistID");

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXMLs/playlistcardtemplateScene.fxml"));
                    AnchorPane playlistCard = fxmlLoader.load();

                    PlaylistCardTemplateScene controller = fxmlLoader.getController();
                    controller.setPlaylistID(playlistID);

                    MouseEffects.addMouseEffects(playlistCard);

                    playlistCard.setOnMouseClicked(event -> {
                        System.out.println("Redirecting to song...");

                        if (homeScene != null) {
                            homeScene.loadPlaylistScene("/FXMLs/playlistcontentsScene.fxml", playlistID);
                        } else {
                            System.out.println("HomeScene is null!");
                        }
                    });

                    playlistsFlowPane.getChildren().add(playlistCard);
                }
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the click event to create a new playlist.
     * Adds the new playlist to the database and loads the playlist contents scene.
     *
     * @param event The MouseEvent triggered by clicking the button.
     */
    @FXML
    private void onMakeplaylistCardClicked(MouseEvent event) {
        System.out.println("Button clicked!");

        // Step 1: Retrieve current user ID
        int userID = UserSession.getInstance().getUserID();
        System.out.println("Current User ID: " + userID);

        // Step 2: Define database query
        String createPlaylistQuery = "INSERT INTO Playlists (playlistName, playlistImageFile, userID) VALUES (?, ?, ?)";

        // Step 3: Database interaction using DatabaseConnection
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(createPlaylistQuery, Statement.RETURN_GENERATED_KEYS)) {

            // Example values for playlist name and image
            String playlistName = "My New Playlist";
            String playlistImageFile = "Vector.png";

            // Set parameters for the PreparedStatement
            pstmt.setString(1, playlistName);
            pstmt.setString(2, playlistImageFile);
            pstmt.setInt(3, userID);

            // Execute the query
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                // Step 4: Get the generated playlistID
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int playlistID = generatedKeys.getInt(1);
                        System.out.println("New playlist ID: " + playlistID);

                        // Step 5: Load the new scene
                        if (homeScene != null) {
                            homeScene.loadPlaylistScene("/FXMLs/playlistcontentsScene.fxml", playlistID);
                        }
                    } else {
                        System.out.println("No playlist ID obtained.");
                    }
                }
            } else {
                System.out.println("Playlist creation failed.");
            }

        } catch (SQLException e) {
            System.err.println("Error while creating playlist: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Playlist Card Pane UX
    @FXML
    private void handlePaneEntered(MouseEvent event) {
        ((Pane) event.getSource()).setStyle("-fx-background-color: #d3d3d32b;");
    }

    @FXML
    private void handlePaneExited(MouseEvent event) {
        ((Pane) event.getSource()).setStyle("-fx-background-color: transparent;");
    }

    @FXML
    private void handlePanePressed(MouseEvent event) {
        Pane pressedPane = (Pane) event.getSource();
        pressedPane.setStyle("-fx-background-color: #dfdfdf3b;");
    }

    @FXML
    private void handlePaneReleased(MouseEvent event) {
        Pane releasedPane = (Pane) event.getSource();
        releasedPane.setStyle("-fx-background-color: #d3d3d32b;");
    }


}