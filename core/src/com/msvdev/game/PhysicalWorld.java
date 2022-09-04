package com.msvdev.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;


/**
 * Класс, описывающий физический мир уровня
 */
public class PhysicalWorld {

    /**
     * Ускорение свободного падения
     */
    public static final float ACCELERATION_OF_GRAVITY = 9.81f;

    /**
     * Pixel per meter - константа, определяющая масштаб при переходе
     * от размеров растрового изображения к размерам реального мира.
     * PPM = количеству точек на один метр
     */
    public static final float PPM = 100f;


    // Экземпляр класса, описывающего физический мир
    private final World world;

    // Отображение тел (для отладки)
    private final Box2DDebugRenderer debugRenderer;


    // Константы, определяющий дискретизацию физического мира
    private static final float TIME_STEP = 1f/60;
    private static final int VELOCITY_ITERATIONS = 3;
    private static final int POSITION_ITERATIONS = 3;

    // Переменная для отслеживания изменения времени в физическом мире
    private float deltaTimeAccumulator = 0;

    // Описание контактов между телами
    private final MyContactListener contactListener;


    /**
     * Основной конструктор класса
     */
    public PhysicalWorld() {
        world = new World(new Vector2(0, -ACCELERATION_OF_GRAVITY), true);

        contactListener = new MyContactListener();
        world.setContactListener(contactListener);

        debugRenderer = new Box2DDebugRenderer();
    }



    /**
     * Добавить в физический мир твёрдые поверхности
     * @param mapObjects коллекция твёрдых поверхностей
     */
    public void addSolidSurfaces(MapObjects mapObjects) {

        for (MapObject mapObject: mapObjects) {

            // Свойства тела
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;


            // Свойства части тела
            FixtureDef fixtureDef = new FixtureDef();

            // Форма тела
            PolygonShape polygonShape = new PolygonShape();


            // Прямоугольный объект
            if (mapObject instanceof RectangleMapObject) {
                RectangleMapObject rectangleMapObject = (RectangleMapObject) mapObject;

                Rectangle rectangle = rectangleMapObject.getRectangle();

                bodyDef.position.set((rectangle.x + 0.5f * rectangle.width) / PPM, (rectangle.y + 0.5f * rectangle.height) / PPM);
                polygonShape.setAsBox(0.5f * rectangle.width / PPM, 0.5f * rectangle.height / PPM);


            // Полигональный объект
            } else if (mapObject instanceof PolygonMapObject) {
                PolygonMapObject polygonMapObject = (PolygonMapObject) mapObject;

                Polygon polygon = polygonMapObject.getPolygon();

                float[] vertices = polygon.getVertices();
                for (int i = 0; i < vertices.length; i++) {
                    vertices[i] /= PPM;
                }

                bodyDef.position.set(polygon.getX() / PPM, polygon.getY() / PPM);
                polygonShape.set(vertices);

            }


            // Форма
            fixtureDef.shape = polygonShape;

            // Трение
            Object friction = mapObject.getProperties().get("friction");
            fixtureDef.friction = friction != null ? (float) friction : 0;

            // Плотность
            fixtureDef.density = 1f;

            // Восстановление при ударе (эластичность)
            fixtureDef.restitution = 0f;


            // Создание твёрдого тела в физическом мире
            Body body = world.createBody(bodyDef);
            body.createFixture(fixtureDef).setUserData("Твёрдая поверхность");

            // Освобождение ресурсов
            polygonShape.dispose();
        }
    }




    /**
     * Добавить объект физического мира
     * @param object добавляемый объект
     * @return экземпляр класса твёрдого тела
     */
    public Body addObject(RectangleMapObject object) {

        // Прямоугольник, описывающий объект
        Rectangle rect = object.getRectangle();

        // Тип объекта (тела): динамический, кинетический или статический (по умолчанию)
        String bodyType = (String) object.getProperties().get("BodyType");


        // Свойства тела
        BodyDef def = new BodyDef();

        // Тип тела
        if (bodyType != null) {

            if (bodyType.equalsIgnoreCase("StaticBody")) {
                def.type = BodyDef.BodyType.StaticBody;

            } else if (bodyType.equalsIgnoreCase("DynamicBody")) {
                def.type = BodyDef.BodyType.DynamicBody;

            } else if (bodyType.equalsIgnoreCase("KinematicBody")) {
                def.type = BodyDef.BodyType.KinematicBody;

            } else {
                def.type = BodyDef.BodyType.StaticBody;
            }

        } else {
            def.type = BodyDef.BodyType.StaticBody;
        }

        // Начальная позиция тела
        def.position.set((rect.x + 0.5f * rect.width) / PPM, (rect.y + 0.5f * rect.height) / PPM);
        def.gravityScale = 1f; //(float) object.getProperties().get("gravityScale");


        // Свойства части тела
        FixtureDef fdef = new FixtureDef();

        // Геометрия составной части тела
        PolygonShape polygonShape = new PolygonShape();
        // Тело квадратное
        polygonShape.setAsBox(0.5f * rect.width / PPM, 0.5f * rect.height / PPM);

        // Другие доступные формы
        CircleShape circleShape;
        ChainShape chainShape;
        EdgeShape edgeShape;

        // Форма
        fdef.shape = polygonShape;
        // Трение
        fdef.friction = 0.2f;
        // Плотность
        fdef.density = 1f;
        // Восстановление при ударе (эластичность)
        fdef.restitution = 0f; // (float) object.getProperties().get("restitution");


        // Создание твёрдого тела в физическом мире
        Body body = world.createBody(def);
        body.createFixture(fdef);
        body.setFixedRotation(true);

        // Сенсор для прыжка (ноги)
        polygonShape.setAsBox(
                0.5f * rect.width / PPM,
                0.5f * rect.height / PPM,
                new Vector2(0, -0.95f * rect.height / PPM),
                0
        );
        Fixture fixture = body.createFixture(fdef);
        fixture.setSensor(true);
        fixture.setUserData("Сенсор ног персонажа");


        // Освобождение ресурсов
        polygonShape.dispose();


        return body;
    }


    /**
     * Вычисление нового события происходящего в физическом мире
     * @param deltaTime время в секундах, прошедшее от предыдущего события
     */
    public void doPhysicsStep(float deltaTime) {

        deltaTimeAccumulator += deltaTime;

        while (deltaTimeAccumulator >= TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            deltaTimeAccumulator -= TIME_STEP;
        }
    }


    /**
     * Отрисовка контуров объектов физического мира (для отладки)
     * @param camera камера
     */
    public void debugDraw(OrthographicCamera camera) {
        debugRenderer.render(world, camera.combined);
    }


    /**
     * Освобождение ресурсов
     */
    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
    }

    public MyContactListener getContactListener() {
        return contactListener;
    }
}
