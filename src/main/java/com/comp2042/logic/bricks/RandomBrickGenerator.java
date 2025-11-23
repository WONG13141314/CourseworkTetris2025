package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

// Generates random bricks using 7-bag system
public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;
    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    public RandomBrickGenerator() {
        brickList = new ArrayList<>();
        brickList.add(new IBrick());
        brickList.add(new JBrick());
        brickList.add(new LBrick());
        brickList.add(new OBrick());
        brickList.add(new SBrick());
        brickList.add(new TBrick());
        brickList.add(new ZBrick());

        fillBag();
        fillBag();
    }

    // Fill bag with shuffled set of all 7 brick types
    private void fillBag() {
        List<Brick> newBag = new ArrayList<>(brickList);
        Collections.shuffle(newBag);
        nextBricks.addAll(newBag);
    }

    @Override
    public Brick getBrick() {
        if (nextBricks.size() <= 7) {
            fillBag();
        }
        return nextBricks.poll();
    }

    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }

    public void reset() {
        nextBricks.clear();
        fillBag();
        fillBag();
    }
}