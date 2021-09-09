package com.libgdx.battlearena.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.utils.Array;
import com.libgdx.battlearena.ecs.components.ModelComponent;
import com.libgdx.battlearena.ecs.components.RigidbodyComponent;
import com.libgdx.battlearena.ecs.components.TransformComponent;
import com.libgdx.battlearena.screens.BulletDynamicsScreen;

public class PhysicsSystem extends IteratingSystem {


    private static final float MAX_STEP_TIME = 1/60f;
    private static float accumulator = 0f;

    private static btDynamicsWorld dynamicsWorld;

    private Array<Entity> bodiesQueue;
    private ComponentMapper<RigidbodyComponent> rbc = ComponentMapper.getFor(RigidbodyComponent.class);
    private ComponentMapper<TransformComponent> tm = ComponentMapper.getFor(TransformComponent.class);

    public static btDynamicsWorld worldInstance(){
        return dynamicsWorld;
    }


    public PhysicsSystem(btDynamicsWorld w) {
        super(Family.all(TransformComponent.class, RigidbodyComponent.class).get());
        dynamicsWorld = w;
        bodiesQueue = new Array<>();
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        final float d = Math.min(1f / 30f, deltaTime);
        dynamicsWorld.stepSimulation(d, 5, 1f / 60f);

        for (Entity e : bodiesQueue) {
            RigidbodyComponent rb = rbc.get(e);
            TransformComponent t = tm.get(e);
            rb.btBody.getWorldTransform(t.transform);
        }
        bodiesQueue.clear();
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bodiesQueue.add(entity);
    }
}
