package com.libgdx.battlearena.scenes;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.libgdx.battlearena.ecs.systems.RenderingSystem;

public class DevTestScene {

    ModelBatch mb;
    PerspectiveCamera cam;
    private Game g;
    public DevTestScene(Game g) {

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(3f, 7f, 10f);
        cam.lookAt(0, 4f, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        RenderingSystem renderingSystem = new RenderingSystem(mb,cam);

    }



}
