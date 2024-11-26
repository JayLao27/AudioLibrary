package AudioController.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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
    @FXML
    VBox bodyVBox;

    @FXML
    private void initialize() {
        loadScene("/FXMLs/mainpageScene.fxml");
    }

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
    private void handleTitleClicked(MouseEvent event) {
        loadScene("/FXMLs/mainpageScene.fxml");
    }

    @FXML
    private void handleProfileClicked(MouseEvent event) {
        loadScene("/FXMLs/profileScene.fxml");
    }

    @FXML
    private void handleLibraryClicked(MouseEvent event) {
        loadScene("/FXMLs/libraryScene.fxml");
    }

    @FXML
    private void handlePlaylistClicked(MouseEvent event) {
        loadScene("/FXMLs/playlistScene.fxml");
    }

    @FXML
    private void handleCartClicked(MouseEvent event) {
        loadScene("/FXMLs/cartScene.fxml");
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

    //Load Scene

    public void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent sceneContent = loader.load();

            Object controller = loader.getController();
            if (controller != null) {
                try {
                    controller.getClass().getMethod("setHomeScene", HomeScene.class)
                            .invoke(controller, this);
                } catch (NoSuchMethodException ignored) {
                    //Ignore controllers without setHomeScene Method
                }
            }

            bodyVBox.getChildren().setAll(sceneContent);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading " + fxmlPath);
        }
    }
}
