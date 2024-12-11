package io.github.snek.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.snek.Functions.*;
import io.github.snek.snek;

public class MainMenuScreen implements Screen {
    final snek game;

    public MainMenuScreen(final snek getGame) {
        this.game = getGame;
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
        snek.batch.draw(new Texture("homescreen.png"), 0, 0, snek.viewport.getWorldWidth(), snek.viewport.getWorldHeight());
        snek.batch.end();
        if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            InputBufferer inputBufferer = new InputBufferer();
            // Remove previously initialized processor.
            Gdx.input.setInputProcessor(null);
            Gdx.input.setInputProcessor(inputBufferer);
            game.setScreen(new GameScreen(game, inputBufferer));
            dispose();
        }
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
