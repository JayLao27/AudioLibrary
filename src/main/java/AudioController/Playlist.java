package AudioController;

import java.io.File;

/**
 * Represents a playlist with a name and an associated image.
 * This class stores the name of the playlist and the file reference to its associated image.
 * It provides methods to access the name and image file, and also overrides the {@link Object#toString()} method
 * to return the playlist name for display purposes.
 * <p>
 * The playlist can be used in contexts such as a media player application where a list of playlists needs to be displayed,
 * with each playlist having a name and an image (e.g., an album art or icon).
 * </p>
 */
public class Playlist {
    private String name;
    private File imageFile;

    /**
     * Constructs a {@link Playlist} with the given name and image file.
     *
     * @param name the name of the playlist
     * @param imageFile the image file associated with the playlist
     */
    public Playlist(String name, File imageFile) {
        this.name = name;
        this.imageFile = imageFile;
    }

    /**
     * Returns the name of the playlist.
     *
     * @return the name of the playlist
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the image file associated with the playlist.
     *
     * @return the image file associated with the playlist
     */
    public File getImageFile() {
        return imageFile;
    }

    /**
     * Returns a string representation of the playlist, which is the name of the playlist.
     * This is useful for displaying the playlist in a user interface (e.g., in a {@link javafx.scene.control.ListView}).
     *
     * @return the name of the playlist
     */
    @Override
    public String toString() {
        return name; // This will be displayed in the ListView
    }
}
