package com.comp2042;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class SoundManager {
    private static SoundManager instance;
    private ExecutorService soundExecutor;
    private Clip backgroundMusic;
    private boolean isMusicPlaying = false;

    private static final float BACKGROUND_MUSIC_VOLUME = 0.8f;
    private static final float CLEAR_ROW_VOLUME = 0.9f;

    private SoundManager() {
        soundExecutor = Executors.newFixedThreadPool(2, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void playBackgroundMusic() {
        if (isMusicPlaying) return;

        soundExecutor.submit(() -> {
            try {
                stopBackgroundMusic();

                InputStream audioSrc = getClass().getClassLoader().getResourceAsStream("sounds/background_music.wav");
                if (audioSrc == null) return;

                InputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

                backgroundMusic = AudioSystem.getClip();
                backgroundMusic.open(audioStream);

                if (backgroundMusic.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
                    float range = gainControl.getMaximum() - gainControl.getMinimum();
                    float gain = (range * BACKGROUND_MUSIC_VOLUME) + gainControl.getMinimum();
                    gainControl.setValue(gain);
                }

                backgroundMusic.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        isMusicPlaying = false;
                    }
                });

                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
                backgroundMusic.start();
                isMusicPlaying = true;

            } catch (Exception e) {
                System.err.println("Error playing background music: " + e.getMessage());
            }
        });
    }

    public void playClearRow() {
        soundExecutor.submit(() -> {
            try {
                InputStream audioSrc = getClass().getClassLoader().getResourceAsStream("sounds/clear_row.wav");
                if (audioSrc == null) return;

                InputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);

                if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    float range = gainControl.getMaximum() - gainControl.getMinimum();
                    float gain = (range * CLEAR_ROW_VOLUME) + gainControl.getMinimum();
                    gainControl.setValue(Math.min(gain, gainControl.getMaximum()));
                }

                clip.start();

            } catch (Exception e) {
                System.err.println("Error playing clear row sound: " + e.getMessage());
            }
        });
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.close();
            backgroundMusic = null;
        }
        isMusicPlaying = false;
    }

    public void pauseBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    public void resumeBackgroundMusic() {
        if (backgroundMusic != null && !backgroundMusic.isRunning()) {
            backgroundMusic.start();
        } else if (backgroundMusic == null) {
            playBackgroundMusic();
        }
    }
}