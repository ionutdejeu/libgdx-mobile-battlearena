package com.libgdx.battlearena.ecs.components;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

public class CameraFollowComponent {
    Entity target = null;
    public Vector2 offset = new Vector2(0,0);
    float baseRotation = 0f;

}
