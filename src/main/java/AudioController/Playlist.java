package AudioController;

import java .io.File;

public class Playlist {
    private String name;
    private File imageFile;

    public Playlist(String name, File imageFile) {
        this.name = name;
        this.imageFile = imageFile;
    }

    public String getName() {
        return name;
    }

    public File getImageFile() {
        return imageFile;
    }

    @Override
    public String toString() {
        return name; // This will be displayed in the ListView
    }
}