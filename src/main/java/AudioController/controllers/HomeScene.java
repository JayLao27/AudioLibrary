package AudioController.controllers;

import AudioController.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

/**
 * Controller for the HomeScene in the AudioPlayer application.
 *
 * This class is responsible for managing the interactions and UI updates for the home scene.
 * It includes handling user input, search functionality, volume control, play/pause state,
 * and sidebar pane interactions.
 *
 * <p>
 * This class provides a set of methods that are responsible for loading various types of scenes,
 * including artist, song, playlist, payment, and current song scenes. It allows flexibility for passing
 * parameters such as artist ID, audio ID, and payment ID, as well as safely handling any potential errors
 * during the FXML loading process.
 * </p>
 *
 * <p>
 * Key features:
 * <li> Dynamic loading of the main page or search results based on user input in the search bar.</li>
 * <li> Volume control using a slider that updates the audio player's volume and displays corresponding icons.</li>
 * <li> Play/pause button that updates based on the playback state.</li>
 * <li> Sidebar pane hover effects for a better user experience.</li>
 * <li> Search queries for specific songs.</li>
 * <li> Load Scene methods to allow other controllers to update HomeScene's bodyVBox.</li>
 * </p>
 */
public class HomeScene {

    //JavaFX's BooleanProperty is used instead of primitive boolean as its state can be tracked with a listener.
    private BooleanProperty isPlaying = new SimpleBooleanProperty(false);

    private String currentQuery, mode;

    @FXML
    ComboBox<SearchMode> modeComboBox;
    @FXML
    Label usernameLabel;
    @FXML
    Pane sidebarProfilePane, sidebarLibraryPane, sidebarPlaylistPane, sidebarCartPane, currentSongPane;
    @FXML
    ProgressBar volumeProgressBar;
    @FXML
    ImageView playPauseIcon, volumeIcon, shuffleIcon, loopIcon;
    @FXML
    Slider volumeSlider;
    @FXML
    TextField topbarSearchField;
    @FXML
    VBox bodyVBox;

