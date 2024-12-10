package io.github.snek;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class snek extends Game {
    public static SpriteBatch batch;
    public BitmapFont font, fontSmall, fontTiny;
    public static FitViewport viewport;

    public void create() {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("I_drawed_this.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 90;
        batch = new SpriteBatch();
        font = fontGenerator.generateFont(fontParameter);
        fontParameter.size = 45;
        fontSmall = fontGenerator.generateFont(fontParameter);
        viewport = new FitViewport(600, 600);
        fontParameter.size = 25;
        fontTiny = fontGenerator.generateFont(fontParameter);

        font.setUseIntegerPositions(false);
        font.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());

        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
        fontSmall.dispose();
        fontTiny.dispose();
    }

}
