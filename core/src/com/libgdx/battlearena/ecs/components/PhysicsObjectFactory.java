package com.libgdx.battlearena.ecs.components;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;

public class PhysicsObjectFactory {
    public static final int STEEL = 0;
    public static final int WOOD = 1;
    public static final int RUBBER = 2;
    public static final int STONE = 3;
    public final static short GROUND_FLAG = 1 << 8;
    public final static short OBJECT_FLAG = 1 << 9;
    public final static short ALL_FLAG = -1;
    private static PhysicsObjectFactory thisInstance;
    private btDynamicsWorld world;
    private final float DEGTORAD = 0.0174533f;

    public PhysicsObjectFactory(){
    }



    public btCollisionShape createBox(Matrix4 transform){
        btCollisionShape shape =new btBoxShape(new Vector3(2.5f, 0.5f, 2.5f));
        return shape;
    }
}
