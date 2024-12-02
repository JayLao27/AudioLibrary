package AudioController;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class PlaybackController {

    private static PlaybackController instance;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;


    private PlaybackController() {}

    public static PlaybackController getInstance() {
        if (instance == null) {
            instance = new PlaybackController();
        }
        return instance;
    }

    public void playAudio(String filePath) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        Media media = new Media(filePath);
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setOnEndOfMedia(() -> {
            isPlaying = false;
        });

        mediaPlayer.play();
        isPlaying = true;
    }

    public void togglePlayPause() {
        if (mediaPlayer != null) {
            if (isPlaying) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.play();
            }
            isPlaying = !isPlaying;
        }
    }

    public void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            isPlaying = false;
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

}
