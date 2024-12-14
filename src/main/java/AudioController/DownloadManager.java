package AudioController;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The {@code DownloadManager} class manages downloading audio files asynchronously.
 * <p>
 * This class uses a fixed thread pool to handle multiple audio downloads concurrently,
 * ensuring efficient resource utilization. The audio files are fetched from resources
 * and saved to a specified destination path on the file system.
 */
public class DownloadManager {

    /**
     * Thread pool executor with a fixed pool size of 5 to manage download tasks concurrently.
     */
    private static final ExecutorService executor = Executors.newFixedThreadPool(5);

    /**
     * Downloads an audio file associated with a specific audio ID and saves it to the given destination.
     * <p>
     * This method runs the download task asynchronously in a separate thread.
     * It retrieves the audio file name using {@link ResourceLoader#getAudioFileName(int)} and attempts
     * to locate the file in the application's resource directory. If found, the file is copied to the
     * specified destination path. The method logs progress and errors to the console.
     *
     * @param audioID     The unique identifier for the audio file to download.
     * @param destination The {@link Path} where the downloaded file will be saved.
     */
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

    /**
     * Shuts down the download manager's thread pool.
     * <p>
     * This method should be called when downloads are no longer needed or when the
     * application is shutting down. It ensures that all running tasks are terminated
     * and no new tasks are accepted.
     */
    public static void shutdown() {
        executor.shutdown();
    }
}
