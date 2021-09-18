package com.libgdx.battlearena.math;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

class QuaternionEx extends Quaternion {

    public static Quaternion LookRotation(Vector3 f){
        return null;
    }

    /**
     * Get a new Quaternion representing a rotation towards a specified forward direction. If
     * upInWorld is orthogonal to forwardInWorld, then the Y axis is aligned with desiredUpInWorld.
     */
    public static Quaternion lookRotation(Vector3 forwardInWorld, Vector3 desiredUpInWorld) {

        // Find the rotation between the world forward and the forward to look at.
        Quaternion rotateForwardToDesiredForward = new Quaternion();
        rotateForwardToDesiredForward.setFromCross(Vector3.X,forwardInWorld);

        // Recompute upwards so that it's perpendicular to the direction
        Vector3 rightInWorld = forwardInWorld.cpy().crs(desiredUpInWorld);
        Vector3 desiredUpInWorld2 = rightInWorld.cpy().crs(forwardInWorld);

        // Find the rotation between the "up" of the rotated object, and the desired up
        //Vector3 newUp = Quaternion.rotateVector(rotateForwardToDesiredForward, Vector3.up());
        //Quaternion rotateNewUpToUpwards = rotationBetweenVectors(newUp, desiredUpInWorld);

        return null;
    }
}
