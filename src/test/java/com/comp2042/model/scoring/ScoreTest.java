package com.comp2042.model.scoring;

import com.comp2042.enums.GameMode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {
    private Score zenScore;
    private Score blitzScore;

    @BeforeEach
    void setUp() {
        zenScore = new Score(GameMode.ZEN);
        blitzScore = new Score(GameMode.BLITZ);
    }

    @Test
    void testInitialScore_IsZero() {
        assertEquals(0, zenScore.scoreProperty().get(), "Initial score should be 0");
    }

    @Test
    void testAdd_IncreasesScore() {
        zenScore.add(100);
        assertEquals(100, zenScore.scoreProperty().get());
    }

    @Test
    void testAdd_MultipleAdditions() {
        zenScore.add(50);
        zenScore.add(30);
        zenScore.add(20);
        assertEquals(100, zenScore.scoreProperty().get(), "Score should accumulate");
    }

    @Test
    void testHighScore_UpdatesWhenExceeded() {
        int initialHigh = zenScore.highScoreProperty().get();
        zenScore.add(initialHigh + 100);

        assertEquals(initialHigh + 100, zenScore.highScoreProperty().get(),
                "High score should update when exceeded");
    }

    @Test
    void testHighScore_DoesNotDecreaseOnReset() {
        zenScore.add(1000);
        int highScore = zenScore.highScoreProperty().get();
        zenScore.reset();

        assertEquals(0, zenScore.scoreProperty().get(), "Score should reset to 0");
        assertEquals(highScore, zenScore.highScoreProperty().get(),
                "High score should persist after reset");
    }

    @Test
    void testGameMode_ZenAndBlitzSeparate() {
        assertNotEquals(zenScore, blitzScore,
                "Zen and Blitz should maintain separate scores");
        assertEquals(GameMode.ZEN, zenScore.getGameMode());
        assertEquals(GameMode.BLITZ, blitzScore.getGameMode());
    }

    @Test
    void testReset_ClearsCurrentScore() {
        zenScore.add(500);
        zenScore.reset();
        assertEquals(0, zenScore.scoreProperty().get());
    }

    @Test
    void testScoreProperty_IsObservable() {
        assertNotNull(zenScore.scoreProperty(),
                "Score property should be observable for binding");
        assertNotNull(zenScore.highScoreProperty(),
                "High score property should be observable for binding");
    }
}
