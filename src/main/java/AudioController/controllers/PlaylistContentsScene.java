package AudioController.controllers;

import AudioController.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for managing the contents of a playlist in the audio library.
 * This scene allows the user to add, remove, and update songs in a playlist,
 * as well as update the playlist's name and image.
 */
public class PlaylistContentsScene implements SceneWithHomeContext {
    private HomeScene homeScene;

    /**
     * Sets the {@link HomeScene} instance to enable navigation from the artist page.
     *
     * @param homeScene The home scene instance for navigation purposes.
     */
    @Override
    public void setHomeScene(HomeScene homeScene) {
        this.homeScene = homeScene;
    }

    @FXML
    private Button addSongsButton;
    @FXML
    private ImageView playlistImage, addImage;
    @FXML
    private Label playlistNameLabel;
    @FXML
    private TextField playlistNameField;
    @FXML
    private VBox playlistVBox;

    private int playlistID;

    /**
     * Initializes the playlist with the given playlist ID.
     * Loads the song list, playlist name, and playlist image.
     *
     * @param playlistID the ID of the playlist to initialize
     */
    public void setPlaylistID(int playlistID) {
        this.playlistID = playlistID;
        System.out.println("Initializing with artist ID: " + playlistID);
        loadSongList();
        String playlistName = ResourceLoader.getPlaylistName(playlistID);
        playlistNameLabel.setText(playlistName);
        loadPlaylistImage();
    }

