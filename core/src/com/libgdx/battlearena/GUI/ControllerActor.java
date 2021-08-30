package com.libgdx.battlearena.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class ControllerActor extends Actor {

    private ShapeRenderer shapeRenderer;
    static private boolean projectionMatrixSet;

    public ControllerActor(final String actorName) {

        shapeRenderer = new ShapeRenderer();
        projectionMatrixSet = false;
        setTouchable(Touchable.enabled);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("Touch down asset with name ", actorName);
                return true;
            }
        });
    }



    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if(!projectionMatrixSet){
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(0, 0, 50, 50);
        shapeRenderer.end();

    }
}