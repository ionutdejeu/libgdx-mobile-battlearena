package com.libgdx.battlearena.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.collision.btConeShape;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.libgdx.battlearena.GUI.Controller;
import com.libgdx.battlearena.objects.BaseScreen;
import com.libgdx.battlearena.utils.UsefulMeshs;


public class ObjectRotationScreen implements Screen {

    private Game game;
    private Stage stage;
    public static final float PPM = 100;
    public static SpriteBatch batch;
    PerspectiveCamera cam;
    CameraInputController camController;
    ModelBatch modelBatch;
    Environment environment;
    Controller joystickController;
    Array<ModelInstance> models;
    ModelInstance gizmo;
    ModelInstance ball;

    public ObjectRotationScreen(Game g) {
        // Create camera sized to screens width/height with Field of View of 75 degrees
        game = g;
        stage = new Stage(new ScreenViewport());



        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(3f, 7f, 10f);
        cam.lookAt(0, 4f, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);
        InputMultiplexer m = new InputMultiplexer();

        batch = new SpriteBatch();
        joystickController = new Controller(batch,m);
        m.addProcessor(camController);
        models = new Array<>();
        Material planeMaterial = new Material(ColorAttribute.createDiffuse(Color.WHITE));
        Model axes = UsefulMeshs.createAxes();
        Model transfomrGizmo = UsefulMeshs.transformGizmo();
        models.add(new ModelInstance(axes));
        gizmo = new ModelInstance(transfomrGizmo);
        ball = new ModelInstance(UsefulMeshs.ball());
        models.add(gizmo);
        models.add(ball);
        gizmo.transform.translate(new Vector3(1,1,1));
        Gdx.input.setInputProcessor(m);


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // You’ve seen all this before, just be sure to clear the GL_DEPTH_BUFFER_BIT when working in 3D


        camController.update();

        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Matrix4 trn = gizmo.transform;
        trn.rotate(Vector3.Y,10*delta);
        modelBatch.begin(cam);
        modelBatch.render(models, environment);
        modelBatch.end();
        joystickController.draw();
        if(true){
            //System.out.println("Touch Started");
            Vector2 s = joystickController.speed().nor();
            Vector3 characterPos= trn.getTranslation(new Vector3());
            //Vector3 s3 = new Vector3(0,0,0);
            //Vector3 s3trn = s3.cpy().rot(trn);
            //System.out.println(s3trn);
            Vector3 playerForward = Vector3.X.rot(trn);
            System.out.println(playerForward);
            Quaternion q = new Quaternion();
            ball.transform.setToTranslation(playerForward.cpy().nor());
            //q.set(Vector3.Y,)

            //s3trn.y= 0;
            //trn.rotate(q);
            //trn.rotateTowardDirection(s3trn,Vector3.Y);
        }

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
        modelBatch.dispose();

    }

}