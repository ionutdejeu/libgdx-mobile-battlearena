package com.libgdx.battlearena.objects;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.FloatCounter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.LinearMath;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.libgdx.tests.BulletConstructor;
import com.libgdx.tests.BulletEntity;
import com.libgdx.tests.BulletWorld;

public class BaseScreen implements Screen, InputProcessor, GestureDetector.GestureListener {
    public StringBuilder performance = new StringBuilder();
    public String instructions = "Tap to shoot\nLong press to toggle debug mode\nSwipe for next test\nCtrl+drag to rotate\nScroll to zoom";
    public PerformanceCounter performanceCounter = new PerformanceCounter(this.getClass().getSimpleName());
    public FloatCounter fpsCounter = new FloatCounter(5);
    public PerspectiveCamera camera;

    @Override
    public boolean keyDown (int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped (char character) {
        return false;
    }



    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved (int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled (float amountX, float amountY) {
        return false;
    }


    @Override
    public void show() {

    }



    @Override
    public void resize (int width, int height) {
    }


    @Override
    public void pause () {
    }

    @Override
    public void resume () {
    }

    @Override
    public void hide() {

    }


    @Override
    public boolean touchDown (float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap (float x, float y, int count, int button) {
        return false;
    }



    @Override
    public boolean fling (float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan (float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop (float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom (float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop () {
    }

    private static boolean initialized = false;

    public static boolean shadows = true;

    public static void init () {
        if (initialized) return;
        // Need to initialize bullet before using it.

        Bullet.init();
        Gdx.app.log("Bullet", "Version = " + LinearMath.btGetVersion());
        initialized = true;
    }

    public Environment environment;
    public DirectionalLight light;
    public ModelBatch shadowBatch;

    public PhysicsWorld world;
    public ObjLoader objLoader = new ObjLoader();
    public ModelBuilder modelBuilder = new ModelBuilder();
    public ModelBatch modelBatch;
    public Array<Disposable> disposables = new Array<Disposable>();
    private int debugMode = btIDebugDraw.DebugDrawModes.DBG_NoDebug;

    protected final static Vector3 tmpV1 = new Vector3(), tmpV2 = new Vector3();

    public PhysicsWorld createWorld () {
        return new PhysicsWorld();
    }


    public void create () {
        init();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.3f, 0.3f, 0.3f, 1.f));
        light = shadows ? new DirectionalShadowLight(1024, 1024, 20f, 20f, 1f, 300f) : new DirectionalLight();
        light.set(0.8f, 0.8f, 0.8f, -0.5f, -1f, 0.7f);
        environment.add(light);
        if (shadows)
            environment.shadowMap = (DirectionalShadowLight)light;
        shadowBatch = new ModelBatch(new DepthShaderProvider());

        modelBatch = new ModelBatch();

        world = createWorld();
        world.performanceCounter = performanceCounter;

        final float width = Gdx.graphics.getWidth();
        final float height = Gdx.graphics.getHeight();
        if (width > height)
            camera = new PerspectiveCamera(67f, 3f * width / height, 3f);
        else
            camera = new PerspectiveCamera(67f, 3f, 3f * height / width);
        camera.position.set(10f, 10f, 10f);
        camera.lookAt(0, 0, 0);
        camera.update();

        // Create some simple models
        final Model groundModel = modelBuilder.createRect(
                20f,
                0f,
                -20f,
                -20f,
                0f,
                -20f,
                -20f,
                0f,
                20f,
                20f,
                0f,
                20f,
                0,
                1,
                0,
                new Material(ColorAttribute.createDiffuse(Color.WHITE), ColorAttribute.createSpecular(Color.WHITE), FloatAttribute
                        .createShininess(16f)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        disposables.add(groundModel);
        final Model boxModel = modelBuilder.createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(Color.WHITE),
                ColorAttribute.createSpecular(Color.WHITE), FloatAttribute.createShininess(64f)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        disposables.add(boxModel);

        // Add the constructors
        world.addConstructor("ground", new PhysicsWorldConstructor(groundModel, 0f)); // mass = 0: static body
        world.addConstructor("box", new PhysicsWorldConstructor(boxModel, 1f)); // mass = 1kg: dynamic body
        world.addConstructor("staticbox", new PhysicsWorldConstructor(boxModel, 0f)); // mass = 0: static body
    }

    @Override
    public void dispose () {
        world.dispose();
        world = null;

        for (Disposable disposable : disposables)
            disposable.dispose();
        disposables.clear();

        modelBatch.dispose();
        modelBatch = null;

        shadowBatch.dispose();
        shadowBatch = null;

        if (shadows)
            ((DirectionalShadowLight)light).dispose();
        light = null;

    }

    @Override
    public void render (float delta) {
        render(true);
    }

    public void render (boolean update) {
        fpsCounter.put(Gdx.graphics.getFramesPerSecond());

        if (update) update();

        beginRender(true);

        renderWorld();

        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        if (debugMode != btIDebugDraw.DebugDrawModes.DBG_NoDebug) world.setDebugMode(debugMode);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

        performance.setLength(0);
        performance.append("FPS: ").append(fpsCounter.value).append(", Bullet: ")
                .append((int)(performanceCounter.load.value * 100f)).append("%");
    }

    protected void beginRender (boolean lighting) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        camera.update();
    }

    protected void renderWorld () {
        if (shadows) {
            ((DirectionalShadowLight)light).begin(Vector3.Zero, camera.direction);
            shadowBatch.begin(((DirectionalShadowLight)light).getCamera());
            world.render(shadowBatch, null);
            shadowBatch.end();
            ((DirectionalShadowLight)light).end();
        }

        modelBatch.begin(camera);
        world.render(modelBatch, environment);
        modelBatch.end();
    }

    public void update () {
        world.update();
    }

    public RigidBodyEntity shoot (final float x, final float y) {
        return shoot(x, y, 30f);
    }

    public RigidBodyEntity shoot (final float x, final float y, final float impulse) {
        return shoot("box", x, y, impulse);
    }

    public RigidBodyEntity shoot (final String what, final float x, final float y, final float impulse) {
        // Shoot a box
        Ray ray = camera.getPickRay(x, y);
        RigidBodyEntity entity = world.add(what, ray.origin.x, ray.origin.y, ray.origin.z);
        entity.setColor(0.5f + 0.5f * (float)Math.random(), 0.5f + 0.5f * (float)Math.random(), 0.5f + 0.5f * (float)Math.random(),
                1f);
        ((btRigidBody)entity.body).applyCentralImpulse(ray.direction.scl(impulse));
        return entity;
    }

    public void setDebugMode (final int mode) {
        world.setDebugMode(debugMode = mode);
    }

    public void toggleDebugMode () {
        if (world.getDebugMode() == btIDebugDraw.DebugDrawModes.DBG_NoDebug)
            setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe | btIDebugDraw.DebugDrawModes.DBG_DrawFeaturesText | btIDebugDraw.DebugDrawModes.DBG_DrawText | btIDebugDraw.DebugDrawModes.DBG_DrawContactPoints);
        else if (world.renderMeshes)
            world.renderMeshes = false;
        else {
            world.renderMeshes = true;
            setDebugMode(btIDebugDraw.DebugDrawModes.DBG_NoDebug);
        }
    }

    @Override
    public boolean longPress (float x, float y) {
        toggleDebugMode();
        return true;
    }

    @Override
    public boolean keyUp (int keycode) {
        if (keycode == Input.Keys.ENTER) {
            toggleDebugMode();
            return true;
        }
        return true;
    }
}
