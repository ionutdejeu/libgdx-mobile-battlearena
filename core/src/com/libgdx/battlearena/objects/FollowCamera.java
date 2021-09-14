package com.libgdx.battlearena.objects;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.libgdx.battlearena.ecs.components.CharacterController;

public class FollowCamera {
    private Vector3 _targetPosition;
    private Vector3 _characterOffset;
    private CharacterController _characterController;
    private float _compensationOffset = 0.1f;
    private Matrix4 transform;

    public FollowCamera(){

    }
}
