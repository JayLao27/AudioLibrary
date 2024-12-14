package AudioController.controllers;

import AudioController.DatabaseConnection;
import AudioController.ResourceLoader;
import AudioController.SceneWithHomeContext;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controller for the library list selector template scene. This scene displays a song's name and cover image.
 * The user can add a song to a playlist using the provided functionality.
 */
public class LibraryListSelectorTemplateScene {

    @FXML
    private Label songNameLabel;
    @FXML
    private ImageView songCoverImage;

    private int audioID;
    private int playlistID;
    private LibrarySelectorScene librarySelectorScene; // Reference to LibrarySelectorScene

    /**
     * Sets the audio and playlist IDs for the current scene, and loads the song details.
     *
     * @param audioID the ID of the audio to be displayed
     * @param playlistID the ID of the playlist where the audio can be added
     * @param librarySelectorScene the scene from which the user navigates to reload the song list after adding
     */
    public void setAudioAndPlaylistID(int audioID, int playlistID, LibrarySelectorScene librarySelectorScene) {
        this.audioID = audioID;
        this.playlistID = playlistID;
        this.librarySelectorScene = librarySelectorScene;
        System.out.println("Initializing with audio ID: " + audioID + " and playlist ID: " + playlistID);
        loadAudioDetails();
    }

    /**
     * Loads the details of the audio, such as the song name and cover image.
     * The song name is fetched using the `ResourceLoader`, and the cover image is set if available.
     */
    private void loadAudioDetails() {
        String songName = ResourceLoader.getAudioName(audioID);
        songNameLabel.setText(songName);

        String artistImagePath = ResourceLoader.getAudioImagePath(audioID);
        if (artistImagePath != null) {
            try {
                Image image = new Image(getClass().getResourceAsStream(artistImagePath));
                if (!image.isError()) {
                    songCoverImage.setImage(image);
                } else {
                    System.out.println("Error loading image: " + artistImagePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception loading image: " + artistImagePath);
            }
        } else {
            System.out.println("Artist image path is null for audioID: " + audioID);
        }
    }

    /**
     * Handles the click event on the "Add" button. This method checks if both the playlist and audio exist
     * in the database, and if so, adds the audio to the playlist.
     *
     * @param event the mouse event triggered when the button is clicked
     */
    @FXML
    private void handleAddButtonClicked(MouseEvent event) {
        System.out.println("Add button clicked!");

        // Step 1: Check if the playlistID exists
        String checkPlaylistQuery = "SELECT COUNT(*) FROM Playlists WHERE playlistID = ?";
        String checkAudioQuery = "SELECT COUNT(*) FROM Audio WHERE audioID = ?";

        try (Connection conn = new DatabaseConnection().getConnection();
             PreparedStatement pstmtPlaylist = conn.prepareStatement(checkPlaylistQuery);
             PreparedStatement pstmtAudio = conn.prepareStatement(checkAudioQuery)) {

            System.out.println("audioID: " + audioID);
            System.out.println("playlistID: " + playlistID);

            // Set the parameters for the check queries
            pstmtPlaylist.setInt(1, playlistID);
            pstmtAudio.setInt(1, audioID);

            // Execute the check queries
            ResultSet rsPlaylist = pstmtPlaylist.executeQuery();
            ResultSet rsAudio = pstmtAudio.executeQuery();

            // Check if the playlist and audio exist
            rsPlaylist.next();
            rsAudio.next();

            if (rsPlaylist.getInt(1) == 0) {
                System.out.println("Playlist does not exist.");
                return;
            }

            if (rsAudio.getInt(1) == 0) {
                System.out.println("Audio does not exist.");
                return;
            }

            // Step 2: Add the audio to the PlaylistAudio table if both exist
            String addAudioToPlaylistQuery = "INSERT INTO PlaylistAudio (playlistID, audioID) VALUES (?, ?)";
            try (PreparedStatement pstmtInsert = conn.prepareStatement(addAudioToPlaylistQuery)) {

                pstmtInsert.setInt(1, playlistID);
                pstmtInsert.setInt(2, audioID);

                int rowsInserted = pstmtInsert.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Audio added to the playlist successfully!");
                    librarySelectorScene.reloadList(); // Refresh the song list
                } else {
                    System.out.println("Failed to add audio to the playlist.");
                }

            } catch (SQLException e) {
                System.err.println("Error while adding audio to playlist: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.err.println("Error while checking for existence of playlist/audio: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
