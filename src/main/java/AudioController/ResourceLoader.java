package AudioController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ResourceLoader {

    public static String getArtistImagePath(int artistID) {
        String query = "SELECT artistImageFile FROM Artists WHERE artistID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, artistID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String imageFileName = resultSet.getString("artistImageFile");
                return "/artistImages/" + imageFileName;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving artist name from database.");
        }
        return null;
    }

    public static String getArtistName(int artistID) {
        String query = "SELECT artistName FROM Artists WHERE artistID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, artistID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("artistName");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving artist name from database.");
        }
        return null;
    }

    public static String getArtistNamefromAudioID(int audioID) {
        String query = "SELECT a.artistName " +
                "FROM Audio AS au " +
                "JOIN Artists AS a ON au.artistID = a.artistID " +
                "WHERE au.audioID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, audioID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("artistName");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving artist name from database.");
        }
        return null;
    }

    public static String getAudioImagePath(int audioID) {
        String query = "SELECT audioImageFileName FROM Audio WHERE audioID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, audioID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String imageFileName = resultSet.getString("audioImageFileName");
                return "/coverArt/" + imageFileName;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving artist name from database.");
        }
        return null;
    }

    public static String getAudioName(int audioID) {
        String query = "SELECT audioName FROM Audio WHERE audioID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, audioID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("audioName");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving artist name from database.");
        }
        return null;
    }

    public static String getAlbumName(int audioID) {
        String query = "SELECT albumName FROM Albums " +
                "LEFT JOIN Audio ON Audio.albumID = Albums.albumID WHERE audioID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, audioID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Check if the albumName is NULL (which can happen if no album is associated with the audio)
                return resultSet.getString("albumName");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving album name from database.");
        }
        return ""; // Return empty string if no album is found
    }

    public static User getProfile(int userID) {
        String query = "SELECT userName, firstName, lastName, email FROM User WHERE userID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userID); // Set userID in the query
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getString("userName"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("email")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving user profile from database.");
        }
        return null;
    }
}
