package com.msvdev.game.busterbanny;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BusterBunnyWalk {

    // Текстура движений персонажа
    private final Texture img;

    // Анимация перемещения шагом
    private final Animation<TextureRegion> walk;

    // Направление перемещения шагом
    private boolean walkForward;

    // Время анимации
    private float time;


    /**
     * Основной конструктор объекта
     * @param imageFileName файл с изображением действий
     * @param col количество колонок (фрагментов по горизонтали)
     * @param row количество строк (фрагментов по вертикали)
     */
    public BusterBunnyWalk(String imageFileName, int col, int row) {

        img = new Texture(imageFileName);
        TextureRegion region = new TextureRegion(img);

        int xCnt = region.getRegionWidth() / col;
        int yCnt = region.getRegionHeight() / row;

        TextureRegion[][] regions = region.split(xCnt, yCnt);
        TextureRegion[] regionsArray = new TextureRegion[regions.length * regions[0].length];
        int cnt = 0;
        for (int i = 0; i < regions.length; i++) {
            for (int j = 0; j < regions[0].length; j++) {
                regionsArray[cnt++] = regions[i][j];
            }
        }

        float frameDuration = 1/4f;

        walk = new Animation<>(frameDuration, regionsArray);
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
        img.dispose();
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
