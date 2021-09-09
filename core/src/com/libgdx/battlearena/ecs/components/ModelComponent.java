package com.libgdx.battlearena.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;

public class ModelComponent implements Component, Disposable {
    public Model model;
    public String node;
    public ModelInstance modelInstance;
    public ModelComponent (ModelInstance i) {
        this.modelInstance = i;
    }
    public void setModel(Model m){
        model = m;
    }
    public void setNode(String n){
        node = n;
    }



    @Override
    public void dispose() {

    }
}
