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


    private static float worldWidth = 0;
    private static float worldHeight = 0;

    public EndingScreen(final snek getGame) {
        this.game = getGame;

        worldWidth = game.viewport.getWorldWidth();
        worldHeight = game.viewport.getWorldHeight();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();
        getTextOffset(game.font, "ur ded (XP)");
        game.font.draw(game.batch, "ur ded (XP)", textXOffset, 500);
        getTextOffset(game.font, "final score: " + GameScreen.applesEaten);
        game.font.draw(game.batch, "final score: " + GameScreen.applesEaten, textXOffset, 380);
        getTextOffset(game.fontSmall, "(click or enter to restart)");
        game.fontSmall.draw(game.batch, "(click or enter to restart)", textXOffset, 200);
        game.batch.end();
        if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    private void getTextOffset(BitmapFont fontA, String text) {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(fontA, text);
        float textWidth = layout.width;
        float textHeight = layout.height;
        textXOffset = (worldWidth - textWidth) / 2;
        textYOffset = (worldHeight - textHeight) / 2;
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
