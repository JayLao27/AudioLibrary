package AudioController.controllers;

import AudioController.DatabaseConnection;
import AudioController.MouseEffects;
import AudioController.ResourceLoader;
import AudioController.SceneWithHomeContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Controller for the artist page in the audio application.
 * <p>
 * This controller manages the display of an artist's information, including their name, image,
 * and a list of songs associated with the artist. It dynamically fetches artist data and songs
 * from a database and populates the corresponding UI components. The controller also supports
 * navigation to individual song pages upon user interaction.
 * </p>
 *
 * <h2>Key Responsibilities:</h2>
 * <ul>
 *   <li>Loading and displaying the artist's details (name and image).</li>
 *   <li>Fetching and rendering a list of songs associated with the artist.</li>
 *   <li>Enabling navigation to individual song pages when a song is clicked.</li>
 * </ul>
 *
 * Implements the {@link SceneWithHomeContext} interface for managing navigation.
 */
public class ArtistPageScene implements SceneWithHomeContext {

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
    private ImageView artistImage;
    @FXML
    private Label artistNameLabel;
    @FXML
    private VBox songlistVBox;

    private int artistID;

    /**
     * Sets the artist ID and loads the associated artist details and song list.
     *
     * @param artistID The unique identifier for the artist whose details are to be displayed.
     */
    public void setArtistID(int artistID) {
        this.artistID = artistID;
        System.out.println("Initializing with artist ID: " + artistID);
        loadArtistDetails();
        loadSongList();
    }

    /**
     * Initializes the controller. This method is called automatically
     * when the FXML file is loaded. In this case, there are no initializations
     * required at this stage.
     */
    @FXML
    public void initialize() {}

    /**
     * Loads and displays the list of songs associated with the artist.
     * <p>
     * The song data is fetched from the database using the artist's ID. Each song is
     * displayed as a clickable UI component, which navigates to the corresponding song page.
     */
    void loadSongList() {
        songlistVBox.getChildren().clear();
        String query = "SELECT audioID FROM Audio WHERE artistID = ? ORDER BY audioID ASC";

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, artistID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    int audioID = rs.getInt("audioID");

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXMLs/songlisttemplateScene.fxml"));
                    AnchorPane songList = fxmlLoader.load();

                    SongListTemplateScene controller = fxmlLoader.getController();
                    controller.setAudioID(audioID);
                    controller.setArtistPageScene(this);

                    MouseEffects.addMouseEffects(songList);

                    songList.setOnMouseClicked(event -> {
                        System.out.println("Redirecting to song...");

                        if (homeScene != null) {
                            homeScene.loadSongScene("/FXMLs/songpageScene.fxml", audioID);
                        } else {
                            System.out.println("HomeScene is null!");
                        }
                    });

                    songlistVBox.getChildren().add(songList);
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads and displays the artist's name and image.
     * <p>
     * The artist's name and image path are retrieved using {@link ResourceLoader}, and
     * the image is set in the {@link ImageView}. If the image cannot be loaded, an error is logged.
     */
    private void loadArtistDetails() {
        String artistName = ResourceLoader.getArtistName(artistID);
        artistNameLabel.setText(artistName);

        String artistImagePath = ResourceLoader.getArtistImagePath(artistID);
        if (artistImagePath != null) {
            try {
                Image image = new Image(getClass().getResourceAsStream(artistImagePath));
                if (!image.isError()) {
                    artistImage.setImage(image);
                } else {
                    System.out.println("Error loading image: " + artistImagePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception loading image: " + artistImagePath);
            }
        } else {
            System.out.println("Artist image path is null for artistID: " + artistID);
        }
    }
}
