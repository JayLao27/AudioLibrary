CREATE DATABASE IF NOT EXISTS NullPointers_db;

USE NullPointers_db;

CREATE TABLE IF NOT EXISTS User (
    userID INT AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(100) NOT NULL UNIQUE,
    firstName VARCHAR(100),
    lastName VARCHAR(100),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    balance DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS Artists (
    artistID INT AUTO_INCREMENT PRIMARY KEY,
    artistName VARCHAR(255) NOT NULL UNIQUE,
    artistImageFile VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Genre (
    genreID INT AUTO_INCREMENT PRIMARY KEY,
    genreName VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Albums (
    albumID INT AUTO_INCREMENT PRIMARY KEY,
    albumName VARCHAR(255) NOT NULL,
    artistID INT NOT NULL,
    FOREIGN KEY (artistID) REFERENCES Artists(artistID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Audio (
    audioID INT AUTO_INCREMENT PRIMARY KEY,
    audioName VARCHAR(255) NOT NULL,
    audioDuration INT NOT NULL,
    audioFileName VARCHAR(255) NOT NULL,
    audioImageFileName VARCHAR(255) NOT NULL,
    audioPrice DECIMAL(10, 2) NOT NULL,
    artistID INT,
    albumID INT,
    genreID INT,
    FOREIGN KEY (artistID) REFERENCES Artists(artistID) ON DELETE SET NULL,
    FOREIGN KEY (albumID) REFERENCES Albums(albumID) ON DELETE SET NULL,
    FOREIGN KEY (genreID) REFERENCES Genre(genreID) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS LibraryAudio (
    userID INT NOT NULL,
    audioID INT NOT NULL,
    PRIMARY KEY (userID, audioID),
    FOREIGN KEY (userID) REFERENCES User(userID) ON DELETE CASCADE,
    FOREIGN KEY (audioID) REFERENCES Audio(audioID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS CartAudio (
    userID INT NOT NULL,
    audioID INT NOT NULL,
    PRIMARY KEY (userID, audioID),
    FOREIGN KEY (userID) REFERENCES User(userID) ON DELETE CASCADE,
    FOREIGN KEY (audioID) REFERENCES Audio(audioID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Playlists(
    playlistID INT AUTO_INCREMENT PRIMARY KEY,
    playlistName VARCHAR(255),
    playlistImageFile VARCHAR(255),
    userID INT NOT NULL,
    FOREIGN KEY (userID) REFERENCES User(userID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS PlaylistAudio (
    playlistID INT NOT NULL,
    audioID INT NOT NULL,
    PRIMARY KEY (playlistID, audioID),
    FOREIGN KEY (playlistID) REFERENCES Playlists(playlistID) ON DELETE CASCADE,
    FOREIGN KEY (audioID) REFERENCES Audio(audioID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Payments (
    paymentID INT AUTO_INCREMENT PRIMARY KEY,
    userID INT NOT NULL,
    paymentDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(10, 2) NOT NULL,
    paymentMethod VARCHAR(100) NOT NULL,
    FOREIGN KEY (userID) REFERENCES User(userID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS PaymentAudio (
    paymentID INT NOT NULL,
    audioID INT NOT NULL,
    PRIMARY KEY (paymentID, audioID),
    FOREIGN KEY (paymentID) REFERENCES Payments(paymentID) ON DELETE CASCADE,
    FOREIGN KEY (audioID) REFERENCES Audio(audioID) ON DELETE CASCADE
);
