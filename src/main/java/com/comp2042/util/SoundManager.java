package com.comp2042.util;

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
    private Clip gameOverMusic;
    private boolean isMusicPlaying = false;
    private boolean isGameOverMusicPlaying = false;

    private static final float BACKGROUND_MUSIC_VOLUME = 0.8f;
    private static final float CLEAR_ROW_VOLUME = 0.9f;
    private static final float GAME_OVER_VOLUME = 0.8f;

    public SoundManager() {
        soundExecutor = Executors.newFixedThreadPool(3, new ThreadFactory() {
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
                if (audioSrc == null) {
                    System.err.println("Background music file not found!");
                    return;
                }

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
                e.printStackTrace();
            }
        });
    }

    public void playClearRow() {
        soundExecutor.submit(() -> {
            try {
                InputStream audioSrc = getClass().getClassLoader().getResourceAsStream("sounds/clear_row.wav");
                if (audioSrc == null) {
                    System.err.println("Clear row sound file not found!");
                    return;
                }

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
                e.printStackTrace();
            }
        });
    }

    public void playGameOverMusic() {
        if (isGameOverMusicPlaying) return;

        soundExecutor.submit(() -> {
            try {
                stopGameOverMusic();

                InputStream audioSrc = getClass().getClassLoader().getResourceAsStream("sounds/game_over.wav");
                if (audioSrc == null) {
                    System.err.println("Game over music file not found! Looking for: sounds/game_over.wav");
                    return;
                }

                InputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

                gameOverMusic = AudioSystem.getClip();
                gameOverMusic.open(audioStream);

                if (gameOverMusic.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl gainControl = (FloatControl) gameOverMusic.getControl(FloatControl.Type.MASTER_GAIN);
                    float range = gainControl.getMaximum() - gainControl.getMinimum();
                    float gain = (range * GAME_OVER_VOLUME) + gainControl.getMinimum();
                    gainControl.setValue(gain);
                }

                gameOverMusic.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        isGameOverMusicPlaying = false;
                    }
                });

                gameOverMusic.loop(Clip.LOOP_CONTINUOUSLY);
                gameOverMusic.start();
                isGameOverMusicPlaying = true;

            } catch (Exception e) {
                System.err.println("Error playing game over music: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void stopGameOverMusic() {
        if (gameOverMusic != null) {
            gameOverMusic.stop();
            gameOverMusic.close();
            gameOverMusic = null;
        }
        isGameOverMusicPlaying = false;
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