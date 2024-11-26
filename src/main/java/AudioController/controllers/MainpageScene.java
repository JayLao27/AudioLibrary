package AudioController.controllers;

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

public class MainpageScene {

    private HomeScene homeScene; // Reference to the main controller

    public void setHomeScene(HomeScene homeScene) {
        this.homeScene = homeScene;
    }

    @FXML
    FlowPane songcardContainerPane;

    @FXML
    public void initialize() {
        try {
            // temporary loop to be replaced with database select * statement
            for (int i = 0; i < 10; i++) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXMLs/songcardtemplateScene.fxml"));
                AnchorPane songCard = fxmlLoader.load();

                addMouseEffects(songCard);

                songCard.setOnMouseClicked(event -> {
                    System.out.println("Redirecting to song...");

                    // Move the scene loading logic here
                    if (homeScene != null) {
                        homeScene.loadScene("/FXMLs/songpageScene.fxml");
                    } else {
                        System.out.println("HomeScene is null!");
                    }
                });

                // Add the song card to the FlowPane
                songcardContainerPane.getChildren().add(songCard);
            }
        } catch (IOException e) {
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

    //Song Card UX

    private void addMouseEffects(Pane songCard) {
        // Mouse enter: scale up and change background
        songCard.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), songCard);
            scaleTransition.setToX(1.05);
            scaleTransition.setToY(1.05);
            scaleTransition.play();

            songCard.setStyle("-fx-background-color: #d3d3d32b;");
        });

        songCard.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), songCard);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();

            songCard.setStyle("-fx-background-color: #cfcfcf1b");
        });

        // Mouse pressed: scale slightly down and darken background
        songCard.setOnMousePressed(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), songCard);
            scaleTransition.setToX(1.04);
            scaleTransition.setToY(1.04);
            scaleTransition.play();

            // Darken background color
            songCard.setStyle("-fx-background-color: #dfdfdf2b;");
        });

        songCard.setOnMouseReleased(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(20), songCard);
            scaleTransition.setToX(1.05);
            scaleTransition.setToY(1.05);
            scaleTransition.play();

            // Restore hover background color
            songCard.setStyle("-fx-background-color: #d3d3d32b;");
        });
    }

}
