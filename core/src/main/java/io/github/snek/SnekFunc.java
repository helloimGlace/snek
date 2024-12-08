package io.github.snek;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static io.github.snek.GameScreen.*;

// Functions for the snek.
public class SnekFunc {
    final snek game;

    // Direction "enums"
    private static final char RIGHT = 'r';
    private static final char LEFT = 'l';
    private static final char UP = 'u';
    private static final char DOWN = 'd';

    public SnekFunc(snek game) {
        this.game = game;
    }

    // The snek's movement logic function.
    public static void moveSnek() {
        // Move the snek.
        snekXBeforeUpdate = snekX;
        snekYBeforeUpdate = snekY;
        switch (direction) {
            case RIGHT: {
                snekX += grid;
                return;
            }
            case LEFT: {
                snekX -= grid;
                return;
            }
            case UP: {
                snekY += grid;
                return;
            }
            case DOWN: {
                snekY -= grid;
            }
        }
    }

    // Check for snek death.
    public static boolean checkForDeath() {
        for (BodyPart bodyPart: bodyParts) {
            if (bodyPart.x == snekX && bodyPart.y == snekY) {
                return true;
            }
        }
        return false;
    }

    // Execute snek death.
    public static void snekDead(float delta) {
        stateTime += delta;
        timeElapsed += delta;
        TextureRegion currentFrame = snekDeathAnim.getKeyFrame(stateTime, true);
        snek.batch.draw(currentFrame, snekX, snekY, grid, grid);
    }

    // Check for out of bounds.
    public static void checkForOutOfBounds() {
        if (snekX >= snek.viewport.getWorldWidth()) {
            snekX = 0;
        }
        if (snekX < 0) {
            snekX = (int)snek.viewport.getWorldWidth() - grid;
        }
        if (snekY >= snek.viewport.getWorldHeight()) {
            snekY = 0;
        }
        if (snekY < 0) {
            snekY = (int)snek.viewport.getWorldWidth() - grid;
        }
    }
}
