package com.libgdx.battlearena.objects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;

public class GizmoEntity extends BaseEntity{

    public GizmoEntity(Model m, Matrix4 transform){
        this.modelInstance = new ModelInstance(m,transform,"line",false);
    }
    @Override
    public void dispose() {

    }
}
