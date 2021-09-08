package com.libgdx.battlearena.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.libgdx.battlearena.ecs.components.BAComponentMapper;
import com.libgdx.battlearena.ecs.components.ModelComponent;
import com.libgdx.battlearena.ecs.components.TextureComponent;
import com.libgdx.battlearena.ecs.components.TransformComponent;
import com.sun.org.apache.xpath.internal.operations.Mod;

import java.util.Comparator;


public class RenderingSystem extends IteratingSystem {

    ModelBatch modelBatch;
    Environment environment;
    private Array<Entity> renderQueue;
    private PerspectiveCamera cam;
    private ComponentMapper<ModelComponent> modelM;
    private ComponentMapper<TransformComponent> transformM;

    private Color tintPlaceholder = Color.WHITE.cpy();


    public RenderingSystem(ModelBatch batch, PerspectiveCamera cam) {
        super(Family.all(TransformComponent.class, ModelComponent.class).get());//, new ZComparator())
        modelM = ComponentMapper.getFor(ModelComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);
        renderQueue = new Array<>();

        modelBatch = batch;
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        this.cam = cam;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        modelBatch.begin(cam);
        Entity currentEnt;
        for(int i=0;i<renderQueue.size;i++){
            currentEnt = renderQueue.get(i);
            TransformComponent  t= transformM.get(currentEnt);
            ModelComponent m = modelM.get(currentEnt);
            m.modelInstance.transform.set(t.transform);
            modelBatch.render(m.modelInstance, environment);
        }

        modelBatch.end();
        renderQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        renderQueue.add(entity);
    }


    public PerspectiveCamera getCamera() {
        return cam;
    }

}