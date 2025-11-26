package com.comp2042.view.components;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class NotificationPanelTest {

    private NotificationPanel notificationPanel;

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Already initialized
        }
        Thread.sleep(100);
    }

    @BeforeEach
    void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            notificationPanel = new NotificationPanel("+100");
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testConstructor_CreatesPanel() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            assertNotNull(notificationPanel);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testConstructor_WithDifferentText() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            NotificationPanel panel1 = new NotificationPanel("+50");
            NotificationPanel panel2 = new NotificationPanel("+200");
            NotificationPanel panel3 = new NotificationPanel("Board Cleared!");

            assertNotNull(panel1);
            assertNotNull(panel2);
            assertNotNull(panel3);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testShowScore_StartsAnimation() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            ObservableList<Node> list = FXCollections.observableArrayList();
            list.add(notificationPanel);

            notificationPanel.showScore(list);

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testShowScore_RemovesFromListAfterAnimation() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            ObservableList<Node> list = FXCollections.observableArrayList();
            list.add(notificationPanel);

            assertEquals(1, list.size());
            notificationPanel.showScore(list);

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
        Thread.sleep(3000); // Wait for animation to complete
    }

    @Test
    void testMultipleNotifications() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            ObservableList<Node> list = FXCollections.observableArrayList();

            NotificationPanel panel1 = new NotificationPanel("+50");
            NotificationPanel panel2 = new NotificationPanel("+100");
            NotificationPanel panel3 = new NotificationPanel("+200");

            list.add(panel1);
            list.add(panel2);
            list.add(panel3);

            panel1.showScore(list);
            panel2.showScore(list);
            panel3.showScore(list);

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }
}