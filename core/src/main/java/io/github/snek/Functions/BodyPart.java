package io.github.snek.Functions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import static io.github.snek.Screens.GameScreen.*;

// BodyPart class for the snek's body parts + functions related to the snek's body.
public class BodyPart {
    public int x, y;
    private final Texture texture;

    public BodyPart(Texture texture) {
        this.texture = texture;
    }

    public void updateBodyPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Batch batch) {
        if (!(x == snekX && y == snekY)) batch.draw(texture, x, y, grid, grid);
    }

    // Update the position of the body parts.
    public static void updateBodyPartsPosition() {
        if (bodyParts.size > 0) {
            BodyPart bodyPart = bodyParts.removeIndex(0);
            bodyPart.updateBodyPosition(snekXBeforeUpdate, snekYBeforeUpdate);
            bodyParts.add(bodyPart);
        }
    }
}
