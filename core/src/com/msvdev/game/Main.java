package com.msvdev.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.msvdev.game.screens.StartMenuScreen;


public class Main extends Game {

    @Override
    public void create() {
        setScreen(new StartMenuScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
    }
}
