package com.libgdx.battlearena.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

public class RigidbodyComponent implements Component, Pool.Poolable, Disposable {

    btRigidBody btBody;
    public RigidbodyComponent(btRigidBody.btRigidBodyConstructionInfo constructionInfo){
        btBody = new btRigidBody(constructionInfo);
    }
    private btRigidBody internalRigidBody;
    @Override
    public void reset() {

    }

    @Override
    public void dispose() {
        btBody.dispose();
    }
}
