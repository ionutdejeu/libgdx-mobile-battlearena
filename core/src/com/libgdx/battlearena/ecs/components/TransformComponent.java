package com.libgdx.battlearena.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

public class TransformComponent implements Component, Pool.Poolable {
    public final Vector3 position = new Vector3();
    public final Vector3 scale = new Vector3(1,1,1);
    public Color tint = Color.WHITE.cpy();
    public Quaternion rotation = new Quaternion();
    public Matrix4 transform = new Matrix4();
    public boolean isHidden = false;
    public Vector2 originOffset = new Vector2(0f, 0f);

    public TransformComponent(){

    }
    public static TransformComponent create(Engine engine){
        if(engine instanceof PooledEngine){
            return ((PooledEngine)engine).createComponent(TransformComponent.class);
        }else {
            return new TransformComponent();
        }
    }
    public TransformComponent setTransform(Matrix4 t){
        transform = t;
        return this;
    }
    public TransformComponent setPosition(float x, float y){
        return setPosition(x, y, this.position.z);

    }
    public TransformComponent setPosition(float x, float y, float z){
        this.position.set(x, y, z);
        this.transform.setFromEulerAngles(x,y,z);
        return this;
    }

    public TransformComponent setOpacity(float opacity){
        //this.opacity = opacity;
        this.tint.set(this.tint.r, this.tint.g, this.tint.b, opacity);
        return this;
    }

    public TransformComponent setScale(float x, float y,float z){
        this.scale.set(x, y,z);
        transform.scale(x,y,z);
        return this;
    }

    public TransformComponent setRotation(Quaternion rot){
        this.rotation = rot;
        transform.rotate(rot);
        return this;
    }

    public TransformComponent setTint(Color color){
        this.tint.set(color.r, color.g, color.b, color.a);
        return this;
    }

    public TransformComponent setTint(float r, float g, float b, float a){
        if(this.tint != null){
            this.tint.set(r, g, b, a);
        }
        return this;
    }

    public TransformComponent setOriginOffset(float x, float y){
        this.originOffset.set(x, y);
        return this;
    }

    public TransformComponent hide(){
        this.isHidden = false;
        return this;
    }

    public TransformComponent show(){
        this.isHidden = true;
        return this;
    }

    public TransformComponent setHidden(boolean isHidin){
        this.isHidden = isHidin;
        return this;
    }

    @Override
    public void reset() {
        this.position.set(0f, 0f, 0f);
        this.scale.set(1f, 1f,1f);
        this.rotation.set(0,0,0,0f);
        this.isHidden = false;
        this.tint.set(255f, 255f, 255f, 1f);
        this.originOffset.set(0f, 0f);
    }
}