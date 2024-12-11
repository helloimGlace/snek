package io.github.snek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
// Music and Sound will be added in the future lol
// import com.badlogic.gdx.audio.Music;
// import com.badlogic.gdx.audio.Sound;

public class GameScreen implements Screen{
    final snek game;

    // Declaration of initial direction and buffer
    public static Direction direction = Direction.RIGHT;
    public static Array<Direction> inputBuffer;

    // snek's grid size and movement timing.
    public static float MOVE_TIME_INIT = .16F; // Initial delay between movements.
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

    // Particles system for snek's apple.
    TextureAtlas atlas;
    ParticleEffectPool appleEffectPool;
    static Array<ParticleEffectPool.PooledEffect> effects = new Array<>();
    ParticleEffect appleEffects = new ParticleEffect();


    public static boolean appleAvailable = false;
    public static int appleX, appleY;
    public static int applesEaten;
    private boolean appleEaten = false;
    private boolean isDead = false;

    // The snek's body.
    static Array<BodyPart> bodyParts;
    public static float timeElapsed;



    public GameScreen(final snek getGame, final InputBufferer inputBufferer) {
        this.game = getGame;

        snekHead = new Sprite(new Texture("snek_head.png"));
        snekHead.setSize(grid, grid);
        snekBody = new Texture("snek_body.png");
        apple = new Sprite(new Texture("apple.png"));
        apple.setSize(grid, grid);
        bodyParts = new Array<>();
        bodyParts.add(new BodyPart(snekBody)); // snek starts with 1 body part.

        // Get death sprites from sheet
        snekDeathSheet = new Texture("snek_death_spritesheet.png");
        TextureRegion[][] tmp = TextureRegion.split(
            snekDeathSheet,
            snekDeathSheet.getWidth() / FRAME_COLS,
            snekDeathSheet.getHeight() / FRAME_ROWS
        );

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

        atlas = new TextureAtlas();
        atlas.addRegion("apple_eat", new TextureRegion(new Texture("particles/apple_eat.png")));
        appleEffects.load(Gdx.files.internal("particles/apple_eat.p"), atlas);
        appleEffectPool = new ParticleEffectPool(appleEffects, 1, 2);

        snekX = (int)snek.viewport.getWorldWidth()/2;
        snekY = (int)snek.viewport.getWorldHeight()/2;
        bodyParts.get(0).updateBodyPosition(snekX-grid, snekY);

        // Reset to the initial values each restart.
        MOVE_TIME = MOVE_TIME_INIT;
        applesEaten = 0;
        stateTime = 0f;
        timeElapsed = 0f;

        direction = Direction.RIGHT;
        inputBuffer = inputBufferer.inputBuffer;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        logic(delta);
        draw(delta);
        if (isDead && timeElapsed > 2f) {
            game.setScreen(new EndingScreen(game));
            dispose();
        }
    }

    private void logic(float delta) {
        if (!isDead) {
            timer -= delta;
            if (timer <= 0) {
                timer = MOVE_TIME;
                SnekFunc.moveSnek();

                Direction newDirection = direction;
                if (inputBuffer.notEmpty()) {
                    newDirection = inputBuffer.removeIndex(0);
                }

                direction = newDirection;

                isDead = SnekFunc.checkForDeath();
                SnekFunc.checkForOutOfBounds();
                BodyPart.updateBodyPartsPosition();
            }
            appleEaten = AppleFunc.checkAppleCollision();
        }
    }

    private void draw(float delta){
        // Clears screen with color black.
        ScreenUtils.clear(Color.BLACK);
        snek.viewport.apply();
        snek.batch.setProjectionMatrix(snek.viewport.getCamera().combined);

        snek.batch.begin();
        for (int i = 0; i < effects.size; i++) {
            ParticleEffectPool.PooledEffect effect = effects.get(i);

            effect.draw(snek.batch, delta);
            if (effect.isComplete()) {
                effect.free();
                effects.removeIndex(i);
            }
        }
        if (!isDead) {
            if (appleEaten) {
                ParticleEffectPool.PooledEffect effect = appleEffectPool.obtain();
                effect.setPosition(appleX+(grid/2f), appleY+(grid/2f));
                effects.add(effect);
                appleEaten = false;
            }

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
        appleEffects.dispose();
        for (int i = effects.size - 1; i >= 0; i--)
            effects.get(i).free();
        effects.clear();
    }
}
