package com.libgdx.battlearena.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btAxisSweep3;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseProxy;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btConvexShape;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btGhostPairCallback;
import com.badlogic.gdx.physics.bullet.collision.btPairCachingGhostObject;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btKinematicCharacterController;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.libgdx.battlearena.GUI.Controller;
import com.libgdx.battlearena.objects.BaseEntity;
import com.libgdx.battlearena.objects.BaseScreen;
import com.libgdx.battlearena.objects.FollowCamera;
import com.libgdx.battlearena.objects.GizmoEntity;
import com.libgdx.battlearena.objects.PhysicsWorld;
import com.libgdx.battlearena.objects.PhysicsWorldConstructor;
import com.libgdx.battlearena.objects.RigidBodyEntity;
import com.libgdx.tests.BulletConstructor;
import com.libgdx.tests.BulletEntity;
import com.libgdx.tests.BulletWorld;

public class RigidBodyTestScreen extends BaseScreen {
    final int BOXCOUNT_X = 5;
    final int BOXCOUNT_Y = 5;
    final int BOXCOUNT_Z = 1;

    final float BOXOFFSET_X = -2.5f;
    final float BOXOFFSET_Y = 0.5f;
    final float BOXOFFSET_Z = 0f;

    RigidBodyEntity ground;
    RigidBodyEntity character;
    BaseEntity transformGizmo;

    btGhostPairCallback ghostPairCallback;
    btPairCachingGhostObject ghostObject;
    btConvexShape ghostShape;
    btKinematicCharacterController characterController;
    Matrix4 characterTransform;
    Vector3 characterDirection = new Vector3();
    Vector3 characterPosCpy = new Vector3();
    Vector3 walkDirection = new Vector3();
    public SpriteBatch batch;
    Controller joystikcController;
    FollowCamera followCamera;
    ShapeRenderer srend;

    @Override
    public PhysicsWorld createWorld() {
        // We create the world using an axis sweep broadphase for this test
        btDefaultCollisionConfiguration collisionConfiguration = new btDefaultCollisionConfiguration();
        btCollisionDispatcher dispatcher = new btCollisionDispatcher(collisionConfiguration);
        btAxisSweep3 sweep = new btAxisSweep3(new Vector3(-1000, -1000, -1000), new Vector3(1000, 1000, 1000));
        btSequentialImpulseConstraintSolver solver = new btSequentialImpulseConstraintSolver();
        btDiscreteDynamicsWorld collisionWorld = new btDiscreteDynamicsWorld(dispatcher, sweep, solver, collisionConfiguration);
        ghostPairCallback = new btGhostPairCallback();
        sweep.getOverlappingPairCache().setInternalGhostPairCallback(ghostPairCallback);
        batch = new SpriteBatch();
        joystikcController = new Controller(batch);
        return new PhysicsWorld(collisionConfiguration, dispatcher, sweep, solver, collisionWorld);
    }

