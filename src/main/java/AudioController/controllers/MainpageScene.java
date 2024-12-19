package AudioController.controllers;

import AudioController.DatabaseConnection;
import AudioController.MouseEffects;
import AudioController.SceneWithHomeContext;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controller for the main page scene. This class handles displaying song cards,
 * navigating to artist or song pages, and applying UX enhancements for shortcut buttons and images.
 * Implements the {@link SceneWithHomeContext} interface for managing navigation.
 */
public class MainpageScene implements SceneWithHomeContext {

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
    FlowPane recommendedSongContainerPane;

    @FXML
    FlowPane soundeffectContainerPane;
    /**
     * Initializes the main page by querying the database for song IDs and loading
     * corresponding song card templates. Each card is clickable and redirects
     * to the song page.
     */
    @FXML
    public void initialize() {
        // Query to get the first 8 audio IDs for recommended songs
        String recommendedQuery = "SELECT audioID FROM Audio ORDER BY audioID ASC LIMIT 8";

        // Query to get sound effects
        String soundEffectQuery = "SELECT audioID FROM Audio WHERE genreID = (SELECT genreID FROM Genre WHERE genreName = 'Sound Effect')";

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement recommendedStatement = connection.prepareStatement(recommendedQuery);
             ResultSet recommendedRs = recommendedStatement.executeQuery()) {

            // Load recommended songs
            while (recommendedRs.next()) {
                int audioID = recommendedRs.getInt("audioID");

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXMLs/songcardtemplateScene.fxml"));
                AnchorPane songCard = fxmlLoader.load();

                SongCardTemplateScene controller = fxmlLoader.getController();
                controller.setAudioID(audioID);

                MouseEffects.addMouseEffects(songCard);

                songCard.setOnMouseClicked(event -> {
                    System.out.println("Redirecting to song...");

                    if (homeScene != null) {
                        homeScene.loadSongScene("/FXMLs/songpageScene.fxml", audioID);
                    } else {
                        System.out.println("HomeScene is null!");
                    }
                });
                recommendedSongContainerPane.getChildren().add(songCard);
            }

            // Load sound effects
            try (PreparedStatement soundEffectStatement = connection.prepareStatement(soundEffectQuery);
                 ResultSet soundEffectRs = soundEffectStatement.executeQuery()) {

                while (soundEffectRs.next()) {
                    int audioID = soundEffectRs.getInt("audioID");

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXMLs/songcardtemplateScene.fxml"));
                    AnchorPane soundEffectCard = fxmlLoader.load();

                    SongCardTemplateScene controller = fxmlLoader.getController();
                    controller.setAudioID(audioID);

                    MouseEffects.addMouseEffects(soundEffectCard);

                    soundEffectCard.setOnMouseClicked(event -> {
                        System.out.println("Redirecting to sound effect...");

                        if (homeScene != null) {
                            homeScene.loadSongScene("/FXMLs/songpageScene.fxml", audioID);
                        } else {
                            System.out.println("HomeScene is null!");
                        }
                    });
                    soundeffectContainerPane.getChildren().add(soundEffectCard);
                }
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the click event on a shortcut image. It identifies the clicked
     * shortcut and redirects the user to the corresponding artist page.
     *
     * @param event The mouse event triggered when a shortcut image is clicked.
     */
    public void handleShortcutClicked(MouseEvent event) {
        ImageView clickedShortcut = (ImageView) event.getSource();
        String fxId = clickedShortcut.getId();
        int artistID;

        switch (fxId) {
            case "mozartShortcut":
                artistID = 1;
                break;
            case "tchaikovskyShortcut":
                artistID = 2;
                break;
            case "chopinShortcut":
                artistID = 3;
                break;
            case "rossiniShortcut":
                artistID = 4;
                break;
            case "alanwalkerShortcut":
                artistID = 5;
                break;
            case "beethovenShortcut":
                artistID = 6;
                break;
            default:
                System.out.println("Unknown ImageView clicked!");
                return;
        }

        try {
            if (homeScene != null) {
                homeScene.loadScene("/FXMLs/artistpageScene.fxml", artistID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Shortcut Buttons UX
    @FXML
    private void handleImageEntered(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(120), imageView);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.play();
    }

    @FXML
    private void handleImageExited(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(120), imageView);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();
    }

    @FXML
    private void handleImageReleased(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), imageView);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.play();
    }

    @FXML
    private void handleImagePressed(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), imageView);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);
        scaleTransition.play();
    }
}
