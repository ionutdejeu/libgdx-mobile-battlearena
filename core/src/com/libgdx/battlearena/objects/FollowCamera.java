package com.libgdx.battlearena.objects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.libgdx.battlearena.ecs.components.CharacterController;

public class FollowCamera {
    private Vector3 _targetPosition;
    private Vector3 _characterOffset;
    private CharacterController _characterController;
    private float _compensationOffset = 0.1f;
    private Matrix4 transform;
    private Camera cam;
    private BaseEntity target;
    private Vector3 blankVect = new Vector3();
    public FollowCamera(PerspectiveCamera cam, BaseEntity target){
        this.cam = cam;
        this.target = target;
    }

    public void update(float deltaTime){
        _targetPosition = target.transform.getTranslation(blankVect);
        cam.view.rotateTowardTarget(_targetPosition,Vector3.Y);
    }
}
