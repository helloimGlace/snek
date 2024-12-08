package io.github.snek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.audio.Music;
//import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen{
    final snek game;

    private static final char RIGHT = 'r';
    private static final char LEFT = 'l';
    private static final char UP = 'u';
    private static final char DOWN = 'd';

    static char direction = RIGHT;
    static char tempDirection = RIGHT;

    // snek's grid size and movement timing.
    private static float MOVE_TIME = 0.2F; // delay between movements
    private static final int grid = 30;
    private float timer = MOVE_TIME;
    private int snekX = 0, snekY = 0;
    private int snekXBeforeUpdate = 0, snekYBeforeUpdate = 0;
    private boolean move = false;

    // Sprites and textures for snek and apple.
    private final Texture snekBody;
    private final Sprite snekHead;
    private final Sprite apple;
    // private final Animation<TextureRegion> snekDeathAnim;
    // float stateTime = 0f;

    private boolean appleAvailable = false;
    private int appleX, appleY;
    public static int applesEaten = 0;

    // The snek's body.
    Array<BodyPart> bodyParts = new Array<>();

    public GameScreen(final snek getGame){
        this.game = getGame;

        snekHead = new Sprite(new Texture("snek_head.png"));
        snekHead.setSize(grid, grid);
        snekBody = new Texture("snek_body.png");
        apple = new Sprite(new Texture("apple.png"));
        apple.setSize(grid, grid);

        // Supposed to play death animation for 2 seconds, but currently does not work, so it is commented out.
        /*
        TextureRegion[] snekDeathFrames = new TextureRegion[2];
        snekDeathFrames[0] = new TextureRegion(new Texture("snek_death_1.png"));
        snekDeathFrames[1] = new TextureRegion(new Texture("snek_death_2.png"));
        snekDeathAnim = new Animation<TextureRegion>(0.333f, snekDeathFrames);
        */

        snekX = (int)game.viewport.getWorldWidth()/2;
        snekY = (int)game.viewport.getWorldHeight()/2;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        input();
        logic();
        draw();
    }

    // Input for the snek.
    private void input() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && direction != DOWN) {
            if (!move) {
                tempDirection = UP;
            } else {
                direction = UP;
                move = false;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && direction != RIGHT) {
            if (!move) {
                tempDirection = LEFT;
            } else {
                direction = LEFT;
                move = false;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && direction != UP) {
            if (!move) {
                tempDirection = DOWN;
            } else {
                direction = DOWN;
                move = false;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && direction != LEFT) {
            if (!move) {
                tempDirection = RIGHT;
            } else {
                direction = RIGHT;
                move = false;
            }
        }
    }

    private void logic() {
        float dlt = Gdx.graphics.getDeltaTime();
        timer -= dlt;
        if (timer <= 0) {
            timer = MOVE_TIME;
            moveSnek();
            move = true;
            if (tempDirection != direction) {
                direction = tempDirection;
            }
            checkForDeath();
            checkForOutOfBounds();
            updateBodyPartsPosition();
        }
        checkAppleCollision();
    }

    // The snek's movement logic function.
    private void moveSnek() {
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

    private void draw(){
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();
        snekHead.setPosition(snekX, snekY);
        snekHead.draw(game.batch);
        for (BodyPart bodyPart: bodyParts) {
            bodyPart.draw(game.batch);
        }
        checkAndPlaceApple();
        game.batch.end();
    }

    // Check for out of bounds.
    private void checkForOutOfBounds() {
        if (snekX >= game.viewport.getWorldWidth()) {
            snekX = 0;
        }
        if (snekX < 0) {
            snekX = (int)game.viewport.getWorldWidth() - grid;
        }
        if (snekY >= game.viewport.getWorldHeight()) {
            snekY = 0;
        }
        if (snekY < 0) {
            snekY = (int)game.viewport.getWorldWidth() - grid;
        }
    }

    // Update the position of the body parts.
    private void updateBodyPartsPosition() {
        if (bodyParts.size > 0) {
            BodyPart bodyPart = bodyParts.removeIndex(0);
            bodyPart.updateBodyPosition(snekXBeforeUpdate, snekYBeforeUpdate);
            bodyParts.add(bodyPart);
        }
    }

    // Check for snek death.
    private void checkForDeath() {
        for (BodyPart bodyPart: bodyParts) {
            if (bodyPart.x == snekX && bodyPart.y == snekY) {
                // Supposed to play death animation for 2 seconds, but currently does not work, so it is commented out.
                /*
                long startTime = TimeUtils.millis();
                long elapsedTime = 0;
                snekDead();
                while (elapsedTime < 2000) {
                    elapsedTime = TimeUtils.timeSinceMillis(startTime);
                }
                */

                // Move to ending screen.
                game.setScreen(new EndingScreen(game));
                dispose();
            }
        }
    }

    // Check for empty space and place apple.
    private void checkAndPlaceApple() {
        if (!appleAvailable) {
            boolean appleBodyCollide;
            do {
                appleBodyCollide = false;
                appleX = MathUtils.random((int) game.viewport.getWorldWidth() / grid - 1) * grid;
                appleY = MathUtils.random((int) game.viewport.getWorldHeight() / grid - 1) * grid;
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
        apple.draw(game.batch);
    }

    // Check for apple collision.
    private void checkAppleCollision() {
        if (appleAvailable && appleX == snekX && appleY == snekY) {
            BodyPart bodyPart = new BodyPart(snekBody);
            bodyPart.updateBodyPosition(snekX, snekY);
            bodyParts.insert(0, bodyPart);
            appleAvailable = false;
            if (MOVE_TIME > .1f && applesEaten % 2 == 0) {
                MOVE_TIME -= .01f;
            }
            applesEaten++;
        }
    }

    // Body part class.
    private class BodyPart {
        private int x, y;
        private Texture texture;

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
    }

    // Snek dead function (currently does not work).
    /*
    private void snekDead() {
        stateTime += Gdx.graphics.getDeltaTime();
        game.batch.begin();
        // game.batch.enableBlending();
        // snekHead.getTexture().dispose();
        TextureRegion currentFrame = snekDeathAnim.getKeyFrame(stateTime, true);
        game.batch.draw(currentFrame, 100, 100, grid, grid);
        game.batch.end();
    }
    */

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        snekHead.getTexture().dispose();
        snekBody.dispose();
        apple.getTexture().dispose();
        for (BodyPart bodyPart: bodyParts) {
            bodyPart.texture.dispose();
        }

    }

    private static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
