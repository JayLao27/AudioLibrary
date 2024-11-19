package com.example.audiolibrary.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class HomeScene {

    private boolean isPlaying = false;

    @FXML
    Pane sidebarProfilePane;
    @FXML
    Pane sidebarLibraryPane;
    @FXML
    Pane sidebarPlaylistPane;
    @FXML
    Pane sidebarCartPane;
    @FXML
    ImageView playPauseIcon;


    //Sidebar Panes UX
    @FXML
    private void handlePaneEntered(MouseEvent event) {
        ((Pane) event.getSource()).setStyle("-fx-background-color: #202020;");
    }

    @FXML
    private void handlePaneExited(MouseEvent event) {
        ((Pane) event.getSource()).setStyle("-fx-background-color: transparent;");
    }

    @FXML
    private void handlePanePressed(MouseEvent event) {
        Pane pressedPane = (Pane) event.getSource();
        pressedPane.setStyle("-fx-background-color: #252525;");
    }

    @FXML
    private void handlePaneRelease(MouseEvent event) {
        Pane releasedPane = (Pane) event.getSource();
        releasedPane.setStyle("-fx-background-color: #202020;");
    }

    //Side Bar Panes Functions
    @FXML
    private void handleProfileClicked(MouseEvent event) {
        //Logic
    }

    @FXML
    private void handleLibraryClicked(MouseEvent event) {
        //Logic
    }

    @FXML
    private void handlePlaylistClicked(MouseEvent event) {
        //Logic
    }

    @FXML
    private void handleCartClicked(MouseEvent event) {
        //Logic
    }


    //Top Bar Controller Buttons UX
    @FXML
    private void handlePlayEntered(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        imageView.setScaleX(1.2);
        imageView.setScaleY(1.2);
    }

    @FXML
    private void handlePlayExited(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        imageView.setScaleX(1.0);
        imageView.setScaleY(1.0);
    }

    @FXML
    private void handlePlayRelease(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        imageView.setScaleX(1.2);
        imageView.setScaleY(1.2);
    }

    @FXML
    private void handlePlayPressed(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        imageView.setScaleX(1.0);
        imageView.setScaleY(1.0);
    }

    //Top Bar Controller Buttons Functions
    @FXML
    private void handleReverseClicked(MouseEvent event) {
        //Logic
    }

    @FXML
    private void handlePlayPauseClicked(MouseEvent event) {
        isPlaying = !isPlaying;

        String imagePath = isPlaying ? "/ProjectImages/pause.png" : "/ProjectImages/play-button.png";
        playPauseIcon.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        playPauseIcon.setSmooth(true);
        playPauseIcon.setPreserveRatio(true);

        if (isPlaying) {
            System.out.println("Swapped to Pause");
        } else {
            System.out.println("Swapped to Play");
        }

        //More Logic
    }

    @FXML
    private void handleForwardClicked(MouseEvent event) {
        //Logic
    }

    @FXML
    private void handleShuffleClicked(MouseEvent event) {
        //Logic
    }
}
