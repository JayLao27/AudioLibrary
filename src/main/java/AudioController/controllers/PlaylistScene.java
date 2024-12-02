package AudioController.controllers;

import AudioController.SceneWithHomeContext; // Make sure to import this if needed
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class PlaylistScene implements SceneWithHomeContext {

    @FXML
    private AnchorPane makeplaylistCard;

    private HomeScene homeScene; // Reference to the HomeScene

    // Method to set the HomeScene reference
    public void setHomeScene(HomeScene homeScene) {
        this.homeScene = homeScene;
    }

    @FXML
    private void onMakeplaylistCardClicked(MouseEvent event) {
        System.out.println("Button clicked!");
        if (homeScene != null) {
            homeScene.loadScene("/FXMLs/addPlaylistScene.fxml");
        }
    }

    // Playlist Card Pane UX
    @FXML
    private void handlePaneEntered(MouseEvent event) {
        ((Pane) event.getSource()).setStyle("-fx-background-color: #d3d3d32b;");
    }

    @FXML
    private void handlePaneExited(MouseEvent event) {
        ((Pane) event.getSource()).setStyle("-fx-background-color: transparent;");
    }

    @FXML
    private void handlePanePressed(MouseEvent event) {
        Pane pressedPane = (Pane) event.getSource();
        pressedPane.setStyle("-fx-background-color: #dfdfdf3b;");
    }

    @FXML
    private void handlePaneReleased(MouseEvent event) {
        Pane releasedPane = (Pane) event.getSource();
        releasedPane.setStyle("-fx-background-color: #d3d3d32b;");
    }


}