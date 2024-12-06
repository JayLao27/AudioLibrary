package AudioController.controllers;

import AudioController.DatabaseConnection;
import AudioController.ResourceLoader;
import AudioController.SceneWithHomeContext;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArtistpageScene implements SceneWithHomeContext {

    private HomeScene homeScene;

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

    public void setArtistID(int artistID) {
        this.artistID = artistID;
        System.out.println("Initializing with artist ID: " + artistID);
        loadArtistDetails();
        loadSongList();
    }

    @FXML
    public void initialize() {}

    private void loadSongList() {
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

                    addMouseEffects(songList);

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

    private void addMouseEffects(Pane songList) {
        // Mouse enter: scale up and change background
        songList.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), songList);
            scaleTransition.setToX(1.05);
            scaleTransition.setToY(1.05);
            scaleTransition.play();

            songList.setStyle("-fx-background-color: #d3d3d32b;");
        });

        songList.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), songList);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();

            songList.setStyle("-fx-background-color: #cfcfcf1b");
        });

        // Mouse pressed: scale slightly down and darken background
        songList.setOnMousePressed(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), songList);
            scaleTransition.setToX(1.04);
            scaleTransition.setToY(1.04);
            scaleTransition.play();

            // Darken background color
            songList.setStyle("-fx-background-color: #dfdfdf2b;");
        });

        songList.setOnMouseReleased(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), songList);
            scaleTransition.setToX(1.05);
            scaleTransition.setToY(1.05);
            scaleTransition.play();

            // Restore hover background color
            songList.setStyle("-fx-background-color: #d3d3d32b;");
        });
    }
}
