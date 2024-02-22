package org.example.util;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.example.entity.Song;
import org.example.model.SongModel;

public class FileChooser {
    public CompletableFuture<Song> selectSong() {
        System.out.println("Select song");

        CompletableFuture<Song> future = new CompletableFuture<>();

        SwingUtilities.invokeLater(() -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("MP3 files", "mp3"));
            int returnValue = fileChooser.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath();
                String title = selectedFile.getName().replaceFirst("[.][^.]+$", "");
                List<Song> allSongs = SongModel.getAll();
                int size = allSongs.size();
                Song song = new Song(size + 1, title, "Unknown", filePath);
                System.out.println("Selected song: " + song.getTitle());

                future.complete(song); // Complete the future with the selected song
            } else {
                future.complete(null); // Complete the future with null if no song is selected
            }
        });

        return future;
    }
}
