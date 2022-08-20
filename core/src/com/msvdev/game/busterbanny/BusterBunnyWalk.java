package com.msvdev.game.busterbanny;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class BusterBunnyWalk {

    // Атлас движений персонажа
    private final TextureAtlas atlas;


    // Анимация перемещения шагом
    private final Animation<TextureRegion> walk;

    // Направление перемещения шагом
    private boolean walkForward;

    // Время анимации
    private float time;


    /**
     * Основной конструктор объекта
     * @param atlasFileName пить к файлу с атласом движений персонажа
     */
    public BusterBunnyWalk(String atlasFileName) {

        atlas = new TextureAtlas(atlasFileName);

        float frameDuration = 1/4f;

        walk = new Animation<TextureRegion>(frameDuration, atlas.findRegions("walk"));

        walk.setPlayMode(Animation.PlayMode.LOOP);

        time = 0;
        walkForward = true;
    }


    /**
     * Получить текущий фрейм (с учётом поворота)
     * @return
     */
    public TextureRegion getFrame() {
        TextureRegion frame = walk.getKeyFrame(time);
        frame.flip(walkForward == frame.isFlipX(), false);
        return frame;
    }


    /**
     * Увеличить текущее время анимации
     * @param time
     */
    public void incrementTime(float time) {
        this.time += time;
    }

    /**
     * Сбросить время анимации в ноль
     */
    public void resetTime() {
        time = 0f;
    }


    public boolean isAnimationOver() {
        return walk.isAnimationFinished(time);
    }

    /**
     * Установка режима анимации
     * @param playMode
     */
    public void setPlayMode(Animation.PlayMode playMode) {
        walk.setPlayMode(playMode);
    }


    /**
     * Освобождение ресурсов
     */
    public void dispose() {
        atlas.dispose();
    }


    /**
     * Установить направление движения персонажа
     * (Зависит от прорисовки движения!)
     * @param walkForward true - слева направо, false - справа на лево
     */
    public void setWalkForward(boolean walkForward) {
        this.walkForward = walkForward;
    }
}
