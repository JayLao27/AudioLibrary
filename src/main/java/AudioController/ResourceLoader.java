package AudioController;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Utility class that provides methods for loading and retrieving various resources
 * from the database, such as user information, artist details, audio file metadata,
 * playlist details, and more. This class performs SQL queries and returns relevant
 * data related to users, artists, audio, albums, playlists, and payments.
 * <p>
 * Each method handles a specific type of resource retrieval, such as retrieving
 * a user's name, an artist's image path, audio details like price or duration,
 * and more. The results are returned as strings, numbers, or custom objects as required.
 * </p>
 */
public class ResourceLoader {

    /**
     * Retrieves the username associated with the given user ID from the database.
     *
     * @param userID the ID of the user
     * @return the username of the user, or null if not found
     */
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


    /**
     * Retrieves the balance associated with the given user ID from the database.
     *
     * @param userID the ID of the user
     * @return the balance of the user, or null if not found
     */
    public static BigDecimal getBalance(int userID) {
        String query = "SELECT balance FROM User WHERE userID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBigDecimal("balance");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving balance from database.");
        }
        return null;
    }


    /**
     * Retrieves the file path of the artist's image based on the artist ID.
     *
     * @param artistID the ID of the artist
     * @return the path to the artist's image, or null if not found
     */
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


    /**
     * Retrieves the name of the artist associated with the given artist ID.
     *
     * @param artistID the ID of the artist
     * @return the name of the artist, or null if not found
     */
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


    /**
     * Retrieves the artist's name associated with the given audio ID by joining
     * the audio and artist tables.
     *
     * @param audioID the ID of the audio file
     * @return the name of the artist, or null if not found
     */
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


    /**
     * Retrieves the artist's ID associated with the given audio ID by joining
     * the audio and artist tables.
     *
     * @param audioID the ID of the audio file
     * @return the artist's ID, or -1 if not found
     */
    public static int getArtistIDFromAudioID(int audioID) {
        String query = "SELECT au.artistID " +
                "FROM Audio AS au " +
                "JOIN Artists AS a ON au.artistID = a.artistID " +
                "WHERE au.audioID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, audioID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("artistID");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving artist ID from database.");
        }
        return -1;  // Return -1 if artist ID is not found
    }


    /**
     * Retrieves the file path of the audio's image (cover art) based on the given audio ID.
     *
     * @param audioID the ID of the audio file
     * @return the path to the audio's image, or null if not found
     */
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


    /**
     * Retrieves the name of the audio file associated with the given audio ID.
     *
     * @param audioID the ID of the audio file
     * @return the name of the audio file, or null if not found
     */
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


    /**
     * Retrieves the file name of the audio file associated with the given audio ID.
     *
     * @param audioID the ID of the audio file
     * @return the file name of the audio file, or null if not found
     */
    public static String getAudioFileName(int audioID) {
        String query = "SELECT audioFileName FROM Audio WHERE audioID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, audioID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("audioFileName");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving audio name from database.");
        }
        return null;
    }


    /**
     * Retrieves the price of the audio associated with the given audio ID.
     *
     * @param audioID the ID of the audio file
     * @return the price of the audio, or null if not found
     */
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


    /**
     * Retrieves the genre of the audio associated with the given audio ID.
     *
     * @param audioID the ID of the audio file
     * @return the genre of the audio, or empty if not found
     */
    public static String getAudioGenre(int audioID) {
        String query = "SELECT g.genreName " +
                "FROM Audio a " +
                "JOIN Genre g ON a.genreID = g.genreID " +
                "WHERE a.audioID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, audioID);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("genreName");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving genre from database.");
        }
        return "";
    }


    /**
     * Retrieves the duration of the audio associated with the given audio ID.
     *
     * @param audioID the ID of the audio file
     * @return the duration of the audio in seconds, or 0 if not found
     */
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


    /**
     * Retrieves the genre ID associated with the given audio ID.
     *
     * @param audioID the ID of the audio file
     * @return the genre ID of the audio, or -1 if not found
     */
    public static int getGenreID(int audioID) {
        String query = "SELECT genreID " +
                "FROM Audio " +
                "WHERE audioID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, audioID);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("genreID");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving genre ID from database.");
        }
        return -1; // Return -1 if not found
    }


    /**
     * Retrieves the genre name associated with the given genre ID.
     *
     * @param genreID the ID of the genre
     * @return the genre name, or an empty string if not found
     */
    public static String getGenreName(int genreID) {
        String query = "SELECT genreName " +
                "FROM Genre " +
                "WHERE genreID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, genreID);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("genreName");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving genre name from database.");
        }
        return ""; // Return an empty string if not found
    }


    /**
     * Retrieves the name of the album associated with the given audio ID.
     * If no album is associated, an empty string is returned.
     *
     * @param audioID the ID of the audio file
     * @return the name of the album, or null if no album is found
     */
    public static String getAlbumName(int audioID) {
        String query = "SELECT albumName FROM Albums " +
                "LEFT JOIN Audio ON Audio.albumID = Albums.albumID WHERE audioID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, audioID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("albumName");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving album name from database.");
        }
        return null;
    }


    /**
     * Retrieves the album ID associated with the given audio ID.
     *
     * @param audioID the ID of the audio file
     * @return the album ID of the audio, or -1 if not found
     */
    public static int getAlbumID(int audioID) {
        String query = "SELECT albumID " +
                "FROM Audio " +
                "WHERE audioID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, audioID);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("albumID");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving album ID from database.");
        }
        return -1; // Return -1 if not found
    }


    /**
     * Retrieves the payment date associated with the given payment ID.
     *
     * @param paymentID the ID of the payment
     * @return the date of the payment, or null if not found
     */
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


    /**
     * Retrieves the name of the playlist associated with the given playlist ID.
     *
     * @param playlistID the ID of the playlist
     * @return the name of the playlist, or null if not found
     */
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


    /**
     * Retrieves the file path of the playlist's image based on the given playlist ID.
     * If no valid image path is found, a default image path is returned.
     *
     * @param playlistID the ID of the playlist
     * @return the path to the playlist's image, or a default image path if not found
     */
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


    /**
     * Retrieves a user's profile information based on the given user ID.
     * The profile includes the username, first name, last name, email, and balance.
     *
     * @param userID the ID of the user
     * @return a {@link User} object containing the user's profile, or null if not found
     */
    public static User getProfile(int userID) {
        String query = "SELECT userName, firstName, lastName, email, balance FROM User WHERE userID = ?";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getString("userName"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("email"),
                        resultSet.getDouble("balance")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving user profile from database.");
        }
        return null;
    }

}
