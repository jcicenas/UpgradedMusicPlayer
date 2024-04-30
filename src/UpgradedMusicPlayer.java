import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class UpgradedMusicPlayer {
    private Clip clip;
    private Timer timer;
    private long currentPosition = 0;
    private String playingSong;

    public UpgradedMusicPlayer() {
        String[] songList = {"Dumbbells.wav", "Hey Papi.wav", "Code Kings.wav", "Fire in My Belly.wav", "For Loop.wav", "GORG.wav", "GUI Mastermind.wav", "Hashin_ in the Code.wav",
                "GUI Mastermind.wav", "Mr. Scott.wav", "Programming.wav", "The Boolean Blues.wav", "The Codebreaker_s Fury.wav"};

        JFrame frame = new JFrame("Upgraded Music Player");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        JPanel buttonPanel = new JPanel();
        JButton playButton = new JButton("Play");
        JButton pauseButton = new JButton("Pause");
        JButton rewindButton = new JButton("-15s");
        JButton forwardButton = new JButton("+15s");
        buttonPanel.add(rewindButton);
        buttonPanel.add(playButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(forwardButton);

        JPanel labelPanel = new JPanel();
        JLabel currentTime = new JLabel("Current Time: 0s");
        JLabel totalTime = new JLabel("Total Time: 0s");
        labelPanel.add(currentTime);
        labelPanel.add(totalTime);

        JPanel dropDown = new JPanel();
        JComboBox<String> songSelector = new JComboBox<>(songList);
        dropDown.add(songSelector);

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                if (clip == null) {
                    playMusic(songSelector.getSelectedItem().toString());
                    startTimer();
                    updateLabels();
                    playingSong = songSelector.getSelectedItem().toString();
                } else if (!clip.isRunning()) {
                    if (songSelector.getSelectedItem().toString().equals(playingSong)) {
                        if (clip.getMicrosecondPosition() == clip.getMicrosecondLength()) {
                            playMusic(songSelector.getSelectedItem().toString());
                            clip.setMicrosecondPosition(0);
                            startTimer();
                            updateLabels();
                            playingSong = songSelector.getSelectedItem().toString();
                        } else {
                            playMusic(songSelector.getSelectedItem().toString());
                            startTimer();
                            updateLabels();
                            playingSong = songSelector.getSelectedItem().toString();
                        }
                    } else {
                        playMusic(songSelector.getSelectedItem().toString());
                        clip.setMicrosecondPosition(0);
                        startTimer();
                        updateLabels();
                        playingSong = songSelector.getSelectedItem().toString();
                    }
                } else {
                    clip.stop();
                    playMusic(songSelector.getSelectedItem().toString());
                    clip.setMicrosecondPosition(0);
                    startTimer();
                    updateLabels();
                    playingSong = songSelector.getSelectedItem().toString();
                }
            }
        });

        forwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                forward(songSelector.getSelectedItem().toString());
                currentPosition = clip.getMicrosecondPosition();
            }
        });

        rewindButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){
                rewind(songSelector.getSelectedItem().toString());
                currentPosition = clip.getMicrosecondPosition();
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event){;
                pauseMusic(songSelector.getSelectedItem().toString());
                stopTimer();
            }
        });

        panel.add(dropDown);
        panel.add(buttonPanel);
        panel.add(labelPanel);

        frame.add(panel);
        frame.setVisible(true);
    }

    public void forward(String selectedSong){
        if (clip.getMicrosecondPosition() + 15000000 > clip.getMicrosecondLength()) {
            clip.setMicrosecondPosition(clip.getMicrosecondLength());
            updateLabels();
        } else {
            clip.setMicrosecondPosition(currentPosition + 15000000);
        }
    }

    public void rewind(String selectedSong){
        if (clip.getMicrosecondPosition() - 15000000 < 0) {
            clip.setMicrosecondPosition(0);
            updateLabels();
        } else {
            clip.setMicrosecondPosition(currentPosition - 15000000);
        }
    }

    public void playMusic(String selectedSong){
        try {
            File file = new File(selectedSong);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.setMicrosecondPosition(currentPosition);
            clip.start();
        }
        catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void pauseMusic(String selectedSong){
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void startTimer(){
        timer = new Timer(1000, new ActionListener(){
            public void actionPerformed(ActionEvent e){
                updateLabels();
            }
        });
        timer.start();
    }

    public void updateLabels(){
        if (clip != null && clip.isOpen()) {
            if (clip.isRunning()) {
                // Convert microseconds to seconds for display
                long currentTimeSeconds = clip.getMicrosecondPosition() / 1000000;
                long totalTimeSeconds = clip.getMicrosecondLength() / 1000000;
                System.out.println(currentTimeSeconds + "s / " + totalTimeSeconds + "s");
            }
        }
    }

    public void stopTimer(){
        if (timer != null) {
            currentPosition = clip.getMicrosecondPosition();
            timer.stop();
        }
    }

    public static void main(String[] args) {
        new UpgradedMusicPlayer();
    }
}
