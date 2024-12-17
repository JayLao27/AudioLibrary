package AudioController.controllers;

import AudioController.DatabaseConnection;
import AudioController.MouseEffects;
import AudioController.ResourceLoader;
import AudioController.SceneWithHomeContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenrePageScene implements SceneWithHomeContext {

    private HomeScene homeScene;

    private int genreID;

    @Override
    public void setHomeScene(HomeScene homeScene) {
        this.homeScene = homeScene;
    }

    @FXML
    private Label genreLabel;
    @FXML
    private VBox songlistVBox;

    public void setGenreID(int genreID) {
        this.genreID = genreID;
        System.out.println("Initializing with genre ID: " + genreID);
        loadSongList(genreID);
        genreLabel.setText(ResourceLoader.getGenreName(genreID) + " Songs");
    }

    void loadSongList(int genreID) {
        songlistVBox.getChildren().clear();
        String query = "SELECT audioID FROM Audio WHERE genreID = ? ORDER BY audioID ASC";

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the genreID to filter songs by genre
            preparedStatement.setInt(1, genreID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    int audioID = rs.getInt("audioID");

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXMLs/songlisttemplateScene.fxml"));
                    AnchorPane songList = fxmlLoader.load();

                    SongListTemplateScene controller = fxmlLoader.getController();
                    controller.setAudioID(audioID);
                    controller.setGenrePageScene(this);

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

}
