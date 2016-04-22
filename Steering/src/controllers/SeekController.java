package controllers;

import engine.Car;
import engine.Game;
import engine.GameObject;
import engine.GameVector;

/**
 * Created by jinuj on 4/13/2016.
 * Implements the seek steering behavior; subject will seek target
 */
public class SeekController extends Controller {

    private GameObject target;

    public SeekController(GameObject _target){
        target = _target;
    }

    public void update(Car subject, Game game, double delta_t, double[] controlVariables) {

        controlVariables[VARIABLE_STEERING] = 0;
        controlVariables[VARIABLE_THROTTLE] = 0;
        controlVariables[VARIABLE_BRAKE] = 0;

        // Get targets position
        GameVector endPosition = new GameVector(target.getX(), target.getY());

        // Get current direction
        GameVector dirVector = new GameVector(Math.cos(subject.getAngle()), Math.sin(subject.getAngle()));
        dirVector = dirVector.normalize();

        // Get acceleration request
        GameVector accelRequest = seek(subject, endPosition);

        // Throttle/Brake output filtered
        double linearAccel = getLinearAccel(accelRequest, dirVector);

        // Left/Right output filtered
        double angularAccel = getAngularAccel(accelRequest, dirVector);

        // Throttle/Brake
        if (linearAccel > 0){
            controlVariables[VARIABLE_THROTTLE] = 1;
        }
        else if (linearAccel < 0){
            controlVariables[VARIABLE_BRAKE] = 1;
        }

        // Turn Left/Right
        if (angularAccel > 0){
            controlVariables[VARIABLE_STEERING] = 1;
        }
        else if (angularAccel < 0){
            controlVariables[VARIABLE_STEERING] = -1;
        }

    }

    private GameVector seek(GameObject subject, GameVector end){

        // D = E - subject.position
        GameVector displacement = new GameVector(end.getX() - subject.getX(), end.getY() - subject.getY());

        // ND = D/|D|
        displacement = displacement.normalize();

        return displacement;
    }

    // Returns the projection of accelRequest onto dirVector
    private double getLinearAccel(GameVector accelRequest, GameVector dirVector){

        double projectVW = dirVector.dotProduct(accelRequest);
        return projectVW;
    }

    private double getAngularAccel(GameVector accelRequest, GameVector dirVector){

        double ca = Math.cos(Math.PI/2);
        double sa = Math.sin(Math.PI/2);
        GameVector right = new GameVector(ca * dirVector.getX() - sa * dirVector.getY(), sa * dirVector.getX() + ca * dirVector.getY());

        return (right.normalize().dotProduct(accelRequest));
    }


}