    @Override
    public void create() {
        super.create();
        instructions = "Tap to shoot\nArrow keys to move\nR to reset\nLong press to toggle debug mode\nSwipe for next test";

        // Create a visual representation of the character (note that we don't use the physics part of BulletEntity, we'll do that manually)
        //final Texture texture = new Texture(Gdx.files.internal("data/badlogic.jpg"));
        //disposables.add(texture);
        final Material material = new Material(ColorAttribute.createSpecular(1, 1, 1, 1), FloatAttribute.createShininess(8f));
        final long attributes = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates;
        final Model capsule = modelBuilder.createCapsule(2f, 6f, 16, material, attributes);
        disposables.add(capsule);
        world.addConstructor("capsule", new PhysicsWorldConstructor(capsule, null));
        character = world.add("transformGizmo", 5f, 3f, 5f);
        characterTransform = character.transform; // Set by reference
        characterTransform.rotate(Vector3.X, 90);

        // Create the physics representation of the character
        ghostObject = new btPairCachingGhostObject();
        ghostObject.setWorldTransform(characterTransform);
        ghostShape = new btCapsuleShape(2f, 2f);
        ghostObject.setCollisionShape(ghostShape);
        ghostObject.setCollisionFlags(btCollisionObject.CollisionFlags.CF_CHARACTER_OBJECT);
        characterController = new btKinematicCharacterController(ghostObject, ghostShape, .35f, Vector3.Y);

        // And add it to the physics world
        world.collisionWorld.addCollisionObject(ghostObject,
                (short) btBroadphaseProxy.CollisionFilterGroups.CharacterFilter,
                (short) (btBroadphaseProxy.CollisionFilterGroups.StaticFilter | btBroadphaseProxy.CollisionFilterGroups.DefaultFilter));
        ((btDiscreteDynamicsWorld) (world.collisionWorld)).addAction(characterController);


        // Add the ground
        (ground = world.add("ground", 0f, 0f, 0f))
                .setColor(0.25f + 0.5f * (float) Math.random(), 0.25f + 0.5f * (float) Math.random(), 0.25f + 0.5f * (float) Math.random(), 1f);
        // Create some boxes to play with
        for (int x = 0; x < BOXCOUNT_X; x++) {
            for (int y = 0; y < BOXCOUNT_Y; y++) {
                for (int z = 0; z < BOXCOUNT_Z; z++) {
                    world.add("box", BOXOFFSET_X + x, BOXOFFSET_Y + y, BOXOFFSET_Z + z)
                            .setColor(0.5f + 0.5f * (float) Math.random(), 0.5f + 0.5f * (float) Math.random(), 0.5f + 0.5f * (float) Math.random(), 1f);
                }
            }
        }

        this.followCamera = new FollowCamera(this.camera, this.character);
        srend = new ShapeRenderer();
        srend.setColor(Color.RED);

    }

    @Override
    public void show() {

    }  // Returns the angle in degrees between /from/ and /to/. This is always the smallest

    public static float kEpsilonNormalSqrt = 1e-15F;

    public static float Angle(Vector3 from, Vector3 to) {
        // sqrt(a) * sqrt(b) = sqrt(a * b) -- valid for real numbers
        float denominator = (float) Math.sqrt(from.len2() * to.len2());
        if (denominator < kEpsilonNormalSqrt)
            return 0F;

        float dot = MathUtils.clamp(from.cpy().dot(to) / denominator, -1F, 1F);
        return ((float) MathUtils.acos(dot)) * MathUtils.radiansToDegrees;
    }

    private Quaternion LookRotation(Vector3 lookAt) {
        Vector3 up = Vector3.Y;
        Vector3 forward = lookAt.cpy();
        Quaternion q = new Quaternion();

        return q;
    }

    private Quaternion UnityLookRotation(Vector3 forward, Vector3 up) {

        forward.nor();

        Vector3 vector = forward.cpy();
        Vector3 vector2 = up.cpy().crs(vector).nor();
        Vector3 vector3 = vector.cpy().crs(vector2);
        float m00 = vector2.x;
        float m01 = vector2.y;
        float m02 = vector2.z;
        float m10 = vector3.x;
        float m11 = vector3.y;
        float m12 = vector3.z;
        float m20 = vector.x;
        float m21 = vector.y;
        float m22 = vector.z;


        float num8 = (m00 + m11) + m22;
        Quaternion quaternion = new Quaternion();
        if (num8 > 0f) {
            float num = (float) Math.sqrt(num8 + 1f);
            quaternion.w = num * 0.5f;
            num = 0.5f / num;
            quaternion.x = (m12 - m21) * num;
            quaternion.y = (m20 - m02) * num;
            quaternion.z = (m01 - m10) * num;
            return quaternion;
        }
        if ((m00 >= m11) && (m00 >= m22)) {
            float num7 = (float) Math.sqrt(((1f + m00) - m11) - m22);
            float num4 = 0.5f / num7;
            quaternion.x = 0.5f * num7;
            quaternion.y = (m01 + m10) * num4;
            quaternion.z = (m02 + m20) * num4;
            quaternion.w = (m12 - m21) * num4;
            return quaternion;
        }
        if (m11 > m22) {
            float num6 = (float) Math.sqrt(((1f + m11) - m00) - m22);
            float num3 = 0.5f / num6;
            quaternion.x = (m10 + m01) * num3;
            quaternion.y = 0.5f * num6;
            quaternion.z = (m21 + m12) * num3;
            quaternion.w = (m20 - m02) * num3;
            return quaternion;
        }
        float num5 = (float) Math.sqrt(((1f + m22) - m00) - m11);
        float num2 = 0.5f / num5;
        quaternion.x = (m20 + m02) * num2;
        quaternion.y = (m21 + m12) * num2;
        quaternion.z = 0.5f * num5;
        quaternion.w = (m01 - m10) * num2;
        quaternion.nor();
        return quaternion;
    }
    public static Quaternion RotateTowards(Quaternion from, Quaternion to, float maxDegreesDelta)
    {
         return null;
    }

