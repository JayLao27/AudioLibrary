package AudioController.controllers;

import AudioController.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public class HomeScene {

    //JavaFX's BooleanProperty is used instead of primitive boolean as its state can be tracked with a listener.
    private BooleanProperty isPlaying = new SimpleBooleanProperty(false);

    @FXML
    Label usernameLabel;
    @FXML
    Pane sidebarProfilePane, sidebarLibraryPane, sidebarPlaylistPane, sidebarCartPane, currentSongPane;
    @FXML
    ImageView playPauseIcon, volumeIcon, shuffleIcon, loopIcon;
    @FXML
    Slider volumeSlider;
    @FXML
    VBox bodyVBox;

    /**************************************************************************************************************************
     * Initializes the scene and sets up the initial UI components and bindings.
     *
     * - Sets the home scene for the `AudioPlayer` instance.
     * - Sets the username label using the `UserSession` and `ResourceLoader`.
     * - Loads the main page scene as the initial view.
     * - Initializes the volume slider to 50 and binds its value to the `AudioPlayer` instance's volume property.
     * - Adds a listener to the volume slider to update the `AudioPlayer` volume and the volume icon when the slider value changes.
     * - Updates the volume icon based on the initial volume slider value.
     * - Binds the `isPlaying` property of the `AudioPlayer` to update the play/pause icon when the playback state changes.
     **************************************************************************************************************************/
    @FXML
    private void initialize() {
        AudioPlayer.getInstance().setHomeScene(this);
        usernameLabel.setText(ResourceLoader.getUsername(UserSession.getInstance().getUserID()));
        loadScene("/FXMLs/mainpageScene.fxml");
        volumeSlider.setValue(50);

        AudioPlayer.getInstance().setVolume(volumeSlider.getValue() / 100.0);

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            AudioPlayer.getInstance().setVolume(newValue.doubleValue() / 100.0);

            updateVolumeIcon(newValue.doubleValue());
        });

        updateVolumeIcon(volumeSlider.getValue());
        isPlaying.bind(AudioPlayer.getInstance().isPlayingProperty());

        isPlaying.addListener((observable, oldValue, newValue) -> updatePlayPauseIcon(newValue));
        shuffleIcon.setPickOnBounds(true);
        loopIcon.setPickOnBounds(true);
    }


    /**************************************************************************************************************************
     * Controller methods for handling sidebar panes interactions and user experience (UX) effects.
     *
     * - `handlePaneEntered`: Changes the background color of the pane when the mouse enters and creates hover effect.
     * - `handlePaneExited`: Resets the background color of the pane to transparent when the mouse exits and removes hover effect.
     * - `handlePanePressed`: Changes the background color of the pane when the pane is pressed.
     * - `handlePaneRelease`: Resets the background color of the pane to the hover shade when the mouse button is released.
     **************************************************************************************************************************/
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


    /**************************************************************************************************************************
     * Controller methods for handling sidebar pane functionality.
     *
     * - `handleTitleClicked`: Loads the main page scene when the title pane is clicked.
     * - `handleProfileClicked`: Loads the profile scene when the profile pane is clicked.
     * - `handleLibraryClicked`: Loads the library scene when the library pane is clicked.
     * - `handlePlaylistClicked`: Loads the playlist scene when the playlist pane is clicked.
     * - `handleCartClicked`: Loads the cart scene when the cart pane is clicked.
     **************************************************************************************************************************/
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


    /**************************************************************************************************************************
     * Controller methods for handling top bar button interactions and user experience (UX) effects.
     *
     * - `handlePlayEntered`: Increases the scale of the play button when the mouse enters its area.
     * - `handlePlayExited`: Resets the scale of the play button when the mouse exits its area.
     * - `handlePlayRelease`: Increases the scale of the play button when the mouse is released.
     * - `handlePlayPressed`: Resets the scale of the play button when the mouse is pressed.
     * - `updateVolumeIcon`: Changes the volume icon depending on the value in the volume slider.
     * - `updatePlayPauseIcon`: Updates the play/pause button icon based on the current playback state.
     **************************************************************************************************************************/
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

    private void updateVolumeIcon(double volume) {
        String imagePath;
        if (volume < 1) {
            imagePath = "/ProjectImages/muteicon.png";  // Mute icon
        } else if (volume >= 1 && volume <= 50) {
            imagePath = "/ProjectImages/volumemidicon.png";   // Low volume icon
        } else {
            imagePath = "/ProjectImages/volumehighicon.png";  // High volume icon
        }

        volumeIcon.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        volumeIcon.setSmooth(true);
        volumeIcon.setPreserveRatio(true);
    }

    private void updatePlayPauseIcon(boolean isPlaying) {
        String iconPath = isPlaying ? "/ProjectImages/pause.png" : "/ProjectImages/play-button.png";
        playPauseIcon.setImage(new Image(getClass().getResourceAsStream(iconPath)));
    }

    private void updateShuffleIcon(boolean isShuffled) {
        String iconPath = isShuffled ? "/ProjectImages/shuffled.png" : "/ProjectImages/shuffle.png";
        shuffleIcon.setImage(new Image(getClass().getResourceAsStream(iconPath)));
    }

    private void updateLoopIcon(boolean isLooped) {
        String iconPath = isLooped ? "/ProjectImages/looped.png" : "/ProjectImages/loop.png";
        loopIcon.setImage(new Image(getClass().getResourceAsStream(iconPath)));
    }


    /**************************************************************************************************************************
     * Controller methods for handling top bar button interactions in the audio player.
     *
     * - `handleReverseClicked`: Plays the previous track using the `AudioPlayer` instance.
     * - `handlePlayPauseClicked`: Toggles between play and pause states using the `AudioPlayer` instance.
     * - `handleForwardClicked`: Plays the next track using the `AudioPlayer` instance.
     * - `handleShuffleClicked`: Handles shuffle functionality.
     **************************************************************************************************************************/
    @FXML
    private void handleReverseClicked(MouseEvent event) {
        AudioPlayer.getInstance().playPrevious();
    }

    @FXML
    private void handlePlayPauseClicked(MouseEvent event) {
        AudioPlayer.getInstance().togglePlayPause();
    }

    @FXML
    private void handleForwardClicked(MouseEvent event) {
        AudioPlayer.getInstance().playNext();
    }

    @FXML
    private void handleShuffleClicked(MouseEvent event) {
        AudioPlayer audioPlayer = AudioPlayer.getInstance();
        boolean newShuffleState = !audioPlayer.isShuffled();
        audioPlayer.setShuffled(newShuffleState);

        System.out.println(newShuffleState ? "Shuffle mode enabled" : "Shuffle mode disabled");
        updateShuffleIcon(newShuffleState);
    }

    @FXML
    private void handleLoopClicked(MouseEvent event) {
        AudioPlayer audioPlayer = AudioPlayer.getInstance();
        boolean newLoopState = !audioPlayer.isLooped();
        audioPlayer.setLooped(newLoopState);

        System.out.println(newLoopState ? "Loop enabled" : "Loop disabled");
        updateLoopIcon(newLoopState);
    }


    /**************************************************************************************************************************
     * Utility methods for dynamically loading FXML scenes into the application.
     *
     * - `loadScene`: Loads an FXML scene to HomeScene's bodyVBox, with optional controller setup.
     * - `loadScene(String, int)`: Loads an FXML scene to HomeScene's bodyVBox and sets an `artistID` in the controller, if supported.
     * - `loadSongScene`: Loads a song-specific FXML scene to HomeScene's bodyVBox and passes an `audioID` to the controller.
     * - `loadPaymentScene`: Loads a payment-specific FXML scene HomeScene's bodyVBox and passes a `paymentID` to the controller.
     * - `loadCurrentSong`: Loads the "current song" scene, setting the `audioID` and linking it to HomeScene's top bar.
     *
     * All methods support setting the `HomeScene` context and safely handle errors during FXML loading or method invocation.
     **************************************************************************************************************************/
    public <T> T loadScene(String fxmlPath) {
        return loadScene(fxmlPath, controller -> {});
    }

    public <T> T loadScene(String fxmlPath, Consumer<T> setupController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent sceneContent = loader.load();

            T controller = loader.getController();
            if (controller instanceof SceneWithHomeContext) {
                ((SceneWithHomeContext) controller).setHomeScene(this);
            }

            setupController.accept(controller);

            bodyVBox.getChildren().setAll(sceneContent);
            return controller;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading " + fxmlPath);
            return null;
        }
    }

    public void loadScene(String fxmlPath, int artistID) {
        loadScene(fxmlPath, artistID, controller -> {});
    }

    public void loadScene(String fxmlPath, int artistID, Consumer<Object> setupController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent sceneContent = loader.load();

            Object controller = loader.getController();
            if (controller != null) {
                if (controller instanceof SceneWithHomeContext) {
                    ((SceneWithHomeContext) controller).setHomeScene(this);
                }

                try {
                    Method setArtistIDMethod = controller.getClass().getMethod("setArtistID", int.class);
                    setArtistIDMethod.invoke(controller, artistID);
                } catch (NoSuchMethodException e) {
                    System.out.println("Controller does not have setArtistID method: " + controller.getClass().getSimpleName());
                }
            }

            bodyVBox.getChildren().setAll(sceneContent);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading " + fxmlPath);
        }
    }

    public void loadSongScene(String fxmlPath, int artistID) {
        loadSongScene(fxmlPath, artistID, controller -> {});
    }

    public void loadSongScene(String fxmlPath, int artistID, Consumer<Object> setupController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent sceneContent = loader.load();

            Object controller = loader.getController();
            if (controller != null) {
                if (controller instanceof SceneWithHomeContext) {
                    ((SceneWithHomeContext) controller).setHomeScene(this);
                }

                try {
                    Method setArtistIDMethod = controller.getClass().getMethod("setAudioID", int.class);
                    setArtistIDMethod.invoke(controller, artistID);
                } catch (NoSuchMethodException e) {
                    System.out.println("Controller does not have setArtistID method: " + controller.getClass().getSimpleName());
                }
            }

            bodyVBox.getChildren().setAll(sceneContent);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading " + fxmlPath);
        }
    }

    public void loadPaymentScene(String fxmlPath, int paymentID) {
        loadPaymentScene(fxmlPath, paymentID, controller -> {});
    }

    public void loadPaymentScene(String fxmlPath, int paymentID, Consumer<Object> setupController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent sceneContent = loader.load();

            Object controller = loader.getController();
            if (controller != null) {
                if (controller instanceof SceneWithHomeContext) {
                    ((SceneWithHomeContext) controller).setHomeScene(this);
                }

                try {
                    Method setPaymentIDMethod = controller.getClass().getMethod("setPaymentID", int.class);
                    setPaymentIDMethod.invoke(controller, paymentID);
                } catch (NoSuchMethodException e) {
                    System.out.println("Controller does not have setPaymentID method: " + controller.getClass().getSimpleName());
                }

                setupController.accept(controller);
            }

            bodyVBox.getChildren().setAll(sceneContent);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading " + fxmlPath);
        }
    }

    public void loadCurrentSong(int audioID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/currentsongScene.fxml"));
            Parent sceneContent = loader.load();

            CurrentsongScene controller = loader.getController();

            if (controller instanceof SceneWithHomeContext) {
                ((SceneWithHomeContext) controller).setHomeScene(this);
            }

            controller.loadSong(audioID);

            currentSongPane.getChildren().setAll(sceneContent);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading currentsongScene.fxml");
        }
    }
}
