package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Generates random bricks using the 7-bag system.
 * Ensures fair distribution by shuffling all 7 brick types into bags.
 */
public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;
    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    /**
     * Constructs a new generator and fills initial bags.
     * Starts with 14 bricks (2 full bags) ready.
     */
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

    /**
     * Fills bag with shuffled set of all 7 brick types.
     * Ensures each type appears exactly once per bag.
     */
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

    @Override
    public void reset() {
        nextBricks.clear();
        fillBag();
        fillBag();
    }
}