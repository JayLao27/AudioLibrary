package AudioController.controllers;

import AudioController.DatabaseConnection;
import AudioController.ResourceLoader;
import AudioController.SceneWithHomeContext;
import AudioController.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controller class for the SongListTemplateScene, responsible for displaying
 * song details and managing interactions such as adding songs to the user's cart.
 * Implements the {@link SceneWithHomeContext} interface for managing navigation.
 */
public class SongListTemplateScene implements SceneWithHomeContext {

    private HomeScene homeScene;

    /**
     * Sets the {@link HomeScene} instance to enable navigation from the artist page.
     *
     * @param homeScene The home scene instance for navigation purposes.
     */
    public void setHomeScene(HomeScene homeScene) {
        this.homeScene = homeScene;
    }

    /**
     * Sets the {@link ArtistPageScene} for managing the artist page navigation.
     *
     * @param artistPageScene The instance of the ArtistPageScene.
     */
    private ArtistPageScene artistPageScene;
    public void setArtistPageScene(ArtistPageScene artistPageScene) {
        this.artistPageScene = artistPageScene;
    }

    /**
     * Sets the {@link AlbumPageScene} for managing the album page navigation.
     *
     * @param albumPageScene The instance of the AlbumPageScene.
     */
    private AlbumPageScene albumPageScene;
    public void setAlbumPageScene(AlbumPageScene albumPageScene) {
        this.albumPageScene = albumPageScene;
    }

    /**
     * Sets the {@link GenrePageScene} for managing the genre page navigation.
     *
     * @param genrePageScene The instance of the GenrePageScene.
     */
    private GenrePageScene genrePageScene;
    public void setGenrePageScene(GenrePageScene genrePageScene) {
        this.genrePageScene = genrePageScene;
    }

    /**
     * Sets the {@link SearchScene} for managing the search page navigation.
     *
     * @param searchScene The instance of the SearchScene.
     */
    private SearchScene searchScene;
    public void setSearchScene(SearchScene searchScene) {
        this.searchScene = searchScene;
    }

    @FXML
    private Button addtocartButton;
    @FXML
    private ImageView songCoverImage;
    @FXML
    private Label songNameLabel;

    private int audioID;

    /**
     * Sets the audio ID for the current song and loads its details.
     *
     * @param audioID The ID of the audio to load details for.
     */
    public void setAudioID(int audioID) {
        this.audioID = audioID;
        System.out.println("Initializing with audio ID: " + audioID);
        loadAudioDetails();
    }

    public void initialize() {}

    /**
     * Loads the details of the audio, including song name and cover image.
     * Also checks if the song is in the user's library or cart, and updates the button text accordingly.
     */
    private void loadAudioDetails() {
        int userID = UserSession.getInstance().getUserID();
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

        if (isSongInLibrary(userID, audioID)) {
            addtocartButton.setText("Already Bought");
            addtocartButton.setDisable(true);
            return;
        }

        if (isSongInCart(userID, audioID)) {
            addtocartButton.setText("In Cart");
            addtocartButton.setDisable(true);
            return;
        }

        Double price = ResourceLoader.getAudioPrice(audioID);
        if (price == 0) {
            addtocartButton.setText("Add to Cart (FREE)");
        } else if (price > 0) {
            addtocartButton.setText("Add to Cart (₱" + String.format("%.2f", price) + ")");
        } else {
            addtocartButton.setText("Add to Cart (₱ ???)");
        }
    }

    /**
     * Checks if the song is already in the user's library.
     *
     * @param userID The ID of the user.
     * @param audioID The ID of the audio.
     * @return {@code true} if the song is in the user's library, {@code false} otherwise.
     */
    private boolean isSongInLibrary(int userID, int audioID) {
        String query = "SELECT COUNT(*) FROM LibraryAudio WHERE userID = ? AND audioID = ?";

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, audioID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error checking if song is in library: " + e.getMessage());
        }
        return false;
    }

    /**
     * Checks if the song is already in the user's cart.
     *
     * @param userID The ID of the user.
     * @param audioID The ID of the audio.
     * @return {@code true} if the song is in the user's cart, {@code false} otherwise.
     */
    private boolean isSongInCart(int userID, int audioID) {
        String query = "SELECT COUNT(*) FROM CartAudio WHERE userID = ? AND audioID = ?";

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, audioID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error checking if song is in cart: " + e.getMessage());
        }
        return false;
    }

    /**
     * Adds the song to the user's cart.
     * This method inserts a record into the CartAudio table and updates the UI accordingly.
     */
    @FXML
    private void addToCart() {
        int userID = UserSession.getInstance().getUserID();

        String query = """
            INSERT INTO CartAudio (userID, audioID)
            VALUES (?, ?)
            """;

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the userID and audioID in the prepared statement
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, audioID);

            // Execute the query to insert the audioID into the CartAudio table
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Audio added to cart successfully!");
                if (artistPageScene != null) {
                    artistPageScene.loadSongList();
                }

                if (searchScene != null) {
                    searchScene.populateSearchResults(homeScene.getCurrentQuery());
                }

            } else {
                System.out.println("Failed to add audio to cart.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding audio to cart: " + e.getMessage());
        }
    }
}
