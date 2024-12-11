package AudioController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main class that launches the AudioController JavaFX application.
 * <p>
 * This class is responsible for starting the JavaFX application, loading the initial login scene
 * from an FXML file, and displaying the primary stage (window) for the application.
 * </p>
 *
 * <p>
 * It extends the {@link javafx.application.Application} class and overrides the {@link #start(Stage)}
 * method to set up the primary stage. The {@link #main(String[])} method is the entry point of the
 * application and launches the JavaFX application.
 * </p>
 */
public class Main extends Application {

    /**
     * The main entry point for the JavaFX application.
     * <p>
     * This method is called when the application starts. It loads the login scene FXML file,
     * sets it as the root for the application's initial stage, and shows the stage to the user.
     * </p>
     *
     * @param stage the primary stage for the application. This is the window that will be displayed.
     * @throws Exception if there is an error loading the FXML file or setting up the scene.
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Load the login scene FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/FXMLs/loginScene.fxml"));

        // Create a scene with the loaded FXML root and set it on the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);

        // Show the primary stage (application window)
        stage.show();
    }

    /**
     * The entry point for launching the JavaFX application.
     * <p>
     * This method invokes the {@link launch(String[])} method from the {@link Application} class,
     * which in turn calls the {@link #start(Stage)} method to initialize the GUI and display the application window.
     * </p>
     *
     * @param args command-line arguments passed to the application (not used in this case).
     */
    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
