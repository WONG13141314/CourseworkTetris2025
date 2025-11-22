package com.comp2042.model.scoring;

import com.comp2042.enums.GameMode;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.prefs.Preferences;

public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty highScore = new SimpleIntegerProperty(0);
    private final Preferences prefs;
    private final GameMode gameMode;

    public Score(GameMode gameMode) {
        this.gameMode = gameMode;
        prefs = Preferences.userNodeForPackage(Score.class);
        loadHighScore();
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public IntegerProperty highScoreProperty() {
        return highScore;
    }

    public void add(int i) {
        int newScore = score.getValue() + i;
        score.setValue(newScore);

        if (newScore > highScore.getValue()) {
            highScore.setValue(newScore);
            saveHighScore();
        }
    }

    private String getHighScoreKey() {
        return gameMode == GameMode.ZEN ? "zen_high_score" : "blitz_high_score";
    }

    private void loadHighScore() {
        try {
            int savedHighScore = prefs.getInt(getHighScoreKey(), 0);
            highScore.setValue(savedHighScore);
        } catch (Exception e) {
            highScore.setValue(0);
        }
    }

    private void saveHighScore() {
        try {
            prefs.putInt(getHighScoreKey(), highScore.getValue());
            prefs.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        score.setValue(0);
    }

    public GameMode getGameMode() {
        return gameMode;
    }
}