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
 * Controller for the artist page scene in the audio application.
 * This page displays the details of an artist, including their name, image,
 * and a list of songs they have produced.
 * The artist's details and song list are loaded dynamically from a database
 * based on the provided artist ID. The user can click on a song to navigate
 * to the individual song page.
 *
 * <p>This controller interacts with the {@link HomeScene} to enable navigation
 * between the artist page and the song pages. It also handles the loading of
 * artist data (name and image) and the list of songs associated with the artist.</p>
 *
 * <p>Key Responsibilities:</p>
 * <ul>
 *   <li>Loading artist details (name and image) from the database and displaying them on the page.</li>
 *   <li>Loading a list of songs associated with the artist and displaying them in a VBox.</li>
 *   <li>Handling navigation to a song page when a song is clicked.</li>
 * </ul>
 */
public class ArtistPageScene implements SceneWithHomeContext {

    private HomeScene homeScene;

    /**
     * Sets the {@link HomeScene} instance for this controller. This is used to
     * navigate back to the home scene or other pages from the artist page.
     *
     * @param homeScene The home scene instance to be set.
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
     * Loads the list of songs for the artist from the database and displays
     * them in the song list VBox. Each song is clickable and redirects
     * to the song page when clicked.
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
                    controller.setParentScene(this);

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
     * Loads the details (name and image) for the artist from the resource loader.
     * The artist's name is displayed in the artistNameLabel, and their image
     * is loaded into the artistImage view.
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
