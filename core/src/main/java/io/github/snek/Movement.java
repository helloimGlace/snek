package io.github.snek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import static io.github.snek.Direction.*;
import static io.github.snek.GameScreen.*;

public class Movement {
    final snek game;

    // public static Direction prevDirection;

    public Movement(snek game) {
        this.game = game;
    }

    public static void queueInput() {
        if (inputQueue.size < maxInQueue) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                inputQueue.add(UP);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                inputQueue.add(DOWN);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                inputQueue.add(LEFT);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                inputQueue.add(RIGHT);
            }
        }
    }

    // The snek's movement logic function.
    public static void moveSnek() {
        // Move the snek.
        snekXBeforeUpdate = snekX;
        snekYBeforeUpdate = snekY;

        // prevDirection = direction;

        updateDirection();

        switch (direction) {
            case RIGHT: {
                snekX += grid;
                return;
            } case LEFT: {
                snekX -= grid;
                return;
            } case UP: {
                snekY += grid;
                return;
            } case DOWN: {
                snekY -= grid;
            }
        }
    }

    private static void updateDirection() {
        if (!inputQueue.isEmpty()) {
            for (Direction inp: inputQueue) {
                int i = inputQueue.indexOf(inp, false);
                if (inp.equals(UP) && direction != DOWN) {
                    direction = inputQueue.removeIndex(i);
                    //moveUpdate = false;
                } else if (inp.equals(DOWN) && direction != UP) {
                    direction = inputQueue.removeIndex(i);
                    //moveUpdate = false;
                } else if (inp.equals(LEFT) && direction != RIGHT) {
                    direction = inputQueue.removeIndex(i);
                    //moveUpdate = false;
                } else if (inp.equals(RIGHT) && direction != LEFT) {
                    direction = inputQueue.removeIndex(i);
                    //moveUpdate = false;
                } else {
                    inputQueue.removeIndex(i);
                }
            }
        }

        /*
        if (prevDirection != direction) {
            moveUpdate = true;
        }
        */
    }
}
