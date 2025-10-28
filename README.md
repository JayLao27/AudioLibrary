# AudioLibrary

A comprehensive JavaFX-based music library management system that allows users to browse, purchase, manage, and play audio tracks with an intuitive graphical interface.

## Description

AudioLibrary is a desktop application built with JavaFX that provides a complete music library experience. Users can:

- **Browse & Discover**: Explore audio tracks by artists, albums, and genres
- **Purchase Music**: Add tracks to cart and purchase using virtual currency
- **Library Management**: Organize purchased tracks in personal library
- **Playlist Creation**: Create and manage custom playlists
- **Audio Playback**: Play music with controls for play/pause, skip, shuffle, and loop
- **Download**: Save purchased tracks to local storage
- **User Profiles**: Manage account balance and view payment history

The application uses a MySQL database backend to store user data, audio metadata, purchases, and playlists.

## Technologies Used

- **Java 17+**
- **JavaFX** - UI framework
- **MySQL** - Database
- **JDBC** - Database connectivity
- **Maven** - Build and dependency management
- **FXML** - UI layout definitions

## Prerequisites

Before running this application, ensure you have the following installed:

1. **Java Development Kit (JDK) 17 or higher**
   - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
   - Verify installation: `java -version`

2. **MySQL Server**
   - Download from [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)
   - Default port: 3306

3. **Maven** (optional - included via Maven Wrapper)
   - Download from [Apache Maven](https://maven.apache.org/download.cgi)

## Database Setup

1. **Start MySQL Server**
   - Ensure MySQL is running on `localhost:3306`

2. **Create Database**
   ```sql
   CREATE DATABASE NullPointers_db;
   ```

3. **Import Database Schema**
   - Locate the SQL schema file in `src/main/resources/SQLs` directory
   - Import the schema:
   ```bash
   mysql -u root -p NullPointers_db < src/main/resources/SQLs/schema.sql
   ```
   
4. **Configure Database Credentials**
   - Open `src/main/java/AudioController/DatabaseConnection.java`
   - Update credentials if needed (default: user=`root`, password=``)
   ```java
   private static final String DB_URL = "jdbc:mysql://localhost:3306/NullPointers_db";
   private static final String DB_USER = "root";
   private static final String DB_PASSWORD = "";
   ```

## How to Run

### Method 1: Using Maven Wrapper (Recommended)

**On Windows:**
```bash
# Navigate to project directory
cd AudioLibrary

# Run the application
mvnw.cmd clean javafx:run
```

**On Linux/Mac:**
```bash
# Navigate to project directory
cd AudioLibrary

# Make the wrapper executable
chmod +x mvnw

# Run the application
./mvnw clean javafx:run
```

### Method 2: Using IDE (IntelliJ IDEA / Eclipse)

1. **Import Project**
   - Open your IDE
   - Select `File → Open` and choose the `AudioLibrary` folder
   - IDE will automatically detect it as a Maven project

2. **Configure JavaFX**
   - Ensure JavaFX SDK is configured in your IDE
   - Maven should handle dependencies automatically

3. **Run Application**
   - Locate `src/main/java/AudioController/Main.java`
   - Right-click and select `Run Main.main()`

### Method 3: Using Maven CLI

```bash
# Navigate to project directory
cd AudioLibrary

# Compile the project
mvn clean compile

# Run the application
mvn javafx:run
```

## Project Structure

```
AudioLibrary/
├── src/
│   └── main/
│       ├── java/
│       │   ├── AudioController/
│       │   │   ├── Main.java                    # Application entry point
│       │   │   ├── AudioPlayer.java             # Audio playback manager
│       │   │   ├── DatabaseConnection.java      # Database connection
│       │   │   ├── ResourceLoader.java          # Resource utility
│       │   │   ├── DownloadManager.java         # Audio download manager
│       │   │   └── controllers/                 # FXML controllers
│       │   │       ├── HomeScene.java
│       │   │       ├── LibraryScene.java
│       │   │       ├── PlaylistContentsScene.java
│       │   │       ├── SongPageScene.java
│       │   │       └── ...
│       │   └── module-info.java
│       └── resources/
│           ├── audioFiles/                      # Audio file storage
│           ├── coverArt/                        # Album/track artwork
│           ├── artistImages/                    # Artist images
│           ├── CSS/                             # Stylesheets
│           ├── FXMLs/                           # UI layout files
│           ├── ProjectImages/                   # UI assets
│           └── SQLs/                            # Database scripts
├── pom.xml                                      # Maven configuration
├── mvnw                                         # Maven wrapper (Unix)
├── mvnw.cmd                                     # Maven wrapper (Windows)
└── README.md
```

## Key Features

### User Management
- User registration and authentication
- Account balance management
- Payment history tracking

### Audio Library
- Browse all available tracks
- View by artist, album, or genre
- Purchase tracks and add to personal library
- View detailed audio information

### Playback Controls
- Play/Pause functionality
- Skip to next/previous track
- Shuffle and loop modes
- Volume control
- Queue management

### Playlists
- Create custom playlists
- Add/remove tracks from playlists
- Edit playlist names and artwork
- Play entire playlists

### Shopping Cart & Checkout
- Add tracks to cart
- View cart contents
- Process payments
- Automatic library updates after purchase

## Configuration

### Audio File Storage
Audio files should be placed in: `src/main/resources/audioFiles`

### Cover Art & Images
- Album/track art: `src/main/resources/coverArt`
- Artist images: `src/main/resources/artistImages`
- UI images: `src/main/resources/ProjectImages`

## Troubleshooting

### Database Connection Issues
- Verify MySQL is running on port 3306
- Check credentials in `DatabaseConnection.java`
- Ensure database `NullPointers_db` exists

### JavaFX Runtime Errors
- Ensure JDK 17+ is being used
- Verify JavaFX dependencies in `pom.xml`
- Check `src/main/java/module-info.java` exports and opens declarations

### Audio Playback Issues
- Verify audio files exist in `resources/audioFiles/`
- Ensure audio files are in supported format (MP3 recommended)
- Check `AudioPlayer.java` for file path resolution

## Contributing

This project was developed by the NullPointers team. Contributions, issues, and feature requests are welcome!


**Note**: Make sure all audio files and database are properly set up before running the application for the first time.
