package AudioController.controllers;

import AudioController.ResourceLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Controller for displaying audio items in the checkout UI.
 * <p>
 * This class manages the display of individual audio items, including their song name,
 * price, and cover image, in the checkout scene.
 */
public class CheckoutListTemplateScene {

    @FXML
    private Label songNameLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private ImageView songCoverImage;

    /**
     * Reference to the parent {@link CheckoutScene} controller for interacting with the checkout view.
     */
    private CheckoutScene checkoutScene;

    /**
     * The unique identifier for the audio item being displayed.
     */
    private int audioID;

    /**
     * Initializes the FXML components for the checkout list template.
     * This method is called automatically when the FXML file is loaded.
     */
    @FXML
    private void initialize() {
        // Initialization logic if needed
    }

    /**
     * Sets the audio ID and initializes the checkout item details.
     * <p>
     * This method retrieves and displays the song name, price, and cover image
     * based on the specified audio ID.
     *
     * @param audioID       The unique identifier for the audio item.
     * @param checkoutScene The parent controller managing the checkout scene.
     */
    public void setAudioID(int audioID, CheckoutScene checkoutScene) {
        this.audioID = audioID;
        this.checkoutScene = checkoutScene;
        System.out.println("Initializing with audio ID: " + audioID);
        loadAudioDetails();
    }

    /**
     * Loads and displays the audio details for the current audio ID.
     * <p>
     * This includes:
     * <ul>
     *     <li>Song name</li>
     *     <li>Price (FREE for zero price)</li>
     *     <li>Cover image</li>
     * </ul>
     * If the song image fails to load, an error is logged to the console.
     */
    private void loadAudioDetails() {
        // Load song name
        String songName = ResourceLoader.getAudioName(audioID);
        songNameLabel.setText(songName);

        // Load and format price
        Double price = ResourceLoader.getAudioPrice(audioID);
        if (price == 0) {
            priceLabel.setText("FREE");
        } else if (price > 0) {
            priceLabel.setText("₱ " + String.format("%.2f", price));
        } else {
            priceLabel.setText("???");
        }

        // Load cover image
        String songImagePath = ResourceLoader.getAudioImagePath(audioID);
        if (songImagePath != null) {
            try {
                Image image = new Image(getClass().getResourceAsStream(songImagePath));
                if (!image.isError()) {
                    songCoverImage.setImage(image);
                } else {
                    System.out.println("Error loading image: " + songImagePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception loading image: " + songImagePath);
            }
        } else {
            System.out.println("Song image path is null for audioID: " + audioID);
        }
    }
}
