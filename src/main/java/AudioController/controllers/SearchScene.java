package AudioController.controllers;

import AudioController.DatabaseConnection;
import AudioController.MouseEffects;
import AudioController.SceneWithHomeContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controller class for the SearchScene, responsible for handling the search functionality
 * within the application. It populates the search results based on a query and displays
 * the results dynamically in a FlowPane.
 */
public class SearchScene implements SceneWithHomeContext {

    private HomeScene homeScene;

    @Override
    public void setHomeScene(HomeScene homeScene) {
        this.homeScene = homeScene;
    }

    @FXML
    private FlowPane searchFlowPane;
    @FXML
    private Label resultsLabel;

    /**
     * Populates the search results dynamically based on the query string and mode.
     * Fetches matching songs from the database and displays them in the FlowPane.
     *
     * @param query The search query string.
     * @param mode  The search mode (TITLE, GENRE, ARTIST, ALBUM).
     */
    public void populateSearchResults(String query, String mode) {
        searchFlowPane.getChildren().clear();

        String sql = getSQLForMode(mode);
        if (sql == null) {
            resultsLabel.setText("Invalid search mode.");
            return;
        }

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            int resultCount = 0;
            preparedStatement.setString(1, "%" + query + "%");

            try (ResultSet rs = preparedStatement.executeQuery()) {

                while (rs.next()) {
                    resultCount++;

                    int audioID = rs.getInt("audioID");

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXMLs/songlisttemplateScene.fxml"));
                    AnchorPane songList = fxmlLoader.load();

                    SongListTemplateScene controller = fxmlLoader.getController();
                    controller.setAudioID(audioID);
                    controller.setHomeScene(this.homeScene);
                    controller.setSearchScene(this);

                    MouseEffects.addMouseEffects(songList);

                    songList.setOnMouseClicked(event -> {
                        System.out.println("Redirecting to song...");

                        if (homeScene != null) {
                            homeScene.loadSongScene("/FXMLs/songpageScene.fxml", audioID);
                        } else {
                            System.out.println("HomeScene is null!");
                        }
                    });
                    searchFlowPane.getChildren().add(songList);
                }
            }

            if (resultCount == 0) {
                resultsLabel.setText("Found 0 results for query: \"" + query + "\"");
            } else {
                resultsLabel.setText("Found " + resultCount + " result" + (resultCount > 1 ? "s" : "") + " for query: \"" + query + "\"");
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the appropriate SQL query based on the search mode.
     *
     * @param mode The search mode.
     * @return SQL query string.
     */
    private String getSQLForMode(String mode) {
        return switch (mode) {
            case "Title" -> "SELECT audioID FROM Audio WHERE audioName LIKE ? ORDER BY audioID ASC";
            case "Genre" -> """
                SELECT a.audioID 
                FROM Audio a 
                JOIN Genre g ON a.genreID = g.genreID 
                WHERE g.genreName LIKE ? 
                ORDER BY a.audioID ASC
                """;
            case "Artist" -> """
                SELECT a.audioID 
                FROM Audio a 
                JOIN Artists ar ON a.artistID = ar.artistID 
                WHERE ar.artistName LIKE ? 
                ORDER BY a.audioID ASC
                """;
            case "Album" -> """
                SELECT a.audioID 
                FROM Audio a 
                JOIN Albums al ON a.albumID = al.albumID 
                WHERE al.albumName LIKE ? 
                ORDER BY a.audioID ASC
                """;
            default -> null;
        };
    }
}
