package AudioController;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadManager {
    private static final ExecutorService executor = Executors.newFixedThreadPool(5);

    public static void downloadAudio(int audioID, Path destination) {
        executor.submit(() -> {
            try {
                System.out.println("Starting download for audioID: " + audioID);

                // Fetch audio file name
                String audioFileName = ResourceLoader.getAudioFileName(audioID);
                if (audioFileName != null) {
                    String filePath = "/audioFiles/" + audioFileName;
                    URL resource = DownloadManager.class.getResource(filePath);

                    if (resource != null) {
                        System.out.println("Found audio file: " + resource);

                        // Copy the file to the destination
                        try (InputStream inputStream = resource.openStream()) {
                            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
                            System.out.println("Download completed: " + destination);
                        }
                    } else {
                        System.out.println("Audio file not found in resources: " + filePath);
                    }
                } else {
                    System.out.println("No audio found for audioID: " + audioID);
                }
            } catch (IOException e) {
                System.out.println("Error during file download for audioID: " + audioID);
                e.printStackTrace();
            }
        });
    }

    public static void shutdown() {
        executor.shutdown();
    }
}
