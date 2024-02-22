package org.example.controller;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.example.entity.Song;
import org.example.model.SongModel;
import javafx.stage.FileChooser;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PlayerFormController extends Application {

    public Button btnAddMusic;
    public Button btnPlay;
    public Button btnStop;
    public Button btnNext;
    public Label lblSongTitle;
    public Button btnPause;
    public Button btnResume;
    public Label lblNotification;
    public Button btnAddToFav;
    public Label lblCountdown;
    FileChooser fileChooser = new FileChooser();
    private Clip clip;
    private MediaPlayer currentMediaPlayer;
    private Song currentSong;
    private Song selectedSong;


    public void btnAddMusicOnAction(ActionEvent actionEvent) throws IOException {
        start(new Stage());
        if (selectedSong != null) {
            lblSongTitle.setText(selectedSong.getTitle());
            currentSong = selectedSong;
        }
    }

    @FXML
    private void btnPlayOnAction(ActionEvent actionEvent) {
        if (currentSong != null) {
            try {
                String filePath = currentSong.getFilePath();
                Media media = new Media(new File(filePath).toURI().toString());
                if (currentMediaPlayer != null) {
                    currentMediaPlayer.stop();
                }
                currentMediaPlayer = new MediaPlayer(media);
                currentMediaPlayer.play();
                //setCoverImage(filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            lblNotification.setText("Please select a song or click 'next' to play your favorite playlist..");
            new Alert(Alert.AlertType.WARNING, "Please select a song or click 'next' to play your favorite playlist..").show();
        }
    }


    public void btnStopOnAction(ActionEvent actionEvent) {
        if (currentMediaPlayer != null) {
            currentMediaPlayer.stop();
        } else {
            lblNotification.setText("No media player is currently playing");
            System.out.println("No media player is currently playing");
        }
    }


    public void btnNextOnAction(ActionEvent actionEvent) {
        Song nextSong = SongModel.getNextSong();
        if (nextSong != null) {
            lblSongTitle.setText(nextSong.getTitle());
            currentSong = nextSong;
            btnPlayOnAction(actionEvent);
        }
    }


    public void btnPauseOnAction(ActionEvent actionEvent) {
        if (currentMediaPlayer != null) {
            currentMediaPlayer.pause();
        } else {
            lblNotification.setText("No media player is currently playing");
            System.out.println("No media player is currently playing");
        }
    }

    public void btnResumeOnAction(ActionEvent actionEvent) {
        if (currentMediaPlayer != null) {
            currentMediaPlayer.play();
        } else {
            lblNotification.setText("No media player is currently playing");
            System.out.println("No media player is currently playing");
        }
    }

    public void btnAddToFavOnAction(ActionEvent actionEvent) {
        boolean isAlreadyAdded = false;
        boolean isSaved = false;
        if(currentSong != null){
            List<Song> allFavs = SongModel.getAll();
            for (Song fav : allFavs) {
                if (fav.getFilePath().equals(currentSong.getFilePath())) {
                    new Alert(Alert.AlertType.WARNING, "Song already in your favorites").show();
                    lblNotification.setText("Song already in your favorites");
                    isAlreadyAdded=true;
                }
            }
            if(isAlreadyAdded==false){
                isSaved = SongModel.saveSong(currentSong);
            }
            if(isSaved){
                lblNotification.setText("Song added to favorites");
            }
        }else{
            lblNotification.setText("No any song selected");
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 files", "*.mp3"));
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            String title = selectedFile.getName().replaceFirst("[.][^.]+$", "");
            List<Song> allSongs = SongModel.getAll(); // Assuming SongModel.getAll() is accessible
            int size = allSongs.size();
            Song song = new Song(size +   1, title, "Unknown", filePath);
            selectedSong=song;
        }
    }
}
