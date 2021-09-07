package com.libgdx.battlearena.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.libgdx.battlearena.screens.ECSTestScreen;
import com.libgdx.battlearena.screens.TitleScreen;

public class BattleArenaGame extends Game {
    static public Skin gameSkin;

    public void create () {
        gameSkin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        this.setScreen(new ECSTestScreen(this));
    }

    public void render () {
        super.render();
    }


    public void dispose () {
    }
}
