package com.libgdx.battlearena.ecs.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;

public class OrbitCameraControlComponent extends ScriptComponent{

    CameraInputController camController;
    PerspectiveCamera camera;

    public void setCamera(PerspectiveCamera cam){
        camera = cam;
        camController = new CameraInputController(camera);

        Gdx.input.setInputProcessor(camController);
    }

    @Override
    public void update(float deltaTime) {
        camController.update();
    }
}
