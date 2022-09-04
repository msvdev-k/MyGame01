package com.msvdev.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.ScreenUtils;
import com.msvdev.game.PhysicalWorld;
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
//    private final Rectangle mapSize;
    private final ShapeRenderer shapeRenderer;


    // Физический мир
    private PhysicalWorld physicalWorld;
    private final int[] bg;
    private final int[] l1;
    private final Body heroBody;
    private final Rectangle heroRect;


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
        camera.zoom = 0.5f;

        // Загрузка карты
        TiledMap map = new TmxMapLoader().load("map/Level_01.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        // Фоновый слой
        bg = new int[1];
        bg[0] = map.getLayers().getIndex("Задний фон");

        // Игровые слои
        l1 = new int[2];
//        l1[0] = map.getLayers().getIndex("Слой 2");
//        l1[1] = map.getLayers().getIndex("Слой 3");

        physicalWorld = new PhysicalWorld();


        RectangleMapObject tmp = (RectangleMapObject) map.getLayers().get("Персонажи").getObjects().get("Герой"); //выбор объекта по имени
//        camera.position.x = tmp.getRectangle().x;
//        camera.position.y = tmp.getRectangle().y;
//        RectangleMapObject tmp = (RectangleMapObject) map.getLayers().get("сеттинг").getObjects().get("hero"); //выбор объекта по имени
        heroRect = tmp.getRectangle();
        heroBody = physicalWorld.addObject(tmp);


        physicalWorld.addSolidSurfaces(map.getLayers().get("Твёрдые горизонтальные поверхности").getObjects());
        physicalWorld.addSolidSurfaces(map.getLayers().get("Твёрдые вертикальные поверхности").getObjects());
//        Array<RectangleMapObject> objects = map.getLayers().get("Твёрдые горизонтальные поверхности").getObjects().getByType(RectangleMapObject.class);
//
//        for (int i = 0; i < objects.size; i++) {
//            physicalWorld.addObject(objects.get(i));
//        }
//
//        tmp = (RectangleMapObject) (map.getLayers().get("Объекты").getObjects().get("Граница карты"));
//        mapSize = tmp.getRectangle();


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
    public void render(float deltaTime) {

        // Текущие координаты мыши
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();


        // Отработка нажатия на кнопку Выход
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) &&
                rectangleExitButton.contains(mouseX, mouseY)) {

            dispose();
            game.setScreen(new StartMenuScreen(this.game));
        }



        // Управление камерой
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            camera.zoom += 0.01f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.O) && camera.zoom > 0) {
            camera.zoom -= 0.01f;
        }



        // Движение персонажа вправо или влево
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            heroBody.applyForceToCenter(new Vector2(-0.2f, 0), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            heroBody.applyForceToCenter(new Vector2(0.2f, 0), true);
        }

        // Поворот персонажа вправо или влево
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            busterBunnyWalk.setWalkForward(true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            busterBunnyWalk.setWalkForward(false);
        }



        // Позиция камеры
        camera.position.x = heroBody.getPosition().x * PhysicalWorld.PPM;
        camera.position.y = heroBody.getPosition().y * PhysicalWorld.PPM;
        camera.update();

        // Очистка экрана
        ScreenUtils.clear(1f, 1f, 1f, 1f);



        // Отображение заднего фона карты
        mapRenderer.setView(camera);
        mapRenderer.render(bg);


        // Текущий кадр персонажа
        busterBunnyWalk.incrementTime(deltaTime);
        TextureRegion heroFrame = busterBunnyWalk.getFrame();


        // Отображение героя
//        batch.setProjectionMatrix(camera.combined);
//        heroRect.x = heroBody.getPosition().x * PhysicalWorld.PPM - heroRect.width/2;
//        heroRect.y = heroBody.getPosition().y * PhysicalWorld.PPM - heroRect.height/2;
        float heroX = 0.5f * Gdx.graphics.getWidth() - 0.5f * (heroRect.width + 3) / camera.zoom;
        float heroY = 0.5f * Gdx.graphics.getHeight() - 0.5f * (heroRect.height + 3) / camera.zoom;
        batch.begin();
        batch.draw(heroFrame, heroX, heroY, heroFrame.getRegionWidth() / camera.zoom, heroFrame.getRegionHeight() / camera.zoom);
        batch.end();



        // Отображение переднего фона карты
        //mapRenderer.render(l1);



        // Отображение кнопок меню
        batch.begin();
        //batch.draw(heroFrame, 0.5f * Gdx.graphics.getWidth(), 0.5f * Gdx.graphics.getHeight());
        batch.draw(exitButton,
                rectangleExitButton.getX(),
                rectangleExitButton.getY(),
                rectangleExitButton.getWidth(),
                rectangleExitButton.getHeight()
        );
        batch.end();



        // Перерасчёт положения тел
        physicalWorld.doPhysicsStep(deltaTime);
//        physicalWorld.debugDraw(camera);

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
        physicalWorld.dispose();
    }
}
