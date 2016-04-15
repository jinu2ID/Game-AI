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

    GameObject target;
    GameVector oldPosition;
    GameVector currentPosition;
    boolean firstUpdate;

    public SeekController(GameObject _target){
        target = _target;
        firstUpdate = true;
        currentPosition = new GameVector(0,0);
    }
    public void update(Car subject, Game game, double delta_t, double[] controlVariables) {

        controlVariables[VARIABLE_STEERING] = 0;
        controlVariables[VARIABLE_THROTTLE] = 0;
        controlVariables[VARIABLE_BRAKE] = 0;

        // Get targets position
        GameVector endPosition = new GameVector(target.getX(), target.getY());
        //System.out.println("target position: " + endPosition.toString());

        GameVector accelRequest = seek(subject, endPosition);
        //System.out.println("accelRequest" + accelRequest.toString());

        // Update car's current position
        currentPosition.setXY(subject.getX(), subject.getY());
        //System.out.println("current position: " + currentPosition.toString());

        // If this is the first time update is called, save the current position as the old position
        if (firstUpdate){
            oldPosition = new GameVector(currentPosition.getX(), currentPosition.getY());
            firstUpdate = false;
        }
        //System.out.println("old position: " + oldPosition.toString());

        // Get current velocity
        GameVector currentVelocity = currentPosition.subtract(oldPosition);
        //System.out.println("currentVelocity" + currentVelocity.toString());

        double linearAccel = getLinearAccel(accelRequest, currentVelocity);
        System.out.println("linear acceleration:" + linearAccel);

        if (linearAccel > 0){
            controlVariables[VARIABLE_THROTTLE] = 1;
        }
        else if (linearAccel < 0){
            controlVariables[VARIABLE_BRAKE] = 1;
        }

        // replace old position to current position
        oldPosition = new GameVector(currentPosition.getX(), currentPosition.getY());

    }

    private GameVector seek(GameObject subject, GameVector end){

        // D = E - subject.position
        GameVector displacement = new GameVector(end.getX() - subject.getX(), end.getY() - subject.getY());
        //System.out.println("displacement: " + displacement.toString());

        // ND = D/|D|
        GameVector normDisplacement = displacement.normalize();
        //System.out.println("normalized displacement: " + normDisplacement.toString());

        return normDisplacement;
    }

    // Returns the scalar projection of v onto w
    private double getLinearAccel(GameVector v, GameVector w){

        double projectVW = v.dotProduct(w);
        double magnitudeW = w.magnitude();
        //System.out.println("projectVW" + projectVW);
        //System.out.println("magnitudeW" + magnitudeW);

        // Avoid divide by 0
        if (magnitudeW != 0)
         projectVW = projectVW/magnitudeW;
        else
            return .1;

        return projectVW;
    }

    private double getAngularAccel(GameVector v, GameVector w){
        GameVector right = new GameVector(v.getX(), v.getY() * -1);

        return right.dotProduct(w)/w.magnitude();
    }
}
