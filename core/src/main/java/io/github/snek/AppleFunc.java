package io.github.snek;

import com.badlogic.gdx.math.MathUtils;

import static io.github.snek.GameScreen.*;

public class AppleFunc {
    final snek game;

    public AppleFunc(snek game) {
        this.game = game;
    }

    // Check for empty space and place apple.
    public static void checkAndPlaceApple() {
        if (!appleAvailable) {
            boolean appleBodyCollide;
            do {
                appleBodyCollide = false;
                appleX = MathUtils.random((int) snek.viewport.getWorldWidth() / grid - 1) * grid;
                appleY = MathUtils.random((int) snek.viewport.getWorldHeight() / grid - 1) * grid;
                for (BodyPart bodyPart : bodyParts) {
                    if (bodyPart.x == appleX && bodyPart.y == appleY) {
                        appleBodyCollide = true;
                        break;
                    }
                }
                if (appleX == snekX && appleY == snekY) {
                    appleBodyCollide = true;
                }
            } while (appleBodyCollide);
            appleAvailable = true;
        }
        apple.setPosition(appleX, appleY);
        apple.draw(snek.batch);
    }

    // Check for apple collision.
    public static void checkAppleCollision() {
        if (appleAvailable && appleX == snekX && appleY == snekY) {
            BodyPart bodyPart = new BodyPart(snekBody);
            bodyPart.updateBodyPosition(snekX, snekY);
            bodyParts.insert(0, bodyPart);
            appleAvailable = false;
            if (MOVE_TIME > .08f && applesEaten % 2 == 0) {
                MOVE_TIME -= .01f;
            }
            applesEaten++;
        }
    }
}
