package AudioController;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AudioPlayer {

    public static void playAudio(int audioID) {
        String audioFileName = null;

        // Query to get the audio file name from the database
        String query = "SELECT audioFileName FROM Audio WHERE audioID = ?";

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, audioID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    audioFileName = rs.getString("audioFileName");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (audioFileName != null) {
            // Get the URI of the audio file in the resources folder
            String filePath = "/audioFiles/" + audioFileName; // Assuming your audio files are in the 'resources/audioFiles' folder
            URL resource = PlaybackController.class.getResource(filePath); // Replace YourClassName with the actual class name

            if (resource != null) {
                // Initialize the MediaPlayer through the AudioPlayer singleton
                PlaybackController.getInstance().playAudio(resource.toString());  // Passing the file path to the AudioPlayer singleton
            } else {
                System.out.println("Audio file not found in resources: " + filePath);
            }
        } else {
            System.out.println("No audio found for audioID: " + audioID);
        }
    }


}
