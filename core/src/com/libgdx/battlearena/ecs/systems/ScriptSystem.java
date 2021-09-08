package com.libgdx.battlearena.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.libgdx.battlearena.ecs.components.ModelComponent;
import com.libgdx.battlearena.ecs.components.OrbitCameraControlComponent;
import com.libgdx.battlearena.ecs.components.ScriptComponent;
import com.libgdx.battlearena.ecs.components.TransformComponent;


public class ScriptSystem extends EntitySystem {


    private ImmutableArray<Entity> scriptQueue;
    private ComponentMapper<ScriptComponent> scriptMapper;
    public ScriptSystem() {
        super(100);
        Family a = Family.all(ScriptComponent.class).get();

        scriptMapper = ComponentMapper.getFor(ScriptComponent.class);


    }

    @Override
    public void addedToEngine (Engine engine) {
        scriptQueue =  engine.getEntitiesFor(Family.all(ScriptComponent.class, OrbitCameraControlComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Entity currentEnt;
        System.out.println(deltaTime);
        for(int i=0;i<scriptQueue.size();i++){
            currentEnt = scriptQueue.get(i);
            ScriptComponent s= scriptMapper.get(currentEnt);
            s.update(deltaTime);
        }

    }



}
