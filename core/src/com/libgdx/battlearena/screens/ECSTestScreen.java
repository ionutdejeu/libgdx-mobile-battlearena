package com.libgdx.battlearena.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.libgdx.battlearena.ecs.components.ModelComponent;
import com.libgdx.battlearena.ecs.components.TransformComponent;
import com.libgdx.battlearena.ecs.systems.RenderingSystem;

public class ECSTestScreen extends ScreenAdapter {

    private static final float MAX_DELTA = 0.16f;
    private Stage stage;
    PooledEngine e;
    Game game;
    ModelBatch mb;
    PerspectiveCamera cam;
    Model model;
    public ECSTestScreen(Game g){
        game = g;
        e = new PooledEngine();
        stage  = new Stage(new ScreenViewport());

        cam = new PerspectiveCamera(

                75,
                Gdx.graphics.getWidth(),

                Gdx.graphics.getHeight());

        cam.position.set(0f, 0f, 3f);

        cam.lookAt(0f, 0f, 0f);


        // Near and Far (plane) repesent the minimum and maximum ranges of the camera in, um, units

        cam.near = 0.1f;

        cam.far = 300.0f;
        cam.update();

        mb = new ModelBatch();
        RenderingSystem renderingSystem = new RenderingSystem(mb,cam);
        e.addSystem(renderingSystem);

        Entity en = e.createEntity();
        TransformComponent tc = e.createComponent(TransformComponent.class);

        ModelBuilder mb = new ModelBuilder();
        Model m = mb.createBox(2f, 2f, 2f,

                new Material(ColorAttribute.createDiffuse(Color.BLUE)),

                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal

        );


        // A model holds all of the information about an, um, model, such as vertex data and texture info
        // However, you need an instance to actually render it.  The instance contains all the
        // positioning information ( and more ).  Remember Model==heavy ModelInstance==Light
        ModelInstance i = new ModelInstance(m, 0, 0, 0);

        ModelComponent mc = e.createComponent(ModelComponent.class);
        mc.modelInstance = i;
        en.add(tc);
        en.add(mc);
        e.addEntity(en);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float deltaChange) {
        float clippedDelta = Math.min(deltaChange, MAX_DELTA);
        e.update(clippedDelta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        e.clearPools();
    }
}
