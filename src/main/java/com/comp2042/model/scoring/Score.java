package com.comp2042.model.scoring;

import com.comp2042.enums.GameMode;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.prefs.Preferences;

/**
 * Manages game scoring system with persistent high scores.
 * Tracks current score and high score separately for each game mode.
 */
public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty highScore = new SimpleIntegerProperty(0);
    private final Preferences prefs;
    private final GameMode gameMode;

    /**
     * Constructs a Score manager for the specified game mode.
     * Automatically loads saved high score from preferences.
     *
     * @param gameMode the game mode (ZEN or BLITZ)
     */
    public Score(GameMode gameMode) {
        this.gameMode = gameMode;
        prefs = Preferences.userNodeForPackage(Score.class);
        loadHighScore();
    }

    /**
     * Gets the score property for binding.
     *
     * @return score property
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Gets the high score property for binding.
     *
     * @return high score property
     */
    public IntegerProperty highScoreProperty() {
        return highScore;
    }

    /**
     * Adds points to current score.
     * Automatically updates high score if exceeded.
     *
     * @param points points to add
     */
    public void add(int points) {
        int newScore = score.getValue() + points;
        score.setValue(newScore);

        if (newScore > highScore.getValue()) {
            highScore.setValue(newScore);
            saveHighScore();
        }
    }

    /**
     * Gets the preference key for current game mode.
     *
     * @return preference key string
     */
    private String getHighScoreKey() {
        return gameMode == GameMode.ZEN ? "zen_high_score" : "blitz_high_score";
    }

    /**
     * Loads high score from persistent storage.
     */
    private void loadHighScore() {
        try {
            int savedHighScore = prefs.getInt(getHighScoreKey(), 0);
            highScore.setValue(savedHighScore);
        } catch (Exception e) {
            highScore.setValue(0);
        }
    }

    /**
     * Saves high score to persistent storage.
     */
    private void saveHighScore() {
        try {
            prefs.putInt(getHighScoreKey(), highScore.getValue());
            prefs.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets current score to zero.
     * High score is preserved.
     */
    public void reset() {
        score.setValue(0);
    }

    /**
     * Gets the game mode for this score manager.
     *
     * @return game mode
     */
    public GameMode getGameMode() {
        return gameMode;
    }
}