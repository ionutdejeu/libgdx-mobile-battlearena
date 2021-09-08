package com.libgdx.battlearena.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.libgdx.battlearena.screens.BulletDynamicsScreen;
import com.libgdx.battlearena.screens.BulletPhysicsScreen;
import com.libgdx.battlearena.screens.ECSTestScreen;
import com.libgdx.battlearena.screens.GameplayScreen3d;
import com.libgdx.battlearena.screens.TitleScreen;

public class BattleArenaGame extends Game {
    static public Skin gameSkin;

    public void create () {
        //GdxNativesLoader.load();
        Bullet.init();
        gameSkin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        this.setScreen(new ECSTestScreen(this));
    }

    public void render () {
        super.render();
    }


    public void dispose () {
    }
}
