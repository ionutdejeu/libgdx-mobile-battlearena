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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.libgdx.battlearena.ecs.components.ModelComponent;
import com.libgdx.battlearena.ecs.components.OrbitCameraControlComponent;
import com.libgdx.battlearena.ecs.components.PhysicsObjectFactory;
import com.libgdx.battlearena.ecs.components.RigidbodyComponent;
import com.libgdx.battlearena.ecs.components.ScriptComponent;
import com.libgdx.battlearena.ecs.components.TransformComponent;
import com.libgdx.battlearena.ecs.systems.PhysicsSystem;
import com.libgdx.battlearena.ecs.systems.RenderingSystem;
import com.libgdx.battlearena.ecs.systems.ScriptSystem;

import org.graalvm.compiler.core.common.type.ArithmeticOpTable;

public class ECSTestScreen extends ScreenAdapter {

    private static final float MAX_DELTA = 0.16f;
    private Stage stage;
    PooledEngine e;
    Game game;
    ModelBatch mb;
    PerspectiveCamera cam;

    btConstraintSolver constraintSolver;
    btCollisionConfiguration collisionConfig;
    btDispatcher dispatcher;
    btBroadphaseInterface broadphase;
    btDynamicsWorld dynamicsWorld;
    PhysicsObjectFactory physicsFactory;
    public ECSTestScreen(Game g){
        GdxNativesLoader.load();

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

        physicsFactory = new PhysicsObjectFactory();
        ModelBuilder mb = new ModelBuilder();
        Model m = mb.createBox(2f, 2f, 2f,

                new Material(ColorAttribute.createDiffuse(Color.BLUE)),

                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal

        );
        ModelInstance i = new ModelInstance(m, 0, 0, 0);

        btCollisionShape collisonShape = physicsFactory.createBox(i.transform);
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
        dynamicsWorld.setGravity(new Vector3(0, -10f, 0));
        PhysicsSystem ps = new PhysicsSystem(dynamicsWorld);
        e.addSystem(ps);
        ScriptSystem ss = new ScriptSystem();
        e.addSystem(ss);

        Entity en = e.createEntity();
        TransformComponent tc = new TransformComponent();
        ModelComponent mc = new ModelComponent();
        RigidbodyComponent rb = new RigidbodyComponent();
        OrbitCameraControlComponent orbCam = new OrbitCameraControlComponent();
        orbCam.setCamera(cam);

        mc.modelInstance = i;
        rb.setRigidBody(physicsFactory.createBox(mc.modelInstance.transform),1,1,mc.modelInstance.transform);
        en.add(tc);
        dynamicsWorld.addRigidBody(rb.btBody, PhysicsObjectFactory.GROUND_FLAG, PhysicsObjectFactory.ALL_FLAG);

        en.add(mc);
        en.add(rb);
        en.add(orbCam);

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
