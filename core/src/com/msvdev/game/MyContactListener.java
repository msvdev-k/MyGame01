package com.msvdev.game;

import com.badlogic.gdx.physics.box2d.*;


/**
 * Класс отслеживающий контакты между телами
 */
public class MyContactListener implements ContactListener {

    private boolean onSolidSurfaceFlag;

    @Override
    public void beginContact(Contact contact) {

        // Пересекаемые части двух тел
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();


        if (a.getUserData() != null && b.getUserData() != null) {

            String stringA = (String) a.getUserData();
            String stringB = (String) b.getUserData();

            if ((stringA.equals("Сенсор ног персонажа") && stringB.equals("Твёрдая поверхность")) ||
                (stringB.equals("Сенсор ног персонажа") && stringA.equals("Твёрдая поверхность"))) {
                onSolidSurfaceFlag = true;
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

        // Пересекаемые части двух тел
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();


        if (a.getUserData() != null && b.getUserData() != null) {

            String stringA = (String) a.getUserData();
            String stringB = (String) b.getUserData();

            if ((stringA.equals("Сенсор ног персонажа") && stringB.equals("Твёрдая поверхность")) ||
                (stringB.equals("Сенсор ног персонажа") && stringA.equals("Твёрдая поверхность"))) {
                onSolidSurfaceFlag = false;
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public boolean isOnSolidSurfaceFlag() {
        return onSolidSurfaceFlag;
    }
}