    /**
     * Initializes the HomeScene and sets up the UI components.
     *
     * - Sets the username label using the `UserSession` and `ResourceLoader`.
     * - Loads the main page scene as the initial view.
     * - Sets up the volume slider and binds it to the audio player's volume property.
     * - Adds listeners to the volume slider and play/pause button.
     * - Handles search field input and dynamically loads the appropriate scene.
     */
    @FXML
    private void initialize() {
        AudioPlayer.getInstance().setHomeScene(this);
        usernameLabel.setText(ResourceLoader.getUsername(UserSession.getInstance().getUserID()) + " (₱ " + ResourceLoader.getBalance(UserSession.getInstance().getUserID()) + ")");
        loadScene("/FXMLs/mainpageScene.fxml");
        volumeSlider.setValue(50);

        AudioPlayer.getInstance().setVolume(volumeSlider.getValue() / 100.0);

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            AudioPlayer.getInstance().setVolume(newValue.doubleValue() / 100.0);

            updateVolumeIcon(newValue.doubleValue());
        });

        updateVolumeIcon(volumeSlider.getValue());

        volumeSlider.setOnMouseEntered(event -> {
            volumeProgressBar.getStyleClass().add("bar-enter");
            volumeProgressBar.getStyleClass().remove("bar-exit");
        });

        volumeSlider.setOnMouseExited(event -> {
            volumeProgressBar.getStyleClass().add("bar-exit");
            volumeProgressBar.getStyleClass().remove("bar-enter");
        });

        volumeSlider.setOnMousePressed(event -> {
            volumeProgressBar.getStyleClass().add("bar-enter");
            volumeProgressBar.getStyleClass().remove("bar-exit");
        });

        volumeSlider.setOnMouseReleased(event -> {
            volumeProgressBar.getStyleClass().add("bar-exit");
            volumeProgressBar.getStyleClass().remove("bar-enter");
        });

        volumeProgressBar.progressProperty().bind(volumeSlider.valueProperty().divide(100.0));
        isPlaying.bind(AudioPlayer.getInstance().isPlayingProperty());

        isPlaying.addListener((observable, oldValue, newValue) -> updatePlayPauseIcon(newValue));
        shuffleIcon.setPickOnBounds(true);
        loopIcon.setPickOnBounds(true);

        topbarSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Trim the input for clean comparisons
            String query = newValue.trim();

            if (query.isEmpty() || query.equalsIgnoreCase("Search")) {
                // Reload the main page scene if input is empty or default text
                loadScene("/FXMLs/mainpageScene.fxml");
            } else {
                // Dynamically load a scene based on the current query
                searchAndLoadResults(query, getCurrentMode());
            }
        });

        // Handle when the search bar gains focus (click or tab)
        topbarSearchField.setOnMouseClicked(event -> {
            if (topbarSearchField.getText().equals("Search")) {
                topbarSearchField.clear();
            }
        });

        // Handle focus lost (restore the filler text if the field is empty)
        topbarSearchField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && topbarSearchField.getText().trim().isEmpty()) {
                topbarSearchField.setText("Search");
            }
        });

        // Handle ESC key press (clear text and restore the filler)
        topbarSearchField.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    if (topbarSearchField.getText().trim().isEmpty()) {
                        topbarSearchField.setText("Search");
                    }
                    topbarSearchField.getParent().requestFocus(); // Remove focus from search bar
                    break;
                default:
                    break;
            }
        });

        modeComboBox.setItems(FXCollections.observableArrayList(SearchMode.values()));
        modeComboBox.setValue(SearchMode.TITLE);

        // Listen for ComboBox value changes
        modeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            String query = topbarSearchField.getText().trim();
            if (!query.isEmpty() && !query.equalsIgnoreCase("Search")) {
                searchAndLoadResults(query, String.valueOf(newValue));
            }
        });
    }

    public enum SearchMode {
        TITLE("Title"), GENRE("Genre"), ARTIST("Artist"), ALBUM("Album");

        private final String displayName;

        SearchMode(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
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
     * - `handleShuffleClicked`: Handles shuffle functionality, toggling shuffle mode on or off.
     * - `handleLoopClicked`: Handles loop functionality, toggling loop mode on or off.
     **************************************************************************************************************************/

    /**
     * Handles the reverse button click event. Plays the previous track in the playlist.
     *
     * @param event The mouse event triggered by the button click.
     */
    @FXML
    private void handleReverseClicked(MouseEvent event) {
        AudioPlayer.getInstance().playPrevious();
    }


    /**
     * Handles the play/pause button click event. Toggles the playback state between playing and paused.
     *
     * @param event The mouse event triggered by the button click.
     */
    @FXML
    private void handlePlayPauseClicked(MouseEvent event) {
        AudioPlayer.getInstance().togglePlayPause();
    }

    /**
     * Handles the forward button click event. Plays the next track in the playlist.
     *
     * @param event The mouse event triggered by the button click.
     */
    @FXML
    private void handleForwardClicked(MouseEvent event) {
        AudioPlayer.getInstance().playNext();
    }

    /**
     * Handles the shuffle button click event. Toggles shuffle mode on or off.
     * Updates the shuffle icon based on the new state.
     *
     * @param event The mouse event triggered by the button click.
     */
    @FXML
    private void handleShuffleClicked(MouseEvent event) {
        AudioPlayer audioPlayer = AudioPlayer.getInstance();
        boolean newShuffleState = !audioPlayer.isShuffled();
        audioPlayer.setShuffled(newShuffleState);

        System.out.println(newShuffleState ? "Shuffle mode enabled" : "Shuffle mode disabled");
        updateShuffleIcon(newShuffleState);
    }

    /**
     * Handles the loop button click event. Toggles loop mode on or off.
     * Updates the loop icon based on the new state.
     *
     * @param event The mouse event triggered by the button click.
     */
    @FXML
    private void handleLoopClicked(MouseEvent event) {
        AudioPlayer audioPlayer = AudioPlayer.getInstance();
        boolean newLoopState = !audioPlayer.isLooped();
        audioPlayer.setLooped(newLoopState);

        System.out.println(newLoopState ? "Loop enabled" : "Loop disabled");
        updateLoopIcon(newLoopState);
    }


    /**
     * Searches for content based on the query and dynamically loads the search results scene.
     * Clears the current content in the VBox and loads a new search scene based on the query.
     *
     * @param query The search query entered by the user.
     */
    @FXML
    private void searchAndLoadResults(String query, String mode) {
        this.currentQuery = query;
        this.mode = String.valueOf(modeComboBox.getValue());

        try {
            bodyVBox.getChildren().clear(); // Clear any existing content in the VBox

            // Load the search results scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/searchScene.fxml"));
            Node searchScene = loader.load();

            // Get the controller of the loaded scene
            SearchScene controller = loader.getController();
            controller.setHomeScene(this);

            // Pass the query to the controller so it can use it to fetch and display results
            controller.populateSearchResults(query, mode);


            // Add the search results scene to the VBox
            bodyVBox.getChildren().add(searchScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the current search query entered by the user.
     *
     * @return The current search query.
     */
    public String getCurrentQuery() {
        return currentQuery;
    }

    /**
     * Returns the current search mode entered by the user.
     *
     * @return The current search mode.
     */
    public String getCurrentMode() {
        return mode;
    }


    /**
     * Loads a scene from the specified FXML path and sets the `HomeScene` context.
     * This method returns the controller of the loaded scene, allowing further manipulation if necessary.
     *
     * @param fxmlPath The path to the FXML file to be loaded.
     * @param <T> The type of the controller associated with the FXML scene.
     * @return The controller of the loaded scene.
     */
    public <T> T loadScene(String fxmlPath) {
        return loadScene(fxmlPath, controller -> {});
    }


    /**
     * Loads a scene from the specified FXML path, sets the `HomeScene` context, and allows additional setup
     * of the controller using the provided `Consumer` function.
     *
     * @param fxmlPath The path to the FXML file to be loaded.
     * @param setupController The setup function to modify the controller after it is loaded.
     * @param <T> The type of the controller associated with the FXML scene.
     * @return The controller of the loaded scene.
     */
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


    /**
     * Loads a scene from the specified FXML path, sets the `HomeScene` context, and passes an `artistID`
     * to the controller if it supports it.
     *
     * @param fxmlPath The path to the FXML file to be loaded.
     * @param artistID The artist ID to be passed to the controller, if supported.
     */
    public void loadScene(String fxmlPath, int artistID) {
        loadScene(fxmlPath, artistID, controller -> {});
    }


    /**
     * Loads a scene from the specified FXML path, sets the `HomeScene` context, and passes an `artistID`
     * to the controller, if the controller has a method for setting the artist ID.
     *
     * @param fxmlPath The path to the FXML file to be loaded.
     * @param artistID The artist ID to be passed to the controller.
     * @param setupController The setup function to modify the controller after it is loaded.
     */
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


    /**
     * Loads a scene from the specified FXML path, sets the `HomeScene` context, and passes a `genreID`
     * to the controller if it supports it.
     *
     * @param fxmlPath The path to the FXML file to be loaded.
     * @param genreID The genre ID to be passed to the controller, if supported.
     */
    public void loadGenreScene(String fxmlPath, int genreID) {
        loadGenreScene(fxmlPath, genreID, controller -> {});
    }


    /**
     * Loads a scene from the specified FXML path, sets the `HomeScene` context, and passes a `genreID`
     * to the controller, if the controller has a method for setting the genre ID.
     *
     * @param fxmlPath The path to the FXML file to be loaded.
     * @param genreID The genre ID to be passed to the controller.
     * @param setupController The setup function to modify the controller after it is loaded.
     */
    public void loadGenreScene(String fxmlPath, int genreID, Consumer<Object> setupController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent sceneContent = loader.load();

            Object controller = loader.getController();
            if (controller != null) {
                if (controller instanceof SceneWithHomeContext) {
                    ((SceneWithHomeContext) controller).setHomeScene(this);
                }

                try {
                    Method setGenreIDMethod = controller.getClass().getMethod("setGenreID", int.class);
                    setGenreIDMethod.invoke(controller, genreID);
                } catch (NoSuchMethodException e) {
                    System.out.println("Controller does not have setGenreID method: " + controller.getClass().getSimpleName());
                }
            }

            bodyVBox.getChildren().setAll(sceneContent);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading " + fxmlPath);
        }
    }


    /**
     * Loads a scene from the specified FXML path, sets the `HomeScene` context, and passes an `albumID`
     * to the controller if it supports it.
     *
     * @param fxmlPath The path to the FXML file to be loaded.
     * @param albumID The album ID to be passed to the controller, if supported.
     */
    public void loadAlbumScene(String fxmlPath, int albumID) {
        loadAlbumScene(fxmlPath, albumID, controller -> {});
    }


    /**
     * Loads a scene from the specified FXML path, sets the `HomeScene` context, and passes an `albumID`
     * to the controller, if the controller has a method for setting the album ID.
     *
     * @param fxmlPath The path to the FXML file to be loaded.
     * @param albumID The album ID to be passed to the controller.
     * @param setupController The setup function to modify the controller after it is loaded.
     */
    public void loadAlbumScene(String fxmlPath, int albumID, Consumer<Object> setupController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent sceneContent = loader.load();

            Object controller = loader.getController();
            if (controller != null) {
                if (controller instanceof SceneWithHomeContext) {
                    ((SceneWithHomeContext) controller).setHomeScene(this);
                }

                try {
                    Method setAlbumIDMethod = controller.getClass().getMethod("setAlbumID", int.class);
                    setAlbumIDMethod.invoke(controller, albumID);
                } catch (NoSuchMethodException e) {
                    System.out.println("Controller does not have setAlbumID method: " + controller.getClass().getSimpleName());
                }
            }

            bodyVBox.getChildren().setAll(sceneContent);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading " + fxmlPath);
        }
    }


    /**
     * Loads a song-specific scene from the specified FXML path and passes an `audioID` to the controller.
     *
     * @param fxmlPath The path to the FXML file to be loaded.
     * @param artistID The artist ID to be passed to the controller.
     */
    public void loadSongScene(String fxmlPath, int artistID) {
        loadSongScene(fxmlPath, artistID, controller -> {});
    }


    /**
     * Loads a song-specific scene from the specified FXML path, passes an `audioID` to the controller,
     * and allows additional setup of the controller using the provided `Consumer` function.
     *
     * @param fxmlPath The path to the FXML file to be loaded.
     * @param artistID The artist ID to be passed to the controller.
     * @param setupController The setup function to modify the controller after it is loaded.
     */
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


    /**
     * Loads a playlist-specific scene from the specified FXML path and passes a `playlistID` to the controller.
     *
     * @param fxmlPath The path to the FXML file to be loaded.
     * @param playlistID The playlist ID to be passed to the controller.
     */
    public void loadPlaylistScene(String fxmlPath, int playlistID) {
        loadPlaylistScene(fxmlPath, playlistID, controller -> {});
    }


    /**
     * Loads a playlist-specific scene from the specified FXML path, passes a `playlistID` to the controller,
     * and allows additional setup of the controller using the provided `Consumer` function.
     *
     * @param fxmlPath The path to the FXML file to be loaded.
     * @param playlistID The playlist ID to be passed to the controller.
     * @param setupController The setup function to modify the controller after it is loaded.
     */
    public void loadPlaylistScene(String fxmlPath, int playlistID, Consumer<Object> setupController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent sceneContent = loader.load();

            Object controller = loader.getController();
            if (controller != null) {
                if (controller instanceof SceneWithHomeContext) {
                    ((SceneWithHomeContext) controller).setHomeScene(this);
                }

                try {
                    Method setArtistIDMethod = controller.getClass().getMethod("setPlaylistID", int.class);
                    setArtistIDMethod.invoke(controller, playlistID);
                } catch (NoSuchMethodException e) {
                    System.out.println("Controller does not have setPlaylistID method: " + controller.getClass().getSimpleName());
                }
            }

            bodyVBox.getChildren().setAll(sceneContent);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading " + fxmlPath);
        }
    }


    /**
     * Loads a payment-specific scene from the specified FXML path and passes a `paymentID` to the controller.
     *
     * @param fxmlPath The path to the FXML file to be loaded.
     * @param paymentID The payment ID to be passed to the controller.
     */
    public void loadPaymentScene(String fxmlPath, int paymentID) {
        loadPaymentScene(fxmlPath, paymentID, controller -> {});
    }


    /**
     * Loads a payment-specific scene from the specified FXML path, passes a `paymentID` to the controller,
     * and allows additional setup of the controller using the provided `Consumer` function.
     *
     * @param fxmlPath The path to the FXML file to be loaded.
     * @param paymentID The payment ID to be passed to the controller.
     * @param setupController The setup function to modify the controller after it is loaded.
     */
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


    /**
     * Loads the current song scene into the top bar.
     *
     * @param audioID The audio ID to be passed to the controller.
     */
    public void loadCurrentSong(int audioID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLs/currentsongScene.fxml"));
            Parent sceneContent = loader.load();

            CurrentSongScene controller = loader.getController();

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
