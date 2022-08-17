package com.msvdev.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.msvdev.game.busterbanny.BusterBunnyWalk;


public class MyGdxGame extends ApplicationAdapter {

	private SpriteBatch batch;

	private BusterBunnyWalk busterBunnyWalk;

	// Вспомогательные переменные для перемещения персонажа (координата, скорость и направление)
	private float xRun;
	private float vxRun;
	private boolean forwardMovement;


	@Override
	public void create () {
		batch = new SpriteBatch();

		busterBunnyWalk = new BusterBunnyWalk("BusterBunnyWalk.png", 6, 1);
		busterBunnyWalk.setPlayMode(Animation.PlayMode.LOOP);

		float duration = 1/4f;
		xRun = 0f;
		vxRun = 12/duration;
		forwardMovement = true;

	}

	@Override
	public void render () {
		ScreenUtils.clear(0.5f, 1f, 0.5f, 1f);


		// Поворот персонажа вправо или влево
		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			forwardMovement = true;
			busterBunnyWalk.setWalkForward(true);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			forwardMovement = false;
			busterBunnyWalk.setWalkForward(false);
		}


		// Дельта времени
		float deltaTime = Gdx.graphics.getDeltaTime();


		// Текущий кадр
		busterBunnyWalk.incrementTime(deltaTime);
		TextureRegion frame = busterBunnyWalk.getFrame();


		// Перемещение координаты
		if (forwardMovement) {
			xRun += vxRun * Gdx.graphics.getDeltaTime();
			if (xRun >= Gdx.graphics.getWidth() - frame.getRegionWidth()) {
				forwardMovement = false;
				busterBunnyWalk.setWalkForward(false);
			}
		} else {
			xRun -= vxRun * Gdx.graphics.getDeltaTime();
			if (xRun <= 0) {
				forwardMovement = true;
				busterBunnyWalk.setWalkForward(true);
			}
		}


		// Отрисовка фрейма
		batch.begin();
		batch.draw(frame, xRun, 0);
		batch.end();
	}


	@Override
	public void dispose () {
		batch.dispose();
		busterBunnyWalk.dispose();
	}
}
