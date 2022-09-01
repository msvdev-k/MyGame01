package com.msvdev.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.msvdev.game.busterbanny.BusterBunnyWalk;


public class GameScreen implements Screen {

    private final Game game;

    private SpriteBatch batch;
    private final BusterBunnyWalk busterBunnyWalk;

    private final Texture exitButton;
    private final Rectangle rectangleExitButton;

    // Вспомогательные переменные для перемещения персонажа (координата, скорость и направление)
    private float xRun;
    private float vxRun;
    private boolean forwardMovement;


    private final OrthographicCamera camera;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Rectangle mapSize;
    private final ShapeRenderer shapeRenderer;


    public GameScreen(Game game) {
        this.game = game;

        batch = new SpriteBatch();

        busterBunnyWalk = new BusterBunnyWalk("atlas/BusterBunny.atlas");
        busterBunnyWalk.setPlayMode(Animation.PlayMode.LOOP);

        exitButton = new Texture("exit_button.png");
        rectangleExitButton = new Rectangle();

        float duration = 1 / 4f;
        xRun = 0f;
        vxRun = 12 / duration;
        forwardMovement = true;


        shapeRenderer = new ShapeRenderer();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 1f;

        // Загрузка карты
        TiledMap map = new TmxMapLoader().load("map/Level_01.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        RectangleMapObject tmp = (RectangleMapObject) map.getLayers().get("Объекты").getObjects().get("Камера"); //выбор объекта по имени
        camera.position.x = tmp.getRectangle().x;
        camera.position.y = tmp.getRectangle().y;

        tmp = (RectangleMapObject) (map.getLayers().get("Объекты").getObjects().get("Граница карты"));
        mapSize = tmp.getRectangle();


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

        // Установка камеры
        float STEP = 3;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && mapSize.x < (camera.position.x - STEP))
            camera.position.x -= STEP;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && (mapSize.x + mapSize.width) > (camera.position.x + STEP))
            camera.position.x += STEP;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) camera.position.y += STEP;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) camera.position.y -= STEP;

        if (Gdx.input.isKeyPressed(Input.Keys.P)) camera.zoom += 0.01f;
        if (Gdx.input.isKeyPressed(Input.Keys.O) && camera.zoom > 0) camera.zoom -= 0.01f;

        camera.update();

        // Очистка экрана
        ScreenUtils.clear(0.5f, 1f, 0.5f, 1f);


        // Текущие координаты мыши
        float x = Gdx.input.getX();
        float y = Gdx.graphics.getHeight() - Gdx.input.getY();


        // Отображение карты
        mapRenderer.setView(camera);
        mapRenderer.render();


        // Прямоугольник, охватывающий карту
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GOLD);
        shapeRenderer.rect(mapSize.x, mapSize.y, mapSize.width, mapSize.height);
        shapeRenderer.end();


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
//        if (forwardMovement) {
//            xRun += vxRun * delta;
//            if (xRun >= Gdx.graphics.getWidth() - frame.getRegionWidth()) {
//                forwardMovement = false;
//                busterBunnyWalk.setWalkForward(false);
//            }
//        } else {
//            xRun -= vxRun * delta;
//            if (xRun <= 0) {
//                forwardMovement = true;
//                busterBunnyWalk.setWalkForward(true);
//            }
//        }


        // Отрисовка фрейма
        batch.begin();
        batch.draw(frame, 0.5f * Gdx.graphics.getWidth(), 0.5f * Gdx.graphics.getHeight());
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
        camera.viewportWidth = width;
        camera.viewportHeight = height;

        // Коррекция координаты кнопки выхода
        rectangleExitButton.setX(width - rectangleExitButton.getWidth());
        rectangleExitButton.setY(height - rectangleExitButton.getHeight());

        // Изменяем размер SpriteBatch
        batch.dispose();
        batch = new SpriteBatch();
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
        mapRenderer.dispose();
        shapeRenderer.dispose();
    }
}