    /**
     * Initializes the scene elements, including handling double-click events for editing the playlist name.
     */
    @FXML
    public void initialize() {
        addImage.setOpacity(0);
        MouseEffects.addMouseEffects(addSongsButton);

        playlistNameField.setVisible(false);

        playlistNameLabel.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                playlistNameField.setText( playlistNameLabel.getText());
                playlistNameField.setVisible(true);
                playlistNameLabel.setVisible(false);
                playlistNameField.requestFocus();
            }
        });

        playlistNameField.setOnAction(event -> saveChanges());

        playlistNameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                saveChanges();
            }
        });
    }

    public void reloadSongList() {
        loadSongList();
    }

    /**
     * Saves the changes made to the playlist name.
     */
    private void saveChanges() {
        String updatedName = playlistNameField.getText();

        playlistNameLabel.setText(updatedName);
        playlistNameLabel.setVisible(true);
        playlistNameField.setVisible(false);

        updatePlaylistName(updatedName);
    }

    /**
     * Updates the playlist name in the database.
     *
     * @param newName the new name for the playlist
     */
    private void updatePlaylistName(String newName) {
        String query = "UPDATE Playlists SET playlistName = ? WHERE playlistID = ?";

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the parameters for the prepared statement
            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, playlistID);

            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Playlist name updated successfully.");
            } else {
                System.out.println("Failed to update playlist name.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating playlist name in the database.");
        }
    }

    /**
     * Updates the playlist image file path in the database.
     *
     * @param filePath the absolute file path of the new playlist image
     */
    private void updatePlaylistImagePath(String filePath) {
        // Use the filePath to update the database with the new path
        String query = "UPDATE Playlists SET playlistImageFile = ? WHERE playlistID = ?";

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, filePath);  // Set the file path
            preparedStatement.setInt(2, playlistID);   // Set the current playlist ID

            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Image path updated successfully.");
            } else {
                System.out.println("Failed to update image path.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating image path in database.");
        }
    }

    /**
     * Loads the list of songs for the current playlist and adds them to the VBox.
     */
    private void loadSongList() {
        playlistVBox.getChildren().clear();

        String query = "SELECT audioID " +
                "FROM PlaylistAudio " +
                "WHERE playlistID = ? " +
                "ORDER BY audioID ASC";

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, playlistID); // Use playlistID to filter

            List<Integer> audioQueue = new ArrayList<>();

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    int audioID = rs.getInt("audioID");

                    audioQueue.add(audioID);

                    // Load the song list template
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXMLs/playlisttemplateScene.fxml"));
                    AnchorPane songList = fxmlLoader.load();

                    PlaylistTemplateScene controller = fxmlLoader.getController();
                    controller.setAudioID(audioID);
                    controller.setPlaylistID(playlistID);
                    controller.setQueue(audioQueue);
                    controller.setPlaylistContentsScene(this);

                    // Add the song to the VBox
                    playlistVBox.getChildren().add(songList);
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the playlist image from the file system and sets it to the ImageView.
     * If the image cannot be loaded, a default image is displayed.
     */
    private void loadPlaylistImage() {

        String imagePath = ResourceLoader.getPlaylistImagePath(playlistID);
        try {
            if (imagePath != null && !imagePath.isEmpty()) {
                File imageFile = new File(imagePath);

                if (imageFile.exists() && imageFile.isFile()) {
                    Image image = new Image(imageFile.toURI().toString());

                    if (!image.isError()) {
                        playlistImage.setImage(image);
                        return;
                    }
                }
            }

            // If we reach here, load default image
            loadDefaultImage();
        } catch (Exception e) {
            e.printStackTrace();
            loadDefaultImage();
        }
    }

    /**
     * Loads a default image and sets it to the playlist image view.
     */
    private void loadDefaultImage() {
        try {
            // More robust path construction
            String defaultImagePath = System.getProperty("user.dir") +
                    File.separator + "src" +
                    File.separator + "main" +
                    File.separator + "resources" +
                    File.separator + "ProjectImages" +
                    File.separator + "Vector.png";

            File defaultImageFile = new File(defaultImagePath);

            if (defaultImageFile.exists()) {
                // Use URL encoding to handle potential spaces in path
                Image defaultImage = new Image(defaultImageFile.toURI().toString());

                if (!defaultImage.isError()) {
                    playlistImage.setImage(defaultImage);
                } else {
                    System.out.println("Error loading default image");
                    playlistImage.setImage(null);
                }
            } else {
                System.out.println("Default image file not found at: " + defaultImagePath);
                playlistImage.setImage(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            playlistImage.setImage(null);
        }
    }

    /**
     * Opens a file chooser to allow the user to select a new playlist image.
     * The image file path is updated in the database.
     *
     * @param event the mouse event triggering this method
     */
    @FXML
    private void handleAddImageClicked(MouseEvent event) {
        // Create a new FileChooser instance
        FileChooser fileChooser = new FileChooser();

        // Set the extension filters for the file chooser (only allow image files)
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(imageFilter);

        // Show the open file dialog
        Stage stage = (Stage) playlistImage.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        // Check if the file is valid (not null)
        if (selectedFile != null) {
            // Update the ImageView with the selected image
            Image image = new Image(selectedFile.toURI().toString());
            playlistImage.setImage(image);

            // Get the absolute path of the selected file
            String filePath = selectedFile.getAbsolutePath();

            // Update the database with the absolute path
            updatePlaylistImagePath(filePath);
        } else {
            System.out.println("No file selected.");
        }
    }

    @FXML
    private void handleAddImageEntered(MouseEvent event) {
        addImage.setOpacity(1);
    }

    @FXML
    private void handleAddImageExited(MouseEvent event) {
        addImage.setOpacity(0);
    }

    @FXML
    private void handleAddImagePressed(MouseEvent event) {
        addImage.setImage(new Image(getClass().getResource("/ProjectImages/AddImageClicked.png").toExternalForm()));

    }

    @FXML
    private void handleAddImageReleased(MouseEvent event) {
        addImage.setImage(new Image(getClass().getResource("/ProjectImages/AddImage.png").toExternalForm()));

    }

    /**
     * Opens the scene to add new songs to the playlist.
     *
     * @param event the mouse event triggering this method
     */
    @FXML
    private void handleAddSongsClicked(MouseEvent event) {
        homeScene.loadPlaylistScene("/FXMLs/libraryselectorScene.fxml", playlistID);
    }

    /**
     * Prompts the user to confirm before deleting the playlist.
     * If confirmed, the playlist is deleted from the database.
     *
     * @param event the mouse event triggering this method
     */
    @FXML
    private void handleDeletePlaylistClicked(MouseEvent event) {
        // Confirmation prompt
        boolean confirmDelete = showConfirmationDialog(
                "Delete Playlist",
                "Are you sure you want to delete this playlist?",
                "This action cannot be undone."
        );

        if (confirmDelete) {
            // Delete the playlist from the database
            deletePlaylistFromDatabase();

            // Redirect to the PlaylistScene
            homeScene.loadPlaylistScene("/FXMLs/playlistScene.fxml", playlistID);
        }
    }

    /**
     * Displays a confirmation dialog with the given title, header, and content.
     *
     * @param title the title of the dialog
     * @param header the header text of the dialog
     * @param content the content text of the dialog
     * @return true if the user confirms the action, false otherwise
     */
    private boolean showConfirmationDialog(String title, String header, String content) {
        // Use JavaFX Alert for the confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        // Wait for user response
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    /**
     * Deletes the playlist from the database.
     */
    private void deletePlaylistFromDatabase() {
        String query = "DELETE FROM Playlists WHERE playlistID = ?";

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, playlistID);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Playlist deleted successfully.");
            } else {
                System.out.println("Failed to delete the playlist.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting playlist from database.");
        }
    }

}
