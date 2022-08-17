package com.msvdev.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture hexagon;
	int clk;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		hexagon = new Texture("hexagon-blue.png");
		clk = 0;
	}

	@Override
	public void render () {
		ScreenUtils.clear(1f, 1f, 1f, 1f);

		float x = Gdx.input.getX() - 0.5f * hexagon.getWidth();
		float y = Gdx.graphics.getHeight() - Gdx.input.getY() - 0.5f * hexagon.getHeight();

		if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) clk++;

		Gdx.graphics.setTitle("Clicked " + clk + " times!");

		batch.begin();
		batch.draw(hexagon, x, y);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		hexagon.dispose();
	}
}
