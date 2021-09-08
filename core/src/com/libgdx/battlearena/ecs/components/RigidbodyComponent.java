package com.libgdx.battlearena.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

public class RigidbodyComponent implements Component, Pool.Poolable, Disposable {

    public btRigidBody btBody;
    private static Vector3 localInertia = new Vector3();

    public RigidbodyComponent() {
    }

    public void setRigidBody(btCollisionShape shape, float mass, int identifier, Matrix4 worldTransform) {
        if (mass > 0f)
            shape.calculateLocalInertia(mass, localInertia);
        else
            localInertia.set(0, 0, 0);

        btRigidBody.btRigidBodyConstructionInfo constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, shape, localInertia);
        setRigidBody(constructionInfo, identifier, worldTransform);
    }


    public void setRigidBody(btRigidBody.btRigidBodyConstructionInfo constructionInfo, int identifier, Matrix4 worldTrans) {
        btBody = new btRigidBody(constructionInfo);
        btBody.setWorldTransform(worldTrans);
        btBody.setUserValue(identifier);

        btBody.setCollisionFlags(btBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
    }

    public static RigidbodyComponent create(Engine engine) {
        if (engine instanceof PooledEngine) {
            return ((PooledEngine) engine).createComponent(RigidbodyComponent.class);
        } else {
            return new RigidbodyComponent();
        }
    }


    @Override
    public void reset() {

    }

    @Override
    public void dispose() {
        btBody.dispose();
    }
}
