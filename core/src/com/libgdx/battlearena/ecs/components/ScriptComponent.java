package com.libgdx.battlearena.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public abstract class ScriptComponent implements Component, Pool.Poolable {
    @Override
    public void reset() {

    }

    public abstract void update(float deltaTime);
}