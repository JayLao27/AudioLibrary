package AudioController.controllers;

import AudioController.AudioPlayer;
import AudioController.DatabaseConnection;
import AudioController.SceneWithHomeContext;
import AudioController.UserSession;
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

public class LibraryScene implements SceneWithHomeContext {

    private HomeScene homeScene;

    @FXML
    private FlowPane libraryFlowPane;

    @Override
    public void setHomeScene(HomeScene homeScene) {
        this.homeScene = homeScene;
        System.out.println("HomeScene set on LibraryScene: " + (homeScene != null));
    }

    @FXML
    private void initialize() {
        loadSongList();
    }

    private void loadSongList() {
        // Get the current userID from the UserSession instance
        int userID = UserSession.getInstance().getUserID();

        // Modify the query to fetch audioIDs from LibraryAudio for the given userID
        String query = "SELECT audioID FROM LibraryAudio WHERE userID = ? ORDER BY audioID ASC";

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the userID parameter in the query
            preparedStatement.setInt(1, userID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    int audioID = rs.getInt("audioID");

                    // Load the song list template
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXMLs/librarylisttemplateScene.fxml"));
                    AnchorPane songList = fxmlLoader.load();

                    LibrarylisttemplateScene controller = fxmlLoader.getController();
                    System.out.println("Controller initialized: " + controller);
                    controller.setAudioID(audioID);

                    addMouseEffects(songList);

                    songList.setOnMouseClicked(event -> {
                        System.out.println("Redirecting to song...");

                        if (homeScene != null) {
                            AudioPlayer.playAudio(audioID);
                            homeScene.loadCurrentSong(audioID);
                        } else {
                            System.out.println("HomeScene is null!");
                        }
                    });

                    // Add the song to the flow pane
                    libraryFlowPane.getChildren().add(songList);
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
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
