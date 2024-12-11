package AudioController;

import AudioController.controllers.HomeScene;

/**
 * Interface to be implemented by FXML scene controllers that require access to a {@link HomeScene} context.
 * <p>
 * This interface allows a JavaFX controller class associated with a specific scene to receive
 * a reference to a {@link HomeScene} instance. This is typically used in cases where the
 * controller needs to interact with or modify the home scene, such as for navigation
 * between scenes, sharing data, or managing state across multiple scenes.
 * </p>
 *
 * <p>
 * By implementing this interface, the controller becomes aware of the {@link HomeScene}
 * and can perform necessary actions that involve the home scene during the lifecycle of the scene.
 * </p>
 *
 * @see HomeScene
 */
public interface SceneWithHomeContext {

    /**
     * Sets the {@link HomeScene} context for this scene's controller.
     * <p>
     * This method is used to inject the {@link HomeScene} instance into the controller
     * for the current scene. Once injected, the controller can access and interact
     * with the home scene for navigation, data exchange, or other scene-related functionality.
     * </p>
     *
     * @param homeScene the {@link HomeScene} instance to be associated with the controller.
     *                  This cannot be null, as the controller needs to interact with it.
     *
     * @throws IllegalArgumentException if {@code homeScene} is null.
     */
    void setHomeScene(HomeScene homeScene);
}