    @Override
    public void render(float delta) {

        walkDirection.set(0, 0, 0);
        if (joystikcController.touchStarted()) {
            Vector2 speed = joystikcController.speed();
            Vector3 toV = new Vector3(speed.x,0f,speed.y);
            Vector3 fromV = Vector3.X.rot(characterTransform).nor();
            Vector3 fromV2 = new Vector3(fromV.x, 0, fromV.z).nor();
            Vector3 charPosition = characterTransform.getTranslation(new Vector3()).nor();

            Vector3 toVTran = toV.add(charPosition).nor();
            float dotV = fromV2.cpy().dot(toV);

            Quaternion q = new Quaternion();
            if (dotV >= 1.0f) {
                q.idt();
            } else if (dotV <= -1.0f) {
                Vector3 axis = fromV2.cpy().crs(Vector3.Z);
                if (axis.len2() <= 1e-6) {
                    axis = fromV2.cpy().crs(Vector3.Y);
                }
                q.setFromAxis(axis.nor(), MathUtils.PI);
            } else {
                double scalar = Math.sqrt(fromV2.len2() * toV.len2()) + dotV;
                Vector3 axis = fromV.cpy().crs(toV);
                q.set(axis, (float) scalar).nor();
            }
            float angle = MathUtils.atan2(speed.x, speed.y) * MathUtils.PI/180;
            Quaternion q3 = new Quaternion();
            q3.setEulerAngles(0,angle,0).nor();
            //q.setFromCross(characterMovementXZ,joystickForwardVector);
            System.out.println("Q:" + q.getPitch() + ":" + q.getYaw() + ":" + q.getRoll());
            Quaternion q2 = UnityLookRotation(fromV2,toV);
            System.out.println("Q2:" + q2.getPitch() + ":" + q2.getYaw() + ":" + q2.getRoll());
            System.out.println("Q3:" + q3.getPitch() + ":" + q3.getYaw() + ":" + q3.getRoll());

            characterTransform.rotateTowardTarget(toV,Vector3.Y);
            Quaternion q4 = characterTransform.getRotation(new Quaternion(),true);
            float rotationAroundY =  q4.getAngleAround(Vector3.Y);
            Quaternion dummyQ = new Quaternion();

            dummyQ.setFromCross(toV,Vector3.Y);
            float targetRotationAroundY = dummyQ.getAngleAround(Vector3.Y);

            System.out.println("Q4:" + q4.getPitch() + ":" + q4.getYaw() + ":" + q4.getRoll());
            System.out.println("JR:"+targetRotationAroundY+", PR:"+rotationAroundY);
            ghostObject.setWorldTransform(characterTransform);

        }

        //characterController.setWalkDirection(walkDirection);
        // Now we can update the world as normally
        super.update();
        // And fetch the new transformation of the character (this will make the model be rendered correctly)
        ghostObject.getWorldTransform(characterTransform);
        super.render(delta);
        //batch.begin();
        joystikcController.draw();
        //batch.end();
        followCamera.update(delta);
    }

    float calculateDirection(float h, float v) {
        float dr = 0;
        if (h != 0) dr = 90 * h;
        if (v < 0) dr = 180;
        else if (v > 0) dr = 0;
        return dr;
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

    }
}
