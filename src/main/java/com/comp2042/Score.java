package com.comp2042;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.prefs.Preferences;

public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty highScore = new SimpleIntegerProperty(0);
    private final Preferences prefs;

    public Score() {
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

    public void reset() {
        score.setValue(0);
    }

    private void loadHighScore() {
        try {
            int savedHighScore = prefs.getInt("tetris_high_score", 0);
            highScore.setValue(savedHighScore);
        } catch (Exception e) {
            highScore.setValue(0);
        }
    }

    private void saveHighScore() {
        try {
            prefs.putInt("tetris_high_score", highScore.getValue());
            prefs.flush();
        } catch (Exception e) {
        }
    }
}
