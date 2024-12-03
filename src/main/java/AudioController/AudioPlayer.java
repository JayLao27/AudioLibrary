package AudioController;

import AudioController.controllers.HomeScene;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AudioPlayer {

    public static void playAudio(int audioID) {
        String audioFileName = null;

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
            String filePath = "/audioFiles/" + audioFileName;
            URL resource = PlaybackController.class.getResource(filePath);

            if (resource != null) {
                PlaybackController.getInstance().playAudio(resource.toString());
            } else {
                System.out.println("Audio file not found in resources: " + filePath);
            }
        } else {
            System.out.println("No audio found for audioID: " + audioID);
        }

    }

}
