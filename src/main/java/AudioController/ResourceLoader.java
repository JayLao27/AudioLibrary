package AudioController;

import java.io.File;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class ResourceLoader {

    public static String getUsername(int userID) {
        String query = "SELECT userName FROM User" +
                " WHERE userID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("userName");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving username from database.");
        }
        return null;
    }

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
            System.out.println("Error retrieving artist image path from database.");
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
            System.out.println("Error retrieving audio image path from database.");
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
            System.out.println("Error retrieving audio name from database.");
        }
        return null;
    }

    public static Double getAudioPrice(int audioID) {
        String query = "SELECT audioPrice FROM Audio WHERE audioID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, audioID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println(resultSet.getDouble("audioPrice"));
                return resultSet.getDouble("audioPrice");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving audio price from database.");
        }
        return null;
    }

    public static int getAudioDuration(int audioID) {
        String query = "SELECT audioDuration FROM Audio WHERE audioID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, audioID);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("audioDuration");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving duration from database.");
        }
        return 0; // Return 0 if no duration is found
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

    public static String getPaymentDate(int paymentID) {
        String query = "SELECT paymentDate FROM Payments WHERE paymentID = ?";

        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, paymentID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("paymentDate");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving payment date from database.");
        }

        return null;
    }

    public static String getPlaylistName(int playlistID) {
        String query = "SELECT playlistName FROM Playlists WHERE playlistID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, playlistID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Returning name: " + resultSet.getString("playlistName"));
                return resultSet.getString("playlistName");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving playlist name from database.");
        }
       return null;
    }

    public static String getPlaylistImagePath(int playlistID) {
        String query = "SELECT playlistImageFile FROM Playlists WHERE playlistID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, playlistID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String imageFileName = resultSet.getString("playlistImageFile");

                if (imageFileName != null && !imageFileName.isEmpty()) {
                    // Create File object to handle both relative and absolute paths
                    File file = new File(imageFileName);

                    // If the path is not absolute, try to resolve it relative to the project's base directory
                    if (!file.isAbsolute()) {
                        // Get the base directory of the project (adjust this path as needed)
                        String basePath = System.getProperty("user.dir") + File.separator + "ProjectImages";
                        file = new File(basePath, imageFileName);
                    }

                    // Check if the file exists
                    if (file.exists() && file.isFile()) {
                        return file.getAbsolutePath();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving playlist image path from database.");
        }

        // Return default image path (as an absolute path)
        return new File(System.getProperty("user.dir") + File.separator + "ProjectImages" + File.separator + "Vector.png").getAbsolutePath();
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
