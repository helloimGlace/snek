package io.github.snek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
// Music and Sound will be added in the future lol
// import com.badlogic.gdx.audio.Music;
// import com.badlogic.gdx.audio.Sound;

public class GameScreen implements Screen{
    final snek game;

    // Direction "enums"
    private static final char RIGHT = 'r';
    private static final char LEFT = 'l';
    private static final char UP = 'u';
    private static final char DOWN = 'd';

    // Declaration of initial direction
    static char direction;
    static char tempDirection;

    // snek's grid size and movement timing.
    private boolean move = false;
    public static float MOVE_TIME_INIT = 0.2F; // Initial delay between movements.
    public static float MOVE_TIME = MOVE_TIME_INIT;
    private float timer = MOVE_TIME_INIT;
    public static final int grid = 30;
    public static int snekX, snekY;
    public static int snekXBeforeUpdate = 0, snekYBeforeUpdate = 0;


    // Sprite sheet columns and rows
    private static final int FRAME_COLS = 2, FRAME_ROWS = 1;

    // Sprites and textures for snek and apple.
    private final Texture snekDeathSheet;
    public static Texture snekBody;
    public static Sprite snekHead;
    public static Sprite apple;
    public static Animation<TextureRegion> snekDeathAnim;
    public static float stateTime;

    public static boolean appleAvailable = false;
    public static int appleX, appleY;
    public static int applesEaten;
    private boolean isDead = false;

    // The snek's body.
    static Array<BodyPart> bodyParts;
    public static float timeElapsed;


    public GameScreen(final snek getGame){
        this.game = getGame;

        snekHead = new Sprite(new Texture("snek_head.png"));
        snekHead.setSize(grid, grid);
        snekBody = new Texture("snek_body.png");
        apple = new Sprite(new Texture("apple.png"));
        apple.setSize(grid, grid);
        bodyParts = new Array<>();

        // Get death sprites from sheet
        snekDeathSheet = new Texture("snek_death_spritesheet.png");
        TextureRegion[][] tmp = TextureRegion.split(snekDeathSheet, snekDeathSheet.getWidth() / FRAME_COLS, snekDeathSheet.getHeight() / FRAME_ROWS);

        // Load each frames into frames array
        TextureRegion[] snekDeathFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; ++i) {
            for (int j = 0; j < FRAME_COLS; ++j) {
                snekDeathFrames[index++] = tmp[i][j];
            }
        }
        // snekDeathAnim is ready
        snekDeathAnim = new Animation<>(0.25f, snekDeathFrames);

        snekX = (int)snek.viewport.getWorldWidth()/2;
        snekY = (int)snek.viewport.getWorldHeight()/2;

        // Reset to the initial values each restart.
        // (my (glace's) dumb ass forgot to do this, causing bugs like the score and
        // speed not resetting or the death animation not playing after the first replay)
        MOVE_TIME = MOVE_TIME_INIT;
        applesEaten = 0;
        stateTime = 0f;
        timeElapsed = 0f;
        direction = RIGHT;
        tempDirection = RIGHT;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        input();
        logic(delta);
        draw(delta);
        if (isDead && timeElapsed > 2f) {
            game.setScreen(new EndingScreen(game));
            dispose();
        }
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

    private void logic(float delta) {
        if (!isDead) {
            timer -= delta;
            if (timer <= 0) {
                timer = MOVE_TIME;
                SnekFunc.moveSnek();
                move = true;
                if (tempDirection != direction) {
                    direction = tempDirection;
                }
                isDead = SnekFunc.checkForDeath();
                SnekFunc.checkForOutOfBounds();
                BodyPart.updateBodyPartsPosition();
            }
            AppleFunc.checkAppleCollision();
        }
    }

    private void draw(float delta){
        // Clears screen with color black.
        ScreenUtils.clear(Color.BLACK);
        snek.viewport.apply();
        snek.batch.setProjectionMatrix(snek.viewport.getCamera().combined);

        snek.batch.begin();
        if (!isDead) {
            snekHead.setPosition(snekX, snekY);
            snekHead.draw(snek.batch);
        } else {
            SnekFunc.snekDead(delta);
        }
        for (BodyPart bodyPart: bodyParts) {
            bodyPart.draw(snek.batch);
        }
        AppleFunc.checkAndPlaceApple();
        snek.batch.end();
    }

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
        snekDeathSheet.dispose();
    }
}
