package com.msvdev.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;


public class StartMenuScreen implements Screen {

    private final Game game;

    private final SpriteBatch batch;
    private final Texture screen;
    private final Texture startButton;

    private final Rectangle rectangleScreen;
    private final Rectangle rectangleStartButton;

    private final Music music;
    private final Sound sound;

    public StartMenuScreen(Game game) {
        this.game = game;
        batch = new SpriteBatch();

        screen = new Texture("start_screen.jpg");
        startButton = new Texture("start_button.png");

        rectangleScreen = new Rectangle();
        rectangleStartButton = new Rectangle();

        music = Gdx.audio.newMusic(Gdx.files.internal("sound/map_menu.mp3"));
        music.setLooping(true);
        music.play();

        sound = Gdx.audio.newSound(Gdx.files.internal("sound/button_press_single.mp3"));
        sound.stop();

    }


    @Override
    public void show() {

        // Центрирование основного изображения на экране
        float scaleX = Gdx.graphics.getWidth() / (float) screen.getWidth();
        float scaleY = Gdx.graphics.getHeight() / (float) screen.getHeight();

        if (scaleX <= scaleY) {
            rectangleScreen.setWidth(screen.getWidth() * scaleX);
            rectangleScreen.setHeight(screen.getHeight() * scaleX);
        } else {
            rectangleScreen.setWidth(screen.getWidth() * scaleY);
            rectangleScreen.setHeight(screen.getHeight() * scaleY);
        }

        rectangleScreen.setX(0.5f * (Gdx.graphics.getWidth() - rectangleScreen.getWidth()));
        rectangleScreen.setY(0.9f * (Gdx.graphics.getHeight() - rectangleScreen.getHeight()));

        // Центрирование кнопки старт
        rectangleStartButton.setWidth(startButton.getWidth());
        rectangleStartButton.setHeight(startButton.getHeight());

        rectangleStartButton.setX(0.5f * (Gdx.graphics.getWidth() - rectangleStartButton.getWidth()));
        rectangleStartButton.setY(0.5f * (rectangleScreen.getY() - rectangleStartButton.getHeight()));

    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        // Текущие координаты мыши
        float x = Gdx.input.getX();
        float y = Gdx.graphics.getHeight() - Gdx.input.getY();


        // Отработка нажатия на кнопку Start
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
            (
                Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) &&
                rectangleStartButton.contains(x, y)
            )
        ) {
            dispose();
            game.setScreen(new GameScreen(this.game));
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && !rectangleStartButton.contains(x, y)) {
            sound.play();
        }


        // Прорисовка
        batch.begin();

        batch.draw(screen,
                rectangleScreen.getX(),
                rectangleScreen.getY(),
                rectangleScreen.getWidth(),
                rectangleScreen.getHeight()
        );

        batch.draw(startButton,
                rectangleStartButton.getX(),
                rectangleStartButton.getY(),
                rectangleStartButton.getWidth(),
                rectangleStartButton.getHeight()
        );

        batch.end();
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
        batch.dispose();
        screen.dispose();
        startButton.dispose();
        music.dispose();
        sound.dispose();
    }

}
