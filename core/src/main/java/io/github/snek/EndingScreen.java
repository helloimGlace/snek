package io.github.snek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class EndingScreen implements Screen {
    final snek game;

    private static float textXOffset;
    private static float textYOffset;


    private static float worldWidth;
    private static float worldHeight;

    public EndingScreen(final snek getGame) {
        this.game = getGame;

        worldWidth = snek.viewport.getWorldWidth();
        worldHeight = snek.viewport.getWorldHeight();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        snek.viewport.apply();
        snek.batch.setProjectionMatrix(snek.viewport.getCamera().combined);

        snek.batch.begin();
        getTextOffset(game.font, "ur ded (XP)");
        game.font.draw(snek.batch, "ur ded (XP)", textXOffset, 500);
        getTextOffset(game.font, "final score: " + GameScreen.applesEaten);
        game.font.draw(snek.batch, "final score: " + GameScreen.applesEaten, textXOffset, 380);
        getTextOffset(game.fontSmall, "(click or enter to restart)");
        game.fontSmall.draw(snek.batch, "(click or enter to restart)", textXOffset, 200);
        game.fontTiny.draw(snek.batch, "press esc to exit to main menu", 1, 50);
        snek.batch.end();
        if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            InputBufferer inputBufferer = new InputBufferer();
            // Remove previously initialized processor.
            Gdx.input.setInputProcessor(null);
            Gdx.input.setInputProcessor(inputBufferer);
            game.setScreen(new GameScreen(game, inputBufferer));
            dispose();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    // Get the offset for the text to be centered.
    private void getTextOffset(BitmapFont fontA, String text) {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(fontA, text);
        float textWidth = layout.width;
        float textHeight = layout.height;
        textXOffset = (worldWidth - textWidth) / 2;
        textYOffset = (worldHeight - textHeight) / 2;
    }

    @Override
    public void resize(int width, int height) {
        snek.viewport.update(width, height, true);
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

    }

}
