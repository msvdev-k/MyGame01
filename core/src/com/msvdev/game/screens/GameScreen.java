package com.msvdev.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.msvdev.game.busterbanny.BusterBunnyWalk;


public class GameScreen implements Screen {

    private final Game game;

    private final SpriteBatch batch;
    private final BusterBunnyWalk busterBunnyWalk;

    private final Texture exitButton;
    private final Rectangle rectangleExitButton;

    // Вспомогательные переменные для перемещения персонажа (координата, скорость и направление)
    private float xRun;
    private float vxRun;
    private boolean forwardMovement;


    public GameScreen(Game game) {
        this.game = game;

        batch = new SpriteBatch();

        busterBunnyWalk = new BusterBunnyWalk("atlas/BusterBunny.atlas");
        busterBunnyWalk.setPlayMode(Animation.PlayMode.LOOP);

        exitButton = new Texture("exit_button.png");
        rectangleExitButton = new Rectangle();

        float duration = 1/4f;
        xRun = 0f;
        vxRun = 12/duration;
        forwardMovement = true;
    }


    @Override
    public void show() {

        // Расположение кнопки выход
        rectangleExitButton.setWidth(exitButton.getWidth());
        rectangleExitButton.setHeight(exitButton.getHeight());
        rectangleExitButton.setX(Gdx.graphics.getWidth() - rectangleExitButton.getWidth());
        rectangleExitButton.setY(Gdx.graphics.getHeight() - rectangleExitButton.getHeight());

    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.5f, 1f, 0.5f, 1f);

        // Текущие координаты мыши
        float x = Gdx.input.getX();
        float y = Gdx.graphics.getHeight() - Gdx.input.getY();


        // Отработка нажатия на кнопку Выход
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) &&
            rectangleExitButton.contains(x, y)) {

            dispose();
            game.setScreen(new StartMenuScreen(this.game));
        }



        // Поворот персонажа вправо или влево
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            forwardMovement = true;
            busterBunnyWalk.setWalkForward(true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            forwardMovement = false;
            busterBunnyWalk.setWalkForward(false);
        }


        // Текущий кадр
        busterBunnyWalk.incrementTime(delta);
        TextureRegion frame = busterBunnyWalk.getFrame();


        // Перемещение координаты
        if (forwardMovement) {
            xRun += vxRun * delta;
            if (xRun >= Gdx.graphics.getWidth() - frame.getRegionWidth()) {
                forwardMovement = false;
                busterBunnyWalk.setWalkForward(false);
            }
        } else {
            xRun -= vxRun * delta;
            if (xRun <= 0) {
                forwardMovement = true;
                busterBunnyWalk.setWalkForward(true);
            }
        }


        // Отрисовка фрейма
        batch.begin();
        batch.draw(frame, xRun, 0);
        batch.draw(exitButton,
                rectangleExitButton.getX(),
                rectangleExitButton.getY(),
                rectangleExitButton.getWidth(),
                rectangleExitButton.getHeight()
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
        busterBunnyWalk.dispose();
        exitButton.dispose();
    }
}
